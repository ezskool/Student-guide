package studentguiden.ntnu.main;

import studentguiden.ntnu.courses.FindCourseActivity;
import studentguiden.ntnu.dinner.DinnerActivity;
import studentguiden.ntnu.dinner.SelectCampusActivity;
import studentguiden.ntnu.misc.CourseDownloader;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.news.NewsActivity;
import studentguiden.ntnu.social.SocialActivity;
import studentguiden.ntnu.timetable.TimetableActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends Activity implements OnClickListener{
	private ImageView btn_dinner, btn_bus, btn_course, btn_news, btn_timetable, btn_social, btn_refresh, btn_back;
	private SharedPreferences prefs;
	private TextView tv_statusbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		prefs = getSharedPreferences("student-guide", MODE_PRIVATE);
		
		initializeViewElements();
	
		if(!Globals.hasCalledCourseDownloader) {
			new CourseDownloader(this, prefs).execute();
			Globals.hasCalledCourseDownloader = true;
		}
	}
	
	private void initializeViewElements() {
		btn_dinner = (ImageView)findViewById(R.id.btn_dinner);
		btn_dinner.setOnClickListener(this);
		btn_bus = (ImageView)findViewById(R.id.btn_bus);
		btn_bus.setOnClickListener(this);
		btn_course = (ImageView)findViewById(R.id.btn_course);
		btn_course.setOnClickListener(this);
		btn_news = (ImageView)findViewById(R.id.btn_news);
		btn_news.setOnClickListener(this);
		btn_timetable = (ImageView)findViewById(R.id.btn_timetable);
		btn_timetable.setOnClickListener(this);
		btn_social = (ImageView)findViewById(R.id.btn_social);
		btn_social.setOnClickListener(this);
		
		btn_refresh = (ImageView)findViewById(R.id.btn_refresh);
		btn_refresh.setVisibility(btn_refresh.GONE);
		btn_back = (ImageView)findViewById(R.id.btn_back);
		btn_back.setVisibility(btn_back.GONE);
		
		tv_statusbar = (TextView)findViewById(R.id.tv_statusbar);
		tv_statusbar.setText(getString(R.string.app_name));
		
	}

	@Override
	public void onClick(View v) {
		if(v==btn_dinner) {
			Intent intent = new Intent(this, SelectCampusActivity.class);
			startActivity(intent);
		}else if(v==btn_course) {
			Intent intent = new Intent(this, FindCourseActivity.class);
			startActivity(intent);
		}else if(v==btn_bus) {
			//TODO:addbuss
		}else if(v==btn_social) {
			Intent intent = new Intent(this, SocialActivity.class);
			startActivity(intent);
		}else if(v==btn_news) {
			Intent intent = new Intent(this, NewsActivity.class);
			startActivity(intent);
		}else if(v==btn_timetable) {
			Intent intent = new Intent(this, TimetableActivity.class);
			startActivity(intent);
		}
		
	}

}
