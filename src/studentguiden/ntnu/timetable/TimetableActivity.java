package studentguiden.ntnu.timetable;

import java.util.ArrayList;



import studentguiden.ntnu.courses.CourseUtilities;
import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.Lecture;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.Util;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimetableActivity extends Activity implements OnClickListener{

	private ImageView btn_back, btn_refresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timetable);

		btn_refresh = (ImageView)findViewById(R.id.btn_refresh);
		btn_back = (ImageView)findViewById(R.id.btn_back);
		btn_refresh.setOnClickListener(this);
		btn_back.setOnClickListener(this);

		updateTimetable();
	}

	public void updateView(ArrayList<Course> courses) {
		LinearLayout lecture_row = (LinearLayout)findViewById(R.id.lecture_row);
		boolean hasLecture = false;

		for (Course course : courses) {
			for (Lecture lecture : course.getLectureList()) {
				if(Util.isLectureToday(lecture)) {
					TextView  tv = new TextView(this);
					tv.setText(lecture.getStart()+" - "+lecture.getEnd()+"   "+course.getCode()+" - Room: " +lecture.getRoom());
					lecture_row.addView(tv);

					hasLecture = true;
				}
			}
		}
		if(!hasLecture) {
			TextView  tv = new TextView(this);
			tv.setText(getString(R.string.no_lectures));
			lecture_row.addView(tv);
		}
	}

	private void updateTimetable() {
		LinearLayout lecture_row = (LinearLayout)findViewById(R.id.lecture_row);
		ArrayList<Lecture> lectures = CourseUtilities.getMyLectures(this);
		boolean hasLecture = false;

		for (Lecture lecture : lectures) {
			if(Util.isLectureToday(lecture)) {
				TextView tv = new TextView(this);
				tv.setText(lecture.getCourseCode()+" "+lecture.getStart()+" - "+lecture.getEnd()+"   "+" - Room: " +lecture.getRoom());
				lecture_row.addView(tv);

				hasLecture = true;
			}
		}

		if(!hasLecture) {
			TextView  tv = new TextView(this);
			tv.setText(getString(R.string.no_lectures));
			lecture_row.addView(tv);
		}
	}

	@Override
	public void onClick(View v) {
		if(v==btn_back) {
			super.finish();
		}else if(v==btn_refresh) {
			updateTimetable();
		}
	}
}
