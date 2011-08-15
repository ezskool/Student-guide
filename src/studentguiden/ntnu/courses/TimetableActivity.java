package studentguiden.ntnu.courses;

import java.sql.SQLException;
import java.util.List;

import studentguiden.ntnu.entities.Lecture;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.storage.DatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TimetableActivity extends Activity implements OnClickListener{

	private TextView tv_monday, tv_tuesday, tv_wednesday, tv_thursday, tv_friday, tv_statusbar_page, tv_home;
	private AutoCompleteTextView ac_search_courses;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timetable);

		tv_statusbar_page = (TextView)findViewById(R.id.tv_statusbar_page);
		tv_statusbar_page.setText("/"+getString(R.string.timetable));
		tv_home = (TextView)findViewById(R.id.tv_statusbar);
		tv_home.setOnClickListener(this);
		
		tv_monday = (TextView) findViewById(R.id.timetable_monday);
		tv_tuesday = (TextView)findViewById(R.id.timetable_tuesday);
		tv_wednesday = (TextView)findViewById(R.id.timetable_wednesday);
		tv_thursday = (TextView)findViewById(R.id.timetable_thursday);
		tv_friday = (TextView)findViewById(R.id.timetable_friday);
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		try {
			updateTimetable();
		} catch (SQLException e) {
			Util.log("Unable to retrieve timetable from db");
			e.printStackTrace();
		}
		
	}
	
	private void updateTimetable() throws SQLException {
		DatabaseHelper db = new DatabaseHelper(this);
		db.openReadableConnection();
		List<Lecture>  lectures = db.getMyLectures();
		db.close();
		
		//TODO: ikke veldig pent. fix
		tv_monday.setText("");
		tv_monday.setText("");
		tv_tuesday.setText("");
		tv_wednesday.setText("");
		tv_thursday.setText("");
		tv_friday.setText("");
		tv_friday.setText("");
		
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
		if(v==tv_home) {
			this.finish();
		}
	}
}
