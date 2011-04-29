package studentguiden.ntnu.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import studentguiden.ntnu.courses.CourseUtilities;
import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.Lecture;
import studentguiden.ntnu.entities.MetaCourse;
import studentguiden.ntnu.misc.JSONHelper;
import studentguiden.ntnu.misc.Util;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HomeActivity extends Activity{
	private SharedPreferences prefs;
	private ProgressDialog pd;
	private ContentParser cp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		prefs = getSharedPreferences("student-guide", MODE_PRIVATE);

		new TimetableUpdater().execute("http://www.ime.ntnu.no/api/schedule/tdt4242", "http://www.ime.ntnu.no/api/schedule/tdt4240","http://www.ime.ntnu.no/api/schedule/tdt4215");

		if(CourseUtilities.getCourseList() == null) {
			String raw = prefs.getString("rawCourseData", "");
			if(raw.equals("")) {
				prefs.edit().putBoolean("hasDownloaded", false).commit();
				cp = new ContentParser();
				cp.execute("");
			}else {
				prefs.edit().putString("rawData", raw).commit();
				prefs.edit().putBoolean("hasDownloaded", true).commit();
			}
		}
	}

	public void updateView(ArrayList<Course> courses) {
		LinearLayout lecture_row = (LinearLayout)findViewById(R.id.lecture_row);
		
		for (Course course : courses) {
			for (Lecture lecture : course.getLectureList()) {
				if(Util.isLectureToday(lecture)) {
					TextView  tv = new TextView(this);
					tv.setText(lecture.getStart()+" - "+lecture.getEnd()+"   "+course.getCode()+" - Room: " +lecture.getRoom());
					lecture_row.addView(tv);
					
					View v = new View(this);
					
				}
			}
		}
	}



	public void showProgressDialog() {
		pd = ProgressDialog.show(this, "", "Downloading content", true);
	}

	private class TimetableUpdater extends AsyncTask<String, Void, Integer> {
		private final int UPDATE_SUCCESS = 0;
		private ArrayList<Course> myCourses;

		@Override
		protected void onPreExecute() {
			myCourses = new ArrayList<Course>();
		}

		@Override
		protected Integer doInBackground(String... params) {

			for (int i = 0; i < params.length; i++) {
				try {
					URL url = new URL(params[i]);
					String rawLectureData = Util.downloadContent(url);

					Course temp = new Course();
					JSONHelper.updateScheduleData(temp, rawLectureData);
					myCourses.add(temp);

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
			return UPDATE_SUCCESS;
		}

		@Override
		protected void onPostExecute(Integer result) {
			updateView(myCourses);
		}

	}

	private class ContentParser extends AsyncTask<String, Void, Integer> {
		private final int PARSING_FAILED = 0;
		private final int PARSING_SUCCESFUL = 1;
		private final int DOWNLOAD_FAILED = 2;
		private ArrayList<MetaCourse> courses = new ArrayList<MetaCourse>();
		private String rawData;

		@Override
		protected void onPreExecute() {
			//			HomeActivity.this.showProgressDialog();

		}

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
					courses.add(new MetaCourse(temp.getString("code"), temp.getString("name")));
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
				//				HomeActivity.this.pd.cancel();
			}else {
				//				HomeActivity.this.pd.cancel();
				//TODO: feilmelding om man ikke har nett osv
			}
		}
	}
}
