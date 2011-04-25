package studentguiden.ntnu.courses;



import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import studentguiden.ntnu.main.R;
import studentguiden.ntnu.main.Util;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * @author Håkon Drolsum Røkenes
 * 
 */

public class FindCourseActivity extends ListActivity implements OnClickListener{

	private Button btn_search;
	private EditText et_search_text;
	private ArrayList<Course> courseList;
	private SharedPreferences prefs;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_menu);

		Util.log("starting findcourseactivity");
		prefs = getSharedPreferences("student-guide", MODE_PRIVATE);

		if(prefs.getBoolean("hasDownloaded", false)){
			Util.log("starting to parse raw course data");
			new ContentParser().execute(prefs.getString("rawCourseData", ""));
		}else {
			Util.log("course data not found");
			//TODO: prøve å laste ned igjen?
		}

		btn_search = (Button)findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);

		et_search_text = (EditText)findViewById(R.id.et_course_search);
	}

	private void setListContent(String searchString) {
		if(searchString=="" || searchString==null) {
			this.setListAdapter(new courseListArrayAdapter(this, R.layout.list_item, courseList));
		}else {
			searchString.toLowerCase();
			Util.log("filtering course list, on searchString "+searchString);
			ArrayList<Course> filteredCourseList = new ArrayList<Course>();
			for (Course course : courseList) {
				if(course.getCourseText().toLowerCase().contains(searchString)) {
					filteredCourseList.add(course);
					Util.log("adding course to filtered list "+course.getCode());
				}
			}
			this.setListAdapter(new courseListArrayAdapter(this, R.layout.list_item, filteredCourseList));
		}
	}


	public void setCourseList(ArrayList<Course> courses) {
		this.courseList = courses;
		setListContent("");
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
		private ArrayList<Course> courses = new ArrayList<Course>();


		@Override
		protected Integer doInBackground(String... params) {
			try {
				JSONArray jsonCourseArray = new JSONObject(params[0]).getJSONArray("course");
				int amountOfCourses = jsonCourseArray.length();


				for (int i = 0; i < amountOfCourses; i++) {
					JSONObject temp = jsonCourseArray.getJSONObject(i);
					courses.add(new Course(temp.getString("code"), temp.getString("name")));
				}
			} catch (JSONException e) {
				Util.log("parsing courses failed: JSONException");
				e.printStackTrace();
				return PARSING_FAILED;
			} 

			return PARSING_SUCCESFUL;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == PARSING_SUCCESFUL) {
				Util.log("Course list parsing was successful");
				FindCourseActivity.this.setCourseList(courses);
			}	
		}

	}



}
