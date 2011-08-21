package no.studassen.main;

import java.util.Locale;

import no.studassen.bus.BusActivity;
import no.studassen.courses.CourseTabwidget;
import no.studassen.dinner.DinnerActivity;
import no.studassen.misc.CourseDownloader;
import no.studassen.misc.Globals;
import no.studassen.misc.Util;
import no.studassen.social.SocialActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MenuActivity extends Activity implements OnClickListener{
	private ImageView btn_dinner, btn_bus, btn_course, btn_social;
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		initializeViewElements();
		checkLanguage();
		//TODO: fiks sjekk om database er updated ok.
		if(Util.hasCourseDataExpired(this) && !Globals.hasCalledCourseDownloader) {
			new CourseDownloader(this).execute();
			Globals.hasCalledCourseDownloader = true;
		}else {
			Util.log("Course data already downloaded and cache has not expired: skipping download of course content");
		}
	}
	
	private void checkLanguage() {
		prefs = getSharedPreferences("studassen", MODE_PRIVATE);
		String language = Locale.getDefault().getDisplayLanguage();
		if(language.contains("Norsk")) {
			prefs.edit().putBoolean("lang_no", true).commit();
		}else{
			prefs.edit().putBoolean("lang_no", false).commit();
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
		
	}

	@Override
	public void onClick(View v) {
		if(v==btn_dinner) {
			Intent intent = new Intent(this, DinnerActivity.class);
			startActivity(intent);
		}else if(v==btn_course) {
			Intent intent = new Intent(this, CourseTabwidget.class);
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
