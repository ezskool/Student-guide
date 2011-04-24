package studentguiden.ntnu.courses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
	private TextView courseName, courseDescription, courseCredit, courseLevel, courseGoals;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);

		Bundle extras = getIntent().getExtras();

		if(extras !=null)
		{
			new ContentDownloader().execute(extras.getString("courseId"));
		}

		courseName = (TextView)findViewById(R.id.tv_coursename);
		courseDescription = (TextView)findViewById(R.id.tv_coursedescription);
		courseLevel = (TextView)findViewById(R.id.tv_courseLevel);
		courseCredit = (TextView)findViewById(R.id.tv_courseCredit);
		courseGoals = (TextView)findViewById(R.id.tv_courseGoals);

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
			courseGoals.setText(infoArray.getJSONObject(0).getString("text"));
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

		
		@Override
		protected Integer doInBackground(String... params) {
			try {
				URL url = new URL("http://www.ime.ntnu.no/api/course/"+params[0]);
				Util.log("Downloading content from url: "+url.toString());
				InputStream in = url.openStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
				textContent = read(reader);
				in.close();
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
			}
		}

		/**
		 * Reads all content from a Reader object. Used for reading the content of an URL
		 * @param reader
		 * @return the string which was read
		 * @throws IOException
		 */
		private String read(Reader reader) throws IOException {
			StringBuilder sb = new StringBuilder();
			int cp;
			while ((cp = reader.read()) != -1) {
				sb.append((char) cp);
			}
			return sb.toString();
		}
	}

}
