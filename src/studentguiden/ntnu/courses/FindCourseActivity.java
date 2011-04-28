package studentguiden.ntnu.courses;



import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.MetaCourse;
import studentguiden.ntnu.main.Globals;
import studentguiden.ntnu.main.HomeActivity;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.Util;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_menu);

		Util.log("starting findcourseactivity");
		prefs = getSharedPreferences("student-guide", MODE_PRIVATE);

		initializeViewElements();
		
		if(CourseUtilities.getCourseList() == null) {
//			pd = ProgressDialog.show(this, "", "Downloading content", true);
//			new ContentParser().execute(prefs.getString("rawCourseData", ""));
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
			Util.log("filtering course list, on searchString "+searchString);
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
	
//	public class ContentParser extends AsyncTask<String, Void, Integer> {
//		private final int PARSING_FAILED = 0;
//		private final int PARSING_SUCCESFUL = 1;
//		private final int DOWNLOAD_FAILED = 2;
//		private ArrayList<Course> courses = new ArrayList<Course>();
//		private String rawData;
//
//		@Override
//		protected Integer doInBackground(String... params) {
//			try {
//				if(params[0].equals("")) {
//					URL url = new URL(Globals.courseListURL);
//					rawData = Util.downloadContent(url);
//				
//				} else{
//					rawData = params[0];
//				}
//
//				JSONArray jsonCourseArray = new JSONObject(rawData).getJSONArray("course");
//				int amountOfCourses = jsonCourseArray.length();
//
//
//				for (int i = 0; i < amountOfCourses; i++) {
//					JSONObject temp = jsonCourseArray.getJSONObject(i);
//					courses.add(new Course(temp.getString("code"), temp.getString("name")));
//				}
//			} catch (JSONException e) {
//				Util.log("parsing courses failed: JSONException");
//				e.printStackTrace();
//				return PARSING_FAILED;
//			} catch(IOException e) {
//				Util.log("Course content download failed: IOException");
//				e.printStackTrace();
//				return DOWNLOAD_FAILED;
//			}
//			return PARSING_SUCCESFUL;
//		}
//
//		@Override
//		protected void onPostExecute(Integer result) {
//			if(result == PARSING_SUCCESFUL) {
//				Util.log("Course list parsing was successful");
//				prefs.edit().putString("rawData", rawData).commit();
//				prefs.edit().putBoolean("hasDownloaded", true).commit();
//				CourseUtilities.setCourseList(courses);
//				FindCourseActivity.this.setListContent("");
//				FindCourseActivity.this.pd.cancel();
//			}else {
//				FindCourseActivity.this.pd.cancel();
//				//TODO: feilmelding om man ikke har nett osv
//			}
//		}
//	}

}
