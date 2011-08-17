package studentguiden.ntnu.misc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.storage.DataBaseUpdater;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;


public class CourseDownloader extends AsyncTask<String, Void, Integer>{
	private final int PARSING_FAILED = 0;
	private final int DOWNLOAD_SUCCESFUL = 1;
	private final int DOWNLOAD_FAILED = 2;
	private final int DOWNLOAD_FAILED_MALFORMED_URL = 3;

	private Context context;
	private String rawData;
	private ArrayList<Course> courseList;
	private String cacheDate = "";

	public CourseDownloader(Context context) {
		this.context = context;
	}

	@Override
	protected Integer doInBackground(String... params) {
		try{

			Util.log("downloading course data");
			rawData = Util.downloadContent(Globals.courseListURL);

			Util.log("creating json objects");
			courseList = new ArrayList<Course>();
			JSONArray jsonCourseArray = new JSONObject(rawData).getJSONArray("course");
			int amountOfCourses = jsonCourseArray.length();

			for (int i = 0; i < amountOfCourses; i++) {
				JSONObject temp = jsonCourseArray.getJSONObject(i);
				Course course = getCourseObject(temp);
				if(course==null) {
					continue;
				}else {
					courseList.add(course);
				}

			}
			cacheDate  = new JSONObject(rawData).getJSONObject("cache").getString("expires");
		}catch(MalformedURLException e) {
			e.printStackTrace();
			return DOWNLOAD_FAILED_MALFORMED_URL;
		}catch(IOException e) {
			e.printStackTrace();
			return DOWNLOAD_FAILED;
		}catch(JSONException e) {
			e.printStackTrace();
			return PARSING_FAILED;
		}		
		return DOWNLOAD_SUCCESFUL;
	}

	@Override
	protected void onPostExecute(Integer result) {
		if(result == DOWNLOAD_SUCCESFUL) {
			Util.log("Course content download successful. setting cachedate to "+cacheDate);
			SharedPreferences prefs = context.getSharedPreferences("studassen", Context.MODE_PRIVATE);
			prefs.edit().putString("CourseDataExpirationDate", cacheDate).commit();
			new DataBaseUpdater(courseList, context).execute();

		}else if(result == DOWNLOAD_FAILED || result == PARSING_FAILED) {
//			Util.displayToastMessage(context.getString(R.string.download_failed_toast),context);
			Util.log("Course download failed");
			Globals.hasCalledCourseDownloader=false;
//			new DatabaseHelper(context);
			Util.showNoConnectionDialog(context);
		}
	}

	private String getAttribute(JSONObject object, String attribute) throws JSONException {
		String temp = object.getString(attribute);
		if(temp!= null) {
			return temp;
		}
		return "";
	}

	private Course getCourseObject(JSONObject object) {
		Course course;
		try {
			course = new Course(object.getString("code"), getAttribute(object, "name"), getAttribute(object, "englishName"));
		} catch (Exception e) {
			return null;
		}
		return course;
	}
	

}
