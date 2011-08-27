package no.studassen.courses;

import java.sql.SQLException;
import no.studassen.R;
import java.util.List;

import no.studassen.entities.Course;
import no.studassen.misc.Util;
import no.studassen.storage.DatabaseHelper;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MyCoursesActivity extends ListActivity implements OnClickListener{
	private TextView tv_statusbar_page, tv_home;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_courses);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		fetchCourses();
		}

	private void fetchCourses() {
		DatabaseHelper db = new DatabaseHelper(this);
		db.openReadableConnection();
		try {
			List<Course> myCourses = db.getMyCourses();
			this.setListAdapter(new CourseListArrayAdapter(this, R.layout.my_courses_list_item, myCourses));
			
		} catch (SQLException e) {
			Util.log("Unable to retrieve my courses");
			e.printStackTrace();
		}
		db.close();
	}

	private void removeCourse() {
		DatabaseHelper db = new DatabaseHelper(this);
		db.openWritableConnection();
		//		db.removeMyCourse(course)
		db.close();
	}

	@Override
	public void onClick(View v) {
//		if(v==tv_home) {
//			this.finish();
//		}
	}
	


	//
	//	private void updateCourseList() {
	//		my_courses_list.removeAllViews();
	//		DatabaseHelper db = new DatabaseHelper(this);
	//		db.openReadableConnection();
	//		List<Course> courseList;
	//		try {
	//			courseList = db.getMyCourses();
	//			db.close();
	//			for (Course course : courseList) {
	//				addCourseToView(course);
	//			}
	//		} catch (SQLException e) {
	//			Util.log("Failed to retrieve my courses");
	//			e.printStackTrace();
	//		}
	//	}



}
