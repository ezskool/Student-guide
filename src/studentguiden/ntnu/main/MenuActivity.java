package studentguiden.ntnu.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import studentguiden.ntnu.courses.FindCourseActivity;
import studentguiden.ntnu.timetable.TimetableActivity;
import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
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
	private boolean hasDownloadedCourses;
	private String[] courseList, courseNameList;
	private CourseListInitializer initializer;
	private ProgressDialog pd;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		btn_courses = (Button)findViewById(R.id.btn_courses);
		btn_courses.setOnClickListener(this);
		
		btn_timetable = (Button)findViewById(R.id.btn_timetable);
		btn_timetable.setOnClickListener(this);

		if(!hasDownloadedCourses) {
			initializer = new CourseListInitializer();
			initializer.execute("");
		}
	}

	public void startCourseListActivity() {
		Intent intent = new Intent(MenuActivity.this, FindCourseActivity.class);
		intent.putExtra("courseList", courseList);
		intent.putExtra("courseNameList", courseNameList);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if(v==btn_courses) {
			if(hasDownloadedCourses) {
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

	public void setCourseList(String[] courses) {
		this.courseList = courses;
	}
	
	public void setCourseNames(String[] names) {
		this.courseNameList = names;
	}

	public void setDownloadComplete() {
		hasDownloadedCourses = true;
		if(pd != null) {
			if(pd.isShowing()) {
				startCourseListActivity();
				pd.dismiss();
			}
		}
	}

	private class CourseListInitializer extends AsyncTask<String, Integer, Integer> {
		private final int DOWNLOAD_SUCCESSFUL = 0;
		private final int DOWNLOAD_FAILED = 1;
		private final int DOWNLOAD_FAILED_INVALID_URL = 2;
		private String rawCourseOverview = "";
		private JSONArray jsonCourseArray;
		protected String[] courseIds, courseNames;

		@Override
		protected Integer doInBackground(String... params) {
			try {
				URL url = new URL("http://www.ime.ntnu.no/api/course/-");
				rawCourseOverview = Util.downloadContent(url);
			}catch(MalformedURLException e){
				e.printStackTrace();
				return DOWNLOAD_FAILED_INVALID_URL;
			}catch (IOException e) {
				e.printStackTrace();
				return DOWNLOAD_FAILED;
			}

			try {
				jsonCourseArray = new JSONObject(rawCourseOverview).getJSONArray("course");
				int amountOfCourses = jsonCourseArray.length();
				courseIds = new String[amountOfCourses];
				courseNames = new String[amountOfCourses];
				
				for (int i = 0; i < amountOfCourses; i++) {
					JSONObject temp = jsonCourseArray.getJSONObject(i);
					courseIds[i] = temp.getString("code");
					courseNames[i] = temp.getString("name");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return DOWNLOAD_SUCCESSFUL;			
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == DOWNLOAD_SUCCESSFUL) {
				Util.log("Course list download was successful");
				MenuActivity.this.setCourseList(courseIds);
				MenuActivity.this.setCourseNames(courseNames);
				MenuActivity.this.setDownloadComplete();
			}	
		}
	}
}