package no.studassen.courses;

import no.studassen.misc.CourseDownloader;
import no.studassen.main.R;
import no.studassen.misc.Globals;
import no.studassen.misc.Util;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class CourseTabwidget extends TabActivity {
	private TabHost mTabHost;
//	private AutoCompleteTextView ac_search_courses;
//	private Cursor c;
//	private CourseCursorAdapter cursorAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabwidget_course_menu);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);

		Intent timetableIntent = new Intent().setClass(this, TimetableActivity.class);
		setupTab(new TextView(this), getString(R.string.tab_timetable), timetableIntent);
		Intent coursesIntent = new Intent().setClass(this, MyCoursesActivity.class);
		setupTab(new TextView(this), getString(R.string.tab_my_courses), coursesIntent);
		Intent findCoursesIntent = new Intent().setClass(this, CourseListActivity.class);
		setupTab(new TextView(this), getString(R.string.tab_find_courses), findCoursesIntent);

//		initCursorAdapter();
//		initItemFilter();
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


//	public void initCursorAdapter(){
//		ac_search_courses = (AutoCompleteTextView) findViewById(R.id.ac_search_course);
//		DatabaseHelper db = new DatabaseHelper(this);
//		db.openReadableConnection();
//
//		c = db.getAutocompleteCursor("");
//		startManagingCursor(c);
//
//		cursorAdapter = new CourseCursorAdapter(this, c);
//		db.close();
//	}
//
//	private void initItemFilter() {
//		ac_search_courses = (AutoCompleteTextView) findViewById(R.id.ac_search_course);
//		ac_search_courses.setAdapter(cursorAdapter);
//		ac_search_courses.setThreshold(1);
//	}
}