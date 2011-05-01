package studentguiden.ntnu.storage;

import java.util.ArrayList;

import studentguiden.ntnu.courses.CourseUtilities;
import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.MetaCourse;
import studentguiden.ntnu.misc.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class DataBaseUpdater extends AsyncTask<String, Void, Integer>{
	private final int DB_UPDATE_SUCCESS = 0;
	private final int DB_UPDATE_FAILURE = 1;

	private ArrayList<MetaCourse> courseList;
	private SharedPreferences prefs;
	private Context context;

	public DataBaseUpdater(ArrayList<MetaCourse> courses, SharedPreferences prefs, Context context) {
		this.courseList = courses;
		this.prefs = prefs;
		this.context = context;
	}

	@Override
	protected Integer doInBackground(String... params) {
		DataBaseAdapter database = new DataBaseAdapter(context);

		Util.log("inserting courses into db");
		database.open();
		for (MetaCourse course : courseList) {
			database.insertCourse(course.getCode(), course.getName());
		}
		database.close();
		CourseUtilities.updateCourseList(database);
		//TODO: ingen validering på at ting gikk riktig her. skrive om databaseadapter?
		return DB_UPDATE_SUCCESS;
	}


	@Override
	protected void onPostExecute(Integer result) {
		if(result==DB_UPDATE_SUCCESS) {
			prefs.edit().putBoolean("database_populated", true).commit();			
			Util.log("Database updated: "+prefs.getBoolean("database_populated", false));
		}else {
			Util.log("database update failed");
		}
		
	}


}
