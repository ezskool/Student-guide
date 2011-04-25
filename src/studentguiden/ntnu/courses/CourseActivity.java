package studentguiden.ntnu.courses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import studentguiden.ntnu.main.R;
import studentguiden.ntnu.main.Util;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author Håkon Drolsum Røkenes
 * 
 */
public class CourseActivity extends Activity{
	private TextView courseName, courseDescription, courseCredit, courseLevel, courseGoals, courseDescriptionTitle, courseGoalsTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);

		Bundle extras = getIntent().getExtras();

		if(extras !=null){
			new ContentDownloader().execute(extras.getString("courseId"));
		}

		courseName = (TextView)findViewById(R.id.tv_coursename);
		courseDescription = (TextView)findViewById(R.id.tv_coursedescription);
		courseLevel = (TextView)findViewById(R.id.tv_courseLevel);
		courseCredit = (TextView)findViewById(R.id.tv_courseCredit);
		courseGoals = (TextView)findViewById(R.id.tv_courseGoals);
		courseDescriptionTitle = (TextView)findViewById(R.id.tv_courseDescription_title);
		courseGoalsTitle = (TextView)findViewById(R.id.tv_courseGoals_title);

	}

	/**
	 * Parses the course description, and updates user interface with the respective values
	 * @param rawCourseDescription the string containing the entire course description in JSON format
	 */
	public void populateCourseDescription(String rawCourseDescription) {
		Util.log("Populating course description..");

		try{
			JSONObject jsonCourseObject = new JSONObject(rawCourseDescription).getJSONObject("course"); 

			courseName.setText(jsonCourseObject.getString("name"));
			courseLevel.setText(jsonCourseObject.getString("studyLevelName"));
			courseCredit.setText(jsonCourseObject.getString("credit")+" SP");
			
			JSONArray infoArray = jsonCourseObject.getJSONArray("infoType");
			courseGoalsTitle.setText(R.string.course_goals);
			courseGoals.setText(infoArray.getJSONObject(0).getString("text"));
			courseDescriptionTitle.setText(R.string.course_description);
			courseDescription.setText(infoArray.getJSONObject(1).getString("text"));
			
			
		}catch(JSONException e) {
			Util.log("populating course description failed: JSONException");
			e.printStackTrace();
		}
	}

	
	private class ContentDownloader extends AsyncTask<String, Void, Integer>{

		private String textContent = "";
		private final int DOWNLOAD_SUCCESSFUL = 0;
		private final int DOWNLOAD_FAILED = 1;
		private final int DOWNLOAD_FAILED_INVALID_URL = 2;

		
		@Override
		protected Integer doInBackground(String... params) {
			try {
				URL url = new URL("http://www.ime.ntnu.no/api/course/"+params[0]);
				textContent = Util.downloadContent(url);
			}catch(MalformedURLException e) {
				Util.log("Content download failed: MalformedURLException");
				return DOWNLOAD_FAILED_INVALID_URL;
			}catch(IOException e) {
				Util.log("Content download failed: IOException");
				e.printStackTrace();
				return DOWNLOAD_FAILED;
			}
			return DOWNLOAD_SUCCESSFUL;
		}

		/**
		 * Called when doInBackground is finished, and updates the UI thread with course information if successful.
		 * If the download failed, a toast message will appear.
		 * @param Integer - indicating whether or not download was successful
		 */
		@Override
		protected void onPostExecute(Integer result) {
			if(result==DOWNLOAD_SUCCESSFUL) {
				CourseActivity.this.populateCourseDescription(textContent);
			}else if(result==DOWNLOAD_FAILED) {
				Util.displayToastMessage(getString(R.string.download_failed_toast),CourseActivity.this.getApplicationContext());
			}else if(result==DOWNLOAD_FAILED_INVALID_URL) {
				Util.displayToastMessage(getString(R.string.invalid_url_toast),CourseActivity.this.getApplicationContext());
			}
		}


	}

}
