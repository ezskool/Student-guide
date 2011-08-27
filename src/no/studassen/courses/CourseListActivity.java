package no.studassen.courses;

import java.util.ArrayList;
import java.util.List;

import no.studassen.R;
import no.studassen.entities.Course;
import no.studassen.storage.DatabaseHelper;
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

/**
 * @author Håkon Drolsum Røkenes
 * 
 */

public class CourseListActivity extends ListActivity implements TextWatcher, OnClickListener{


	private EditText et_search;
	private SharedPreferences prefs;
	private ProgressDialog pd;
	private Dialog searchDialog;
	private List<Course> courses;
	private DatabaseHelper db;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_list);

		et_search = (EditText)findViewById(R.id.et_search_courses);
		et_search.addTextChangedListener(this);

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
		if(db!=null) {
			db.close();
		}
	}

	private void setListContent(String query) {
		if(query.length() >0) {
			List<Course> courses  = getDBInstance().getAutocompleteList(query);
			this.setListAdapter(new CourseListArrayAdapter(this, R.layout.my_courses_list_item, courses));
		}else {
			//TODO: dette er antakeligvis ikke en god måte å cleare listadapter
			this.setListAdapter(new CourseListArrayAdapter(this, R.layout.my_courses_list_item, new ArrayList<Course>()));
		}
	}


	@Override
	public void onClick(View v) {
	}


	//	@Override
	//	protected void onListItemClick(ListView l, View v, int position, long id) {
	//		super.onListItemClick(l, v, position, id);
	//		if(v == btn_add_course) {
	//			Util.log("clicked");
	//		}else {
	//			Course selectedCourse = (Course) this.getListAdapter().getItem(position);
	//			startCourseActivity(selectedCourse.getCode());
	//		}
	//	}

	/**
	 * Starts the CourseActivity class for a course id
	 * @param courseId the respective course id
	 */
	private void startCourseActivity(String code){
		Intent intent = new Intent(CourseListActivity.this, CourseActivity.class);
		intent.putExtra("code", code);
		startActivity(intent);
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
