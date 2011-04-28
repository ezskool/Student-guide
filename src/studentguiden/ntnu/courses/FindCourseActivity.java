package studentguiden.ntnu.courses;

import java.util.ArrayList;

import studentguiden.ntnu.entities.MetaCourse;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.Util;
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
import android.widget.EditText;
import android.widget.ListView;

/**
 * @author Håkon Drolsum Røkenes
 * 
 */

public class FindCourseActivity extends ListActivity implements TextWatcher{

	private final int DIALOG_SEARCH_COURSE = 0;

	private EditText et_search;
	//	private ArrayList<Course> courseList;
	private SharedPreferences prefs;
	private ProgressDialog pd;
	private Dialog searchDialog;
	private ContentUpdater updater;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_menu);

		Util.log("starting findcourseactivity");
		prefs = getSharedPreferences("student-guide", MODE_PRIVATE);
		initializeViewElements();

		if(CourseUtilities.getCourseList() == null) {
			updater = new ContentUpdater(10000, 1000);
			updater.start();
			pd = ProgressDialog.show(this, "", "Downloading content", true);

		}else {
			setListContent(et_search.getText().toString());
			//TODO: lag en refresh knapp, i tilfelle course content ikke har rukket å laste ned innen man trykker inn hit?
		}
	}

	private void initializeViewElements() {

		et_search = (EditText)findViewById(R.id.et_search);
		et_search.addTextChangedListener(this);
	}

	private void setListContent(String searchString) {
		if(searchString=="" || searchString==null) {
			this.setListAdapter(new CourseListArrayAdapter(this, R.layout.list_item, CourseUtilities.getCourseList()));
		}else {
			searchString.toLowerCase();
			Util.log("filtering course list, on searchString: "+searchString);
			ArrayList<MetaCourse> filteredCourseList = new ArrayList<MetaCourse>();
			for (MetaCourse course : CourseUtilities.getCourseList()) {
				if(course.getCourseText().toLowerCase().contains(searchString)) {
					filteredCourseList.add(course);
				}
			}
			this.setListAdapter(new CourseListArrayAdapter(this, R.layout.list_item, filteredCourseList));
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		MetaCourse selectedCourse = (MetaCourse) this.getListAdapter().getItem(position);
		startCourseActivity(selectedCourse.getCode());
	}

	/**
	 * Starts the CourseActivity class for a course id
	 * @param courseId the respective course id
	 */
	private void startCourseActivity(String courseId){
		Intent intent = new Intent(FindCourseActivity.this, CourseActivity.class);
		intent.putExtra("courseId", courseId);
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

	private class ContentUpdater extends CountDownTimer {
		private boolean keepChecking = true;
		
		public ContentUpdater(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {

		}

		@Override
		public void onTick(long millisUntilFinished) {
			if(CourseUtilities.getCourseList() != null && keepChecking) {
				FindCourseActivity.this.setListContent(et_search.getText().toString());
				FindCourseActivity.this.pd.cancel();
				keepChecking = false;
			}
		}

	}

}
