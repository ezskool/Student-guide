package studentguiden.ntnu.storage;

import java.sql.SQLException;
import java.util.ArrayList;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.misc.Util;
import android.content.Context;
import android.os.AsyncTask;

public class DataBaseUpdater extends AsyncTask<String, Void, Integer>{
	private final int DB_UPDATE_SUCCESS = 1;
	private final int DB_UPDATE_FAILED = 0;

	private ArrayList<Course> courseList;
	private Context context;

	public DataBaseUpdater(ArrayList<Course> courses, Context context) {
		this.courseList = courses;
		this.context = context;
	}

	@Override
	protected Integer doInBackground(String... params) {
		DatabaseHelper db = new DatabaseHelper(context);
		Util.log("inserting courses into database");
		db.openWritableConnection();
		try {
			db.insertCourseList(courseList);
		} catch (SQLException e) {
			Util.log("Insertion of course list failed");
			e.printStackTrace();
			return DB_UPDATE_FAILED;
		}

		db.close();
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
