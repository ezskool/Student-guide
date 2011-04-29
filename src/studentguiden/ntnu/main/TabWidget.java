package studentguiden.ntnu.main;


import studentguiden.ntnu.courses.FindCourseActivity;
import studentguiden.ntnu.dinner.DinnerActivity;
import studentguiden.ntnu.dinner.SelectCampusActivity;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.social.SocialActivity;
import studentguiden.ntnu.timetable.TimetableActivity;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class TabWidget extends TabActivity{
	private TabHost.TabSpec homeTab, socialTab, courseTab, dinnerTab;
	private Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);
		
		Resources res = getResources();
		TabHost tabHost = getTabHost(); 

		intent = new Intent().setClass(this, HomeActivity.class);
		homeTab = tabHost.newTabSpec("Home").setIndicator("Home", res.getDrawable(R.drawable.ic_home)).setContent(intent);
		tabHost.addTab(homeTab);

		intent = new Intent().setClass(this, SocialActivity.class);
		socialTab = tabHost.newTabSpec("Social").setIndicator("Social", res.getDrawable(R.drawable.ic_tab_artists)).setContent(intent);
		tabHost.addTab(socialTab);

		intent = new Intent().setClass(this, FindCourseActivity.class);
		courseTab = tabHost.newTabSpec("Courses").setIndicator("Courses",res.getDrawable(R.drawable.ic_options)).setContent(intent);
		tabHost.addTab(courseTab);

		intent = new Intent().setClass(this, SelectCampusActivity.class);
		dinnerTab = tabHost.newTabSpec("Dinner").setIndicator("Dinner",res.getDrawable(R.drawable.ic_dinner)).setContent(intent);
		tabHost.addTab(dinnerTab);

		tabHost.setCurrentTab(0);
	}
}
