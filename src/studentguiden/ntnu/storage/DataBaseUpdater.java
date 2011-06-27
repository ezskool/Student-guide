package studentguiden.ntnu.storage;

import java.sql.SQLException;
import java.util.ArrayList;

import studentguiden.ntnu.courses.CourseUtilities;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.storage.entities.Course;
import studentguiden.ntnu.storage.entities.MetaCourse;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class DataBaseUpdater extends AsyncTask<String, Void, Integer>{
	private final int DB_UPDATE_SUCCESS = 1;
	private final int DB_UPDATE_FAILED = 0;

	private ArrayList<MetaCourse> courseList;
	private Context context;

	public DataBaseUpdater(ArrayList<MetaCourse> courses, Context context) {
		this.courseList = courses;
		this.context = context;
	}

	@Override
	protected Integer doInBackground(String... params) {
		DatabaseHelper database = new DatabaseHelper(context);
		Util.log("inserting courses into database");

		for (MetaCourse course : courseList) {
			try {
				database.insertCourse(course);
			} catch (SQLException e) {
				e.printStackTrace();
				Util.log("Database update failed while inserting course "+course.getCode());
				return DB_UPDATE_FAILED;
			}
		}

		database.close();
		return DB_UPDATE_SUCCESS;
	}

	@Override
	protected void onPostExecute(Integer result) {
		if(result==DB_UPDATE_SUCCESS) {
			Util.log("Database updated");
		}else if(result == DB_UPDATE_FAILED){
			Util.log("database update failed");
		}
	}
}
