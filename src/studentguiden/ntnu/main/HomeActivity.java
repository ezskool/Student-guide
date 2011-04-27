package studentguiden.ntnu.main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import studentguiden.ntnu.courses.CourseUtilities;
import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.misc.Util;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

public class HomeActivity extends Activity{
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		prefs = getSharedPreferences("student-guide", MODE_PRIVATE);
		
		if(CourseUtilities.getCourseList() == null) {
			new ContentParser().execute(prefs.getString("rawCourseData", ""));
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
					URL url = new URL(Globals.courseListURL);
					rawData = Util.downloadContent(url);
				
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
				prefs.edit().putString("rawData", rawData).commit();
				prefs.edit().putBoolean("hasDownloaded", true).commit();
				CourseUtilities.setCourseList(courses);
			}	
		}
	}

}
