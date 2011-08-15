package studentguiden.ntnu.courses;

import java.sql.SQLException;
import java.util.List;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.storage.DatabaseHelper;
import android.R.anim;
import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MyCoursesActivity extends ListActivity {
	private TextView tv_statusbar;
//	private ImageButton btn_remove_course;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_list);

		tv_statusbar = (TextView)findViewById(R.id.tv_statusbar);
		tv_statusbar.setText(getString(R.string.my_courses));

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
	}

	private void removeCourse() {
		DatabaseHelper db = new DatabaseHelper(this);
		db.openWritableConnection();
		//		db.removeMyCourse(course)
		db.close();
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
