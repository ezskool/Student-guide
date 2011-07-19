package studentguiden.ntnu.courses;

import java.sql.SQLException;
import java.util.List;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.storage.DatabaseHelper;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCoursesActivity extends Activity implements OnClickListener{
	private TextView tv_statusbar;
	private LinearLayout my_courses_list;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_courses);

		tv_statusbar = (TextView)findViewById(R.id.tv_statusbar);
		tv_statusbar.setText(getString(R.string.my_courses));

		my_courses_list = (LinearLayout)findViewById(R.id.my_courses_list);
		updateCourseList();
	}

	private void updateCourseList() {
		DatabaseHelper db = new DatabaseHelper(this);
		db.openReadableConnection();
		List<Course> courseList;
		try {
			courseList = db.getMyCourses();
			db.close();
			for (Course course : courseList) {
				addCourseToView(course);
			}
		} catch (SQLException e) {
			Util.log("Failed to retrieve my courses");
			e.printStackTrace();
		}
	}

	/**
	 * adds course name to view
	 * @param course
	 */
	private void addCourseToView(Course course) {
		TextView tv = new TextView(this);
		tv.setText(course.getCourseText());
		tv.setTextSize(16);
		my_courses_list.addView(tv);

		View line= new View(this);
		line.setBackgroundResource(R.drawable.line_timetable);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, 2);
		line.setLayoutParams(params);
		my_courses_list.addView(line);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
