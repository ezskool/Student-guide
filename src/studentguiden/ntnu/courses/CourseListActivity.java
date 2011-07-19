package studentguiden.ntnu.courses;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.storage.DatabaseHelper;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
	//	private ArrayList<Course> courseList;
	private SharedPreferences prefs;
	private ProgressDialog pd;
	private Dialog searchDialog;
	//	private ContentUpdater updater;
	private ImageView btn_add_course;
	private List<Course> courses;
	private DatabaseHelper db;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_list);

		initializeViewElements();
//		fetchCourses();
//		setListContent(et_search.getText().toString());
	}

	/**
	 * Lazily instantiates the database object, if needed
	 * @return
	 */
	private DatabaseHelper getDBInstance() {
		if(db == null) {
			db = new DatabaseHelper(this);
			db.openReadableConnection();
		}else if(!db.isOpen()){
			db.openReadableConnection();
		}
		return db;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		db.close();
	}
	
	private ArrayList<Course> autocomplete(String query) {
		ArrayList<Course> list = (ArrayList<Course>)getDBInstance().getAutocomplete(query);
		return list;
	}

	private void initializeViewElements() {

		et_search = (EditText)findViewById(R.id.et_search);
		et_search.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
	}

	private void setListContent(String searchString) {
		if(!(searchString=="" || searchString==null)) {
			searchString.toLowerCase();
			ArrayList<Course> autoCompleteList = autocomplete(searchString);
			this.setListAdapter(new CourseListArrayAdapter(this, android.R.layout.simple_list_item_1, autoCompleteList));
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if(v == btn_add_course) {
			Util.log("clicked");
		}else {
			Course selectedCourse = (Course) this.getListAdapter().getItem(position);
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
}
