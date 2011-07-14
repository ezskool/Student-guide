package studentguiden.ntnu.storage;

import java.util.ArrayList;
import java.util.List;

import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.storage.entities.Course;
import studentguiden.ntnu.storage.entities.Lecture;
import studentguiden.ntnu.storage.entities.MetaCourse;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DataBaseAdapter extends SQLiteOpenHelper {




	//		public static final String KEY_CODE = "code";
	//		public static final String KEY_NAME = "name";  
	private static final String TAG = "DBAdapter";
	private static final String DATABASE_NAME = "studassen";
	//		private static final String DATABASE_TABLE = "courses";
	private static final int DATABASE_VERSION = 1;
	private static final String MYCOURSES_TABLE = "mycourses";
	private static final String LECTURES_TABLE = "lectures";

	//		private static final String DIRECTORY = "data/datastudentguiden.ntnu.storage/databases/";
	//		private static final String PATH = DIRECTORY+DATABASE_NAME;

	private static final String CREATE_TABLE_COURSE =
		"create table IF NOT EXISTS courses (code text primary key unique, name_no text not null, name_en text not null);";

	private static final String CREATE_TABLE_MYCOURSES = 
		"create table IF NOT EXISTS "+MYCOURSES_TABLE+" (code text primary key, name_no text not null, name_en text not null);";

	private static final String CREATE_TABLE_LECTURES = 
		"create table IF NOT EXISTS "+LECTURES_TABLE+" (_id integer primary key autoincrement, code text not null, start text not null," +
		" end text not null, day text not null, dayNumber integer not null, weeks text not null, room text not null);";


	private SQLiteDatabase db;


	public DataBaseAdapter(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;

		db.execSQL(CREATE_TABLE_COURSE);
		db.execSQL(CREATE_TABLE_MYCOURSES);
		db.execSQL(CREATE_TABLE_LECTURES);
	}

	//TODO: fjerne alt på upgrade?
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Util.log("Upgrading database from version " + oldVersion 
				+ " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS titles");
		onCreate(db);
	}


	/**
	 * Opens the database connection
	 * @return
	 * @throws SQLException
	 */
	public void openReadWrite() throws SQLException {
		db = getWritableDatabase();
	}

	public void openReadOnly() throws SQLException {
		db = getReadableDatabase();
	}

	/**
	 * Closes the database connection
	 */
	public void close() {
		db.close();
	}


	public long insertCourse(String code, String name_no, String name_en) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("code", code);
		initialValues.put("name_no", name_no);
		initialValues.put("name_en", name_en);
		return db.insert("courses", null, initialValues);
	}

	//TODO: memory kan overloades hvis for mange fag insertes. hvor mange?
	/**
	 * Inserts a list of courses in a single transaction
	 * @param courseList
	 */
	public void insertCourseList(ArrayList<MetaCourse> courseList) {
		db.beginTransaction();
		db.setLockingEnabled(true);
		for (MetaCourse course : courseList) {
			insertCourse(course.getCode(), course.getName_no(), course.getName_en());
		}
		db.setLockingEnabled(false);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	//	public void insertCourseList(ArrayList<MetaCourse> courseList) {
	//		InsertHelper ih = new InsertHelper(db, "courses");
	//		
	//		final int codeIndex =ih.getColumnIndex("code"); 
	//		final int nameNoIndex = ih.getColumnIndex("name_no");
	//		final int nameEnIndex = ih.getColumnIndex("name_en");
	//		db.setLockingEnabled(true);
	//		for (MetaCourse course : courseList) {
	//			ih.prepareForInsert();
	//			ih.bind(codeIndex, course.getCode());
	//			ih.bind(nameNoIndex, course.getName_no());
	//			ih.bind(nameEnIndex, course.getName_en());
	//			ih.execute();
	//		}
	//		db.setLockingEnabled(false);
	//	}

	public long insertMyCourse(Course course) {
		return insertCourse(course.getCode(), course.getName_no(), course.getName_en());
	}

	public long insertMyCourse(String code, String name_no, String name_en) {
		Util.log("inserting course to my courses: "+code);
		ContentValues initialValues = new ContentValues();
		initialValues.put("code", code);
		initialValues.put("name", name_no);
		initialValues.put("name_en", name_en);
		return db.insert(MYCOURSES_TABLE,null, initialValues);
	}

	public void insertLectures(List<Lecture> lectureList) {
		int count = 0;
		db.beginTransaction();
		for (Lecture lecture : lectureList) {
			insertLecture(lecture);
			count++;
			if(count >100) {
				db.setTransactionSuccessful();
				db.endTransaction();
				db.beginTransaction();
			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public long insertLecture(Lecture lecture) {
		return insertLecture(lecture.getCourseCode(), lecture.getStart(), lecture.getEnd(), lecture.getDay(), lecture.getDayNumber(), lecture.getWeeksText(), lecture.getRoom());
	}

	public long insertLecture(String code, String start, String end, String day, int dayNumber, String week, String room) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("code", code);
		initialValues.put("start", start);
		initialValues.put("end", end);
		initialValues.put("day", day);
		initialValues.put("dayNumber", dayNumber);
		initialValues.put("weeks", week);
		initialValues.put("room", room);
		return db.insert(LECTURES_TABLE, null, initialValues);
	}

	//---deletes a particular title---
	public boolean deleteCourse(String rowId) {
		return db.delete("courses", "code" + 
				"=" + rowId, null) > 0;
	}

	public List<MetaCourse> getAllCourses() {
		List<MetaCourse> courses = new ArrayList<MetaCourse>();
		Cursor c = db.query("courses", new String[] {
				"code",
				"name_no",
		"name_en"},
		null,
		null,
		"code",
		null,
		null);

		while(c.moveToNext()) {
			courses.add(new MetaCourse(c.getString(0), c.getString(1), c.getString(2)));
		}
		c.close();

		return courses;
	}

	public List<Lecture> getMyLectures() {
		List<Lecture> myLectures = new ArrayList<Lecture>();

		Cursor c = db.query(LECTURES_TABLE, new String[] {
				"_id",
				"code",
				"start",
				"end",
				"day",
				"dayNumber",
				"weeks",
				"room"
		}, null, null, null, null, "start");

		while(!c.isAfterLast()){
			myLectures.add(new Lecture(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5), c.getString(6)));
		}

		return myLectures;
	}

	public List<Course> getMyCourses() {
		List<Course> myCourses = new ArrayList<Course>();
		Cursor c = db.query(MYCOURSES_TABLE, new String[] {
				"code",
				"name_no",
				"name_en"
		}, null, null, null, null, "code");

		while(c.moveToNext()) {
			myCourses.add(new Course(c.getString(0), c.getString(1), c.getString(2)));
		}
		c.close();
		return myCourses;
	}


	/**
	 * Retrieves a particular course
	 * @param code
	 * @return
	 * @throws SQLException
	 */
	public Cursor getCourse(String code) throws SQLException {
		Cursor mCursor =
			db.query(true, "courses", new String[] {
					"code", 
					"name_no",
					"name_en"
			}, 
			"code" + "=" + code, 
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

	/**
	 * Updates a course
	 * @param code
	 * @param name_no
	 * @param name_en
	 * @return
	 */
	public boolean updateCourse(String code, String name_no, String name_en) {
		ContentValues args = new ContentValues();
		args.put("code", code);
		args.put("name_no", name_no);
		args.put("name_en", name_en);
		return db.update("courses", args, 
				"code" + "=" + code, null) > 0;
	}
}