package studentguiden.ntnu.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import studentguiden.ntnu.courses.Course;
import studentguiden.ntnu.courses.FindCourseActivity;
import studentguiden.ntnu.timetable.TimetableActivity;
import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author Håkon Drolsum Røkenes
 * 
 */

public class MenuActivity extends Activity implements OnClickListener{

	private Button btn_timetable, btn_courses;
//	private ArrayList<Course> courseList;
	private ProgressDialog pd;
	private String rawCourseData;
	private SharedPreferences prefs;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		prefs = getSharedPreferences("student-guide", MODE_PRIVATE);

		btn_courses = (Button)findViewById(R.id.btn_courses);
		btn_courses.setOnClickListener(this);
		
		btn_timetable = (Button)findViewById(R.id.btn_timetable);
		btn_timetable.setOnClickListener(this);

		if((rawCourseData = prefs.getString("rawCourseData", null)) == null) {
			prefs.edit().putBoolean("hasDownloaded", false).commit();
			new ContentDownloader().execute("http://www.ime.ntnu.no/api/course/-");
		}else {
			Util.log("Course data already downloaded: skipping");
		}
	}

	public void startCourseListActivity() {
		Intent intent = new Intent(MenuActivity.this, FindCourseActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if(v==btn_courses) {
			if(prefs.getBoolean("hasDownloaded",true)) {
				startCourseListActivity();
			}else{
				pd = ProgressDialog.show(this, "", "Downloading content", true);
				pd.setCancelable(true);
				pd.setCanceledOnTouchOutside(true);
			}
		}else if(v==btn_timetable) {
			Intent intent = new Intent(MenuActivity.this, TimetableActivity.class);
			startActivity(intent);
		}
	}

	public void setRawCourseData(String data) {
		this.rawCourseData = data;
		prefs.edit().putString("rawCourseData", data).commit();
		prefs.edit().putBoolean("hasDownloaded", true).commit();
		if(pd != null) {
			if(pd.isShowing()) {
				startCourseListActivity();
				pd.dismiss();
			}
		}
	}



	private class ContentDownloader extends AsyncTask<String, Integer, Integer> {
		private final int DOWNLOAD_SUCCESSFUL = 0;
		private final int DOWNLOAD_FAILED = 1;
		private final int DOWNLOAD_FAILED_INVALID_URL = 2;
		private String rawData = "";
		
		@Override
		protected Integer doInBackground(String... params) {
			try {
				URL url = new URL(params[0]);
				rawData = Util.downloadContent(url);
			}catch(MalformedURLException e){
				e.printStackTrace();
				return DOWNLOAD_FAILED_INVALID_URL;
			}catch (IOException e) {
				e.printStackTrace();
				return DOWNLOAD_FAILED;
			}

			
			return DOWNLOAD_SUCCESSFUL;			
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == DOWNLOAD_SUCCESSFUL) {
				Util.log("Course list download was successful");
				MenuActivity.this.setRawCourseData(rawData);
			}	
		}
	}
}