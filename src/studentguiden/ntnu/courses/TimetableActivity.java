package studentguiden.ntnu.courses;

import java.util.ArrayList;

import studentguiden.ntnu.entities.Lecture;
import studentguiden.ntnu.main.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TimetableActivity extends Activity implements OnClickListener{

	private ImageView btn_back;
	private Button btn_my_courses;
	private TextView tv_monday, tv_tuesday, tv_wednesday, tv_thursday, tv_friday, tv_statusbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timetable);

		tv_statusbar = (TextView)findViewById(R.id.tv_statusbar);
		tv_statusbar.setText(getString(R.string.timetable));
		
		btn_back = (ImageView)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
		btn_my_courses = (Button)findViewById(R.id.btn_my_courses);
		btn_my_courses.setOnClickListener(this);
		
		tv_monday = (TextView) findViewById(R.id.timetable_monday);
		tv_tuesday = (TextView)findViewById(R.id.timetable_tuesday);
		tv_wednesday = (TextView)findViewById(R.id.timetable_wednesday);
		tv_thursday = (TextView)findViewById(R.id.timetable_thursday);
		tv_friday = (TextView)findViewById(R.id.timetable_friday);

		updateTimetable();
	}

	private void updateTimetable() {
		ArrayList<Lecture> lectures = CourseUtilities.getMyLectures(this);
	
		for (Lecture lecture : lectures) {
			int day = lecture.getDayNumber();
			if(day==0) {
				appendLectureText(tv_monday, lecture);
			}else if(day==1) {
				appendLectureText(tv_tuesday, lecture);
			}else if(day==2) {
				appendLectureText(tv_wednesday, lecture);
			}else if(day==3) {
				appendLectureText(tv_thursday, lecture);
			}else if(day==4) {
				appendLectureText(tv_friday, lecture);
			}
		}

	}
	
	private void appendLectureText(TextView tv, Lecture lecture) {
		tv.append(lecture.getStart()+" - "+lecture.getEnd()+" "+lecture.getCourseCode()+" "+getString(R.string.room)+" "+lecture.getRoom()+"\n");
	}

	@Override
	public void onClick(View v) {
		if(v==btn_back) {
			super.finish();
		}else if(v==btn_my_courses) {
			Intent intent = new Intent(this, MyCoursesActivity.class);
			startActivity(intent);
		}
	}
}
