package studentguiden.ntnu.courses;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.storage.DatabaseHelper;
import studentguiden.ntnu.storage.entities.Course;
import studentguiden.ntnu.storage.entities.MetaCourse;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * @author Håkon Drolsum Røkenes
 * 
 */

public class CourseListActivity extends ListActivity implements TextWatcher, OnClickListener{

	private final int DIALOG_SEARCH_COURSE = 0;

	private EditText et_search;
	private ImageView btn_back;
	//	private ArrayList<Course> courseList;
	private SharedPreferences prefs;
	private ProgressDialog pd;
	private Dialog searchDialog;
	//	private ContentUpdater updater;
	private ImageView btn_add_course;
	private List<MetaCourse> courses;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_menu);
		btn_add_course = (ImageView)findViewById(R.id.img_item_icon);

		initializeViewElements();
		fetchCourses();
		setListContent(et_search.getText().toString());		
	}

	private void fetchCourses() {
		DatabaseHelper db = new DatabaseHelper(this);
		try {
			courses = db.getAllCourses();
			Util.log(courses.size()+" courses fetched from database");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void initializeViewElements() {
		btn_back = (ImageView)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		et_search = (EditText)findViewById(R.id.et_search);
		et_search.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v==btn_back) {
			super.finish();
		}
	}

	private void setListContent(String searchString) {
		if(searchString=="" || searchString==null) {
			this.setListAdapter(new CourseListArrayAdapter(this, R.layout.list_item, courses));
		}else {
			searchString.toLowerCase();
			Util.log("filtering course list, on searchString: "+searchString);
			ArrayList<MetaCourse> filteredCourseList = new ArrayList<MetaCourse>();
			for (MetaCourse course : courses) {
				if(course.getCourseText().toLowerCase().contains(searchString.toLowerCase())) {
					filteredCourseList.add(course);
				}
			}
			this.setListAdapter(new CourseListArrayAdapter(this, android.R.layout.simple_list_item_1, filteredCourseList));
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if(v == btn_add_course) {
			Util.log("clicked");
		}else {
			MetaCourse selectedCourse = (MetaCourse) this.getListAdapter().getItem(position);
			startCourseActivity(selectedCourse.getCode());
		}
	}

	/**
	 * Starts the CourseActivity class for a course id
	 * @param courseId the respective course id
	 */
	private void startCourseActivity(String code){
		Intent intent = new Intent(CourseListActivity.this, CourseActivity.class);
		intent.putExtra("code", code);
		startActivity(intent);
	}

	protected Dialog onCreateDialog(int id) {
		if(id==DIALOG_SEARCH_COURSE) {
			return searchDialog;
		}
		return null;
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		setListContent(et_search.getText().toString());
	}


	




	//	private class ContentUpdater extends CountDownTimer {
	//		private boolean keepChecking = true;
	//
	//		public ContentUpdater(long millisInFuture, long countDownInterval) {
	//			super(millisInFuture, countDownInterval);
	//		}
	//
	//		@Override
	//		public void onTick(long millisUntilFinished) {
	//			if(CourseUtilities.getCourseList() != null && keepChecking) {
	//				CourseListActivity.this.setListContent(et_search.getText().toString());
	//				CourseListActivity.this.pd.cancel();
	//				keepChecking = false;
	//			}
	//		}
	//
	//		@Override
	//		public void onFinish() {
	//			// TODO Auto-generated method stub
	//			
	//		}
	//
	//	}



}
