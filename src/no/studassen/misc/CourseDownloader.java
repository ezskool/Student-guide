package no.studassen.misc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import no.studassen.entities.Course;
import no.studassen.storage.DataBaseUpdater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;


public class CourseDownloader extends AsyncTask<String, Void, Integer>{
	private final int PARSING_FAILED = 0;
	private final int DOWNLOAD_SUCCESFUL = 1;
	private final int DOWNLOAD_FAILED = 2;
	private final int DOWNLOAD_FAILED_MALFORMED_URL = 3;
	private final int IO_FAILED = 4;

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
		}catch(MalformedURLException e) {
			e.printStackTrace();
			return DOWNLOAD_FAILED_MALFORMED_URL;
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
			return DOWNLOAD_FAILED;
		}
		
		if(rawData == null) {
			Util.log("No internet connection. Reading course data from file.");
			try {
				rawData = Util.readFromFile("courses_metadata.txt", context);
			} catch (IOException e) {
				e.printStackTrace();
				return IO_FAILED;
			}
		}
		
		try {
			courseList = createCourseList(rawData);
		} catch(JSONException e) {
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
			Util.log("Course download failed");
			Globals.hasCalledCourseDownloader=false;
			Util.showNoConnectionDialog(context);
		}
	}


	private ArrayList<Course> createCourseList(String rawData) throws JSONException {
		ArrayList<Course> courses= new ArrayList<Course>();
		Util.log("creating json objects");
		JSONArray jsonCourseArray = new JSONObject(rawData).getJSONArray("course");
		int amountOfCourses = jsonCourseArray.length();

		for (int i = 0; i < amountOfCourses; i++) {
			JSONObject temp = jsonCourseArray.getJSONObject(i);
			Course course = getCourseObject(temp);
			if(course==null) {
				continue;
			}else {
				courses.add(course);
			}
		}
		cacheDate  = new JSONObject(rawData).getJSONObject("cache").getString("expires");
		return courses;
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
