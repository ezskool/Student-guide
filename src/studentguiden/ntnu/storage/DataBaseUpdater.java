package studentguiden.ntnu.storage;

import java.util.ArrayList;

import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.storage.entities.MetaCourse;
import android.content.Context;
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
		DataBaseAdapter db = new DataBaseAdapter(context);
		Util.log("inserting courses into database");
		db.openReadWrite();
		db.insertCourseList(courseList);

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
