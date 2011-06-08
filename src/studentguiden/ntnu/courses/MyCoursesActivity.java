package studentguiden.ntnu.courses;

import java.util.ArrayList;

import studentguiden.ntnu.entities.MetaCourse;
import studentguiden.ntnu.main.R;
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
		ArrayList<MetaCourse> courseList = CourseUtilities.getMyCourses(this);
		
		for (MetaCourse course : courseList) {
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
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
