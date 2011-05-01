package studentguiden.ntnu.misc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import studentguiden.ntnu.courses.CourseUtilities;
import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.MetaCourse;
import studentguiden.ntnu.main.Globals;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.social.SocialActivity;
import studentguiden.ntnu.storage.DataBaseAdapter;
import studentguiden.ntnu.storage.DataBaseUpdater;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.AvoidXfermode.Mode;
import android.os.AsyncTask;


public class CourseDownloader extends AsyncTask<String, Void, Integer>{
	private Context context;
	private final int PARSING_FAILED = 0;
	private final int DOWNLOAD_SUCCESFUL = 1;
	private final int DOWNLOAD_FAILED = 2;
	private final int DOWNLOAD_FAILED_MALFORMED_URL = 3;
	private String rawData;
	private DataBaseAdapter db;
	private SharedPreferences prefs;
	private ArrayList<MetaCourse> courseList;

	public CourseDownloader(Context context, SharedPreferences prefs) {
		this.context = context;
		this.prefs = prefs;
	}

	@Override
	protected Integer doInBackground(String... params) {
		try{
			db = new DataBaseAdapter(context);
			Util.log("is database populated? "+prefs.getBoolean("database_populated",false));
			if(!prefs.getBoolean("database_populated",false)) {
				
				Util.log("downloading course data");
				URL url = new URL(Globals.courseListURL);
				rawData = Util.downloadContent(url);

				Util.log("creating json objects");
				courseList = new ArrayList<MetaCourse>();
				JSONArray jsonCourseArray = new JSONObject(rawData).getJSONArray("course");
				int amountOfCourses = jsonCourseArray.length();
	
				for (int i = 0; i < amountOfCourses; i++) {
					JSONObject temp = jsonCourseArray.getJSONObject(i);
					courseList.add(new MetaCourse(temp.getString("code"), temp.getString("name")));
				}
				Util.log("course list object created");
				CourseUtilities.setCourseList(courseList);
				new DataBaseUpdater(courseList, prefs, context).execute();
			}else {
				Util.log("course data already downloaded. updating courselist from database");
				CourseUtilities.updateCourseList(db);
			}

			
	
		}catch(MalformedURLException e) {
			return DOWNLOAD_FAILED_MALFORMED_URL;
		}catch(IOException e) {
			return DOWNLOAD_FAILED;
		}catch(JSONException e) {
			return PARSING_FAILED;
		}		
		return DOWNLOAD_SUCCESFUL;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		if(result == DOWNLOAD_SUCCESFUL) {
			Util.log("Course content download successful.");
		}else if(result == DOWNLOAD_FAILED) {
			Globals.hasCalledCourseDownloader = false;
			Util.displayToastMessage(context.getString(R.string.download_failed_toast),context);
		}else {
			Util.log("parsing failed: coursedownloader");
			Globals.hasCalledCourseDownloader = false;
		}
	}
}
