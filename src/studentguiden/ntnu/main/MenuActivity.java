package studentguiden.ntnu.main;

import studentguiden.ntnu.courses.CourseMenuActivity;
import studentguiden.ntnu.timetable.TimetableActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity implements OnClickListener{
	
	private Button btn_timetable, btn_courses;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        btn_courses = (Button)findViewById(R.id.btn_courses);
        btn_timetable = (Button)findViewById(R.id.btn_timetable);
        
        btn_courses.setOnClickListener(this);
        btn_timetable.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		if(v==btn_courses) {
			Intent intent = new Intent(MenuActivity.this, CourseMenuActivity.class);
			startActivity(intent);
		}else if(v==btn_timetable) {
			Intent intent = new Intent(MenuActivity.this, TimetableActivity.class);
			startActivity(intent);
		}
		
	}
}