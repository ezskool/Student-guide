package studentguiden.ntnu.home;

import studentguiden.ntnu.bus.BusActivity;
import studentguiden.ntnu.courses.CourseMenu;
import studentguiden.ntnu.courses.TimetableActivity;
import studentguiden.ntnu.dinner.DinnerActivity;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.CourseDownloader;
import studentguiden.ntnu.misc.Globals;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.social.SocialActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends Activity implements OnClickListener{
	private ImageView btn_dinner, btn_bus, btn_course, btn_social;
	private SharedPreferences prefs;
	private TextView tv_statusbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
		initializeViewElements();
	
		//TODO: fiks sjekk om database er updated ok.
		if(Util.hasCourseDataExpired(this)) {
			new CourseDownloader(this).execute();
			Globals.hasCalledCourseDownloader = true;
		}else {
			Util.log("Course data already downloaded and cache has not expired: skipping download of course content");
		}
	}
	
	private void initializeViewElements() {
		btn_dinner = (ImageView)findViewById(R.id.btn_dinner);
		btn_dinner.setOnClickListener(this);
		btn_bus = (ImageView)findViewById(R.id.btn_bus);
		btn_bus.setOnClickListener(this);
		btn_course = (ImageView)findViewById(R.id.btn_course);
		btn_course.setOnClickListener(this);
		btn_social = (ImageView)findViewById(R.id.btn_social);
		btn_social.setOnClickListener(this);
		
		tv_statusbar = (TextView)findViewById(R.id.tv_statusbar);
		tv_statusbar.setText(getString(R.string.app_name));
		
	}

	@Override
	public void onClick(View v) {
		if(v==btn_dinner) {
			Intent intent = new Intent(this, DinnerActivity.class);
			startActivity(intent);
		}else if(v==btn_course) {
			Intent intent = new Intent(this, CourseMenu.class);
			startActivity(intent);
		}else if(v==btn_bus) {
			Intent intent = new Intent(this, BusActivity.class);
			startActivity(intent);
		}else if(v==btn_social) {
			Intent intent = new Intent(this, SocialActivity.class);
			startActivity(intent);
		}		
	}

}
