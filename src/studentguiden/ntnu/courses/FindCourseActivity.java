package studentguiden.ntnu.courses;



import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.Util;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class FindCourseActivity extends ListActivity implements OnClickListener, OnKeyListener{

	private Button btn_search;
	private EditText et_search_text;
	private ArrayList<Course> courseList;
	private SharedPreferences prefs;
	private ProgressDialog pd;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_menu);

		Util.log("starting findcourseactivity");
		prefs = getSharedPreferences("student-guide", MODE_PRIVATE);


		new ContentParser().execute(prefs.getString("rawCourseData", ""));
		
		pd = ProgressDialog.show(this, "", "Downloading content", true);
		

		btn_search = (Button)findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);

		et_search_text = (EditText)findViewById(R.id.et_course_search);
		et_search_text.setOnKeyListener(this);
	}

	private void setListContent(String searchString) {
		if(searchString=="" || searchString==null) {
			this.setListAdapter(new CourseListArrayAdapter(this, R.layout.list_item, courseList));
		}else {
			searchString.toLowerCase();
			Util.log("filtering course list, on searchString "+searchString);
			ArrayList<Course> filteredCourseList = new ArrayList<Course>();
			for (Course course : courseList) {
				if(course.getCourseText().toLowerCase().contains(searchString)) {
					filteredCourseList.add(course);
				}
			}
			this.setListAdapter(new CourseListArrayAdapter(this, R.layout.list_item, filteredCourseList));
		}
	}


	public void setCourseList(ArrayList<Course> courses) {
		this.courseList = courses;
		setListContent("");
	}
	


	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(v==et_search_text) {
			setListContent(et_search_text.getText().toString());
		}
		return false;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Course selectedCourse = (Course) this.getListAdapter().getItem(position);
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

	@Override
	public void onClick(View v) {
		if(v==btn_search) {
			setListContent(et_search_text.getText().toString());
		}
	}


	private class ContentParser extends AsyncTask<String, Void, Integer> {
		private final int PARSING_FAILED = 0;
		private final int PARSING_SUCCESFUL = 1;
		private final int DOWNLOAD_FAILED = 2;
		private ArrayList<Course> courses = new ArrayList<Course>();
		private String rawData;

		@Override
		protected Integer doInBackground(String... params) {
			try {
				if(params[0].equals("")) {
					URL url = new URL(params[0]);
					rawData = Util.downloadContent(url);
					prefs.edit().putString("rawCourseData", rawData).commit();
				} else{
					rawData = params[0];
				}
				
				JSONArray jsonCourseArray = new JSONObject(rawData).getJSONArray("course");
				int amountOfCourses = jsonCourseArray.length();


				for (int i = 0; i < amountOfCourses; i++) {
					JSONObject temp = jsonCourseArray.getJSONObject(i);
					courses.add(new Course(temp.getString("code"), temp.getString("name")));
				}
			} catch (JSONException e) {
				Util.log("parsing courses failed: JSONException");
				e.printStackTrace();
				return PARSING_FAILED;
			} catch(IOException e) {
				Util.log("Course content download failed: IOException");
				e.printStackTrace();
				return DOWNLOAD_FAILED;
			}

			return PARSING_SUCCESFUL;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == PARSING_SUCCESFUL) {
				Util.log("Course list parsing was successful");
				FindCourseActivity.this.setCourseList(courses);
				FindCourseActivity.this.pd.cancel();
			}	
		}

	}







}
