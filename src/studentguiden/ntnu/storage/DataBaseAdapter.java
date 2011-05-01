package studentguiden.ntnu.storage;

import studentguiden.ntnu.misc.Util;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_CODE = "code";
	public static final String KEY_NAME = "name";  
	private static final String TAG = "DBAdapter";
	private static final String DATABASE_NAME = "studentguiden";
	private static final String DATABASE_TABLE = "courses";
	private static final int DATABASE_VERSION = 1;
	private static final String MYCOURSES_TABLE = "mycourses";
	private static final String LECTURES_TABLE = "lectures";

	private static final String CREATE_TABLE_COURSE =
		"create table IF NOT EXISTS "+DATABASE_TABLE+" (_id integer primary key autoincrement, "+KEY_CODE+" text not null, "+KEY_NAME+" text not null);";
	
	private static final String CREATE_TABLE_MYCOURSES = 
		"create table IF NOT EXISTS "+MYCOURSES_TABLE+" (_id integer primary key );";
	
	private static final String CREATE_TABLE_LECTURES = 
		"create table IF NOT EXISTS "+LECTURES_TABLE+" (_id integer primary key autoincrement, day text not null, start text not null, " +
				"end text not null, room text not null);";

	private final Context context; 

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DataBaseAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE_COURSE);
			db.execSQL(CREATE_TABLE_MYCOURSES);
			db.execSQL(CREATE_TABLE_LECTURES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Util.log("Upgrading database from version " + oldVersion 
					+ " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS titles");
			onCreate(db);
		}
	}    

	/**
	 * Opens the database connection
	 * @return
	 * @throws SQLException
	 */
	public DataBaseAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Closes the database connection
	 */
	public void close() {
		DBHelper.close();
	}

	public long insertCourse(String code, String name) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CODE, code);
		initialValues.put(KEY_NAME, name);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public long insertMyCourse(int id) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("id", id);
		return db.insert(MYCOURSES_TABLE,null, initialValues);
	}


	//---deletes a particular title---
	public boolean deleteCourse(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + 
				"=" + rowId, null) > 0;
	}

	public Cursor getAllCourses() {
		return db.query(DATABASE_TABLE, new String[] {
				KEY_ROWID,
				KEY_CODE,
				KEY_NAME},
				null,
				null,
				"code",
				null,
				null);
	}
	
	public Cursor getMyCourses() {
		return db.query(MYCOURSES_TABLE, new String[] {
				KEY_ROWID
		}, null, null, null, null, null);
	}


	//---retrieves a particular title---
	public Cursor getCourse(long rowId) throws SQLException {
		Cursor mCursor =
			db.query(true, DATABASE_TABLE, new String[] {
					KEY_ROWID,
					KEY_CODE, 
					KEY_NAME
			}, 
			KEY_ROWID + "=" + rowId, 
			null,
			null, 
			null, 
			null, 
			null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	//---updates a title---
	public boolean updateCourse(long rowId, String code, String name) {
		ContentValues args = new ContentValues();
		args.put(KEY_CODE, code);
		args.put(KEY_NAME, name);
		return db.update(DATABASE_TABLE, args, 
				KEY_ROWID + "=" + rowId, null) > 0;
	}
}