package studentguiden.ntnu.courses;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import studentguiden.ntnu.entities.Lecture;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.storage.DatabaseHelper;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TimetableActivity extends Activity implements OnClickListener{

	private LinearLayout layout_monday, layout_tuesday, layout_wednesday, layout_thursday, layout_friday;
	private AutoCompleteTextView ac_search_courses;
	private TextView tv_week;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timetable);

		layout_monday = (LinearLayout) findViewById(R.id.timetable_container_monday);
		layout_tuesday = (LinearLayout)findViewById(R.id.timetable_container_tuesday);
		layout_wednesday = (LinearLayout)findViewById(R.id.timetable_container_wednesday);
		layout_thursday = (LinearLayout)findViewById(R.id.timetable_container_thursday);
		layout_friday = (LinearLayout)findViewById(R.id.timetable_container_friday);
		tv_week  = (TextView)findViewById(R.id.tv_timetable_week);


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

		tv_week.setText(getString(R.string.week_header)+" "+Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
		
		layout_monday.removeAllViews();
		layout_tuesday.removeAllViews();
		layout_wednesday.removeAllViews();
		layout_thursday.removeAllViews();
		layout_friday.removeAllViews();

		Util.log("printing "+lectures.size()+" lectures");
		for (Lecture lecture : lectures) {
//			if(Util.isLectureThisWeek(lecture)) {
			Util.log("printing lecture on day:"+lecture.getDay()+"("+lecture.getDayNumber()+"). code: "+lecture.getCourseCode());
				int day = lecture.getDayNumber();
				if(day==0) {
					layout_monday.addView(createLectureText(lecture));
				}else if(day==1) {
					layout_tuesday.addView(createLectureText(lecture));
				}else if(day==2) {
					layout_wednesday.addView(createLectureText(lecture));
				}else if(day==3) {
					layout_thursday.addView(createLectureText(lecture));
				}else if(day==4) {
					layout_friday.addView(createLectureText(lecture));
				}else {
					Util.log("some course did not get printed, on day:"+lecture.getDay()+"("+lecture.getDayNumber()+"). code: "+lecture.getCourseCode());
				}
//			}
		}


	}

	private TextView createLectureText(Lecture lecture) {
		TextView temp = new TextView(this);
		temp.setText(lecture.getStart()+" - "+lecture.getEnd()+" "+lecture.getCourseCode()+" "+getString(R.string.room)+" "+lecture.getRoom()+"\n");
		temp.setBackgroundColor(Integer.parseInt(lecture.getColor()));
		return temp;
	}

	@Override
	public void onClick(View v) {
//		if(v==tv_home) {
//			this.finish();
//		}
	}
}
