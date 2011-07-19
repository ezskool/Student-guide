package studentguiden.ntnu.courses;

import studentguiden.ntnu.main.R;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class CourseMenu extends TabActivity{
	private TabHost mTabHost;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_menu);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		
		Intent timetableIntent = new Intent().setClass(this, TimetableActivity.class);
		setupTab(new TextView(this), getString(R.string.tab_timetable), timetableIntent);
		Intent coursesIntent = new Intent().setClass(this, MyCoursesActivity.class);
		setupTab(new TextView(this), getString(R.string.tab_my_courses), coursesIntent);
		
	}
	private void setupTab(final View view, final String tag, Intent intent) {
		View tabview = createTabView(mTabHost.getContext(), tag);
	        TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		mTabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

}
