package studentguiden.ntnu.storage;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.Lecture;
import studentguiden.ntnu.misc.Util;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;



/**
 * Database helper class used to manage the creation and upgrading of the database, as well as insertion/removal of database entities.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "studassen.db";
	private static final int DATABASE_VERSION = 1;
	private SQLiteDatabase db;
	private Context context;
	private static final String CREATE_TABLE_COURSES =
		"create table IF NOT EXISTS courses (_id integer primary key, code text unique not null, name_no text not null, name_en text not null);";
	//TODO: foreign key courses table
	private static final String CREATE_TABLE_MYCOURSES =
		"create table IF NOT EXISTS my_courses (code text primary key, name_no text not null, name_en text not null, color text not null);";
	private static final String CREATE_TABLE_LECTURES =
		"create table IF NOT EXISTS my_lectures (id_ integer primary key, course_code text not null, day text not null, day_number integer not null, start text not null, end text not null, room text not null, room_code text, weeks text, activity_description text, color text not null);";


	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	public boolean isOpen() {
		return db!=null ? db.isOpen() : false;
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_COURSES);
		db.execSQL(CREATE_TABLE_MYCOURSES);
		db.execSQL(CREATE_TABLE_LECTURES);
		this.db = db;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db,  int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS courses");
		db.execSQL("DROP TABLE IF EXISTS my_courses");
		db.execSQL("DROP TABLE IF EXISTS my_lectures");
		onCreate(db);
	}

	public void openWritableConnection() {
		db = getWritableDatabase();
	}

	public void openReadableConnection() {
		db = getReadableDatabase();
	}


	/**
	 * Inserts the course object into the database
	 * @param course
	 * @throws SQLException
	 */
	public void insertCourse(Course course) throws SQLException{
		ContentValues initialValues = new ContentValues();
		initialValues.put("code", course.getCode());
		initialValues.put("name_no", course.getName_no());
		initialValues.put("name_en", course.getName_en());
		db.insert("courses", null, initialValues);
	}

	/**
	 * Inserts a list of courses into database, in one single transaction. The database is locked onto current thread to improve insertion time. 
	 * The insertion is done in plain SQL, in order to have two tables for the same object (Course)
	 * @param courses
	 * @throws SQLException
	 */
	public void insertCourseList(List<Course> courses) throws SQLException {
		db.beginTransaction();
		db.setLockingEnabled(true);
		for (Course course: courses) {
			insertCourse(course);
		}
		db.setLockingEnabled(false);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * Retrieves all course objects from the database, ordered by code
	 * @return
	 * @throws SQLException
	 */
	public List<Course> getAllCourses() {
		List<Course> courses = new ArrayList<Course>();
		Cursor c = getAllCoursesCursor();

		while(c.moveToNext()) {
			courses.add(new Course(c.getString(0), c.getString(1), c.getString(2)));
		}
		c.close();

		return courses;
	}
	/**
	 * Retrieves the cursor for all the courses in the database, ordered by "code"
	 * @return
	 */
	public Cursor getAllCoursesCursor() {
		return db.query("courses", 
				new String[] {"code","name_no","name_en"},
				null,
				null,
				"code",
				null,
				null);
	}
	//TODO: preparedstatements?
	public Cursor getAutocompleteCursor(String query) {	
		Cursor c =  db.rawQuery("SELECT * FROM courses WHERE name_no LIKE '%"+query+"%' "+
				"UNION SELECT * FROM courses WHERE name_en LIKE '%"+query+"%' " +
				"UNION SELECT * FROM courses WHERE code LIKE '"+query+"%' LIMIT 0,100"
				, null);
		return c;
	}

	public List<Course> getAutocompleteList(String query) {
		Cursor c = getAutocompleteCursor(query);
		List<Course> courses = new ArrayList<Course>();

		while(c.moveToNext()) {
			courses.add(new Course(c.getString(1), c.getString(2), c.getString(3)));
		}
		c.close();

		return courses;
	}

	/**
	 * Removes a saved course and its corresponding lectures
	 * @param course
	 * @throws SQLException
	 */
	public void removeMyCourse(Course course) throws SQLException {
		//		removeLecturesFromCourse(course);
		db.delete("my_courses", "code=?", new String[] { course.getCode() });
		db.delete("my_lectures", "course_code=?", new String[] { course.getCode() });
		Util.log("Deleted course "+course.getCode());
	}

	/**
	 * Retrieves all Lecture objects from the database, sorted by starting time so lectures get chronologically sorted in the list
	 * @return
	 * @throws SQLException
	 */
	public List<Lecture> getMyLectures() throws SQLException {
		List<Lecture> lectures = new ArrayList<Lecture>();
		Cursor c = getMyLecturesCursor();
		while(c.moveToNext()) {
			Util.log("fetching lectures. at index: "+c.getPosition()+". total lectures: "+c.getCount());
			
			lectures.add(new Lecture(c.getString(0), c.getString(3), c.getString(4), c.getString(1), c.getInt(2), c.getString(7), c.getString(5), c.getString(6), c.getString(8), c.getString(9)));
		}
		c.close();

		Util.log("Retrieved "+lectures.size()+" lectures from db");
		return lectures;
	}

	public Cursor getMyLecturesCursor() {
		return db.query("my_lectures", 
				new String[] {"course_code", "day", "day_number", "start", "end", "room", "room_code", "weeks", "activity_description", "color"},
				null,
				null,
				null,
				null,
				"start");
	}

	/**
	 * Inserts lecture into database
	 * @param lecture
	 * @throws SQLException
	 */
	public long insertLecture(Lecture lecture) throws SQLException {
		ContentValues initialValues = new ContentValues();
		initialValues.put("course_code", lecture.getCourseCode());
		initialValues.put("start", lecture.getStart());
		initialValues.put("end", lecture.getEnd());
		initialValues.put("day", lecture.getDay());
		initialValues.put("day_number", lecture.getDayNumber());
		initialValues.put("weeks", lecture.getWeeks());
		initialValues.put("room", lecture.getRoom());
		initialValues.put("room_code", lecture.getRoomCode());
		initialValues.put("color", lecture.getColor());
		return db.insert("my_lectures", null, initialValues);
	}

	/**
	 * Inserts a list of lectures into database
	 * @param lectureList
	 * @throws SQLException
	 */
	public void insertLectures(List<Lecture> lectureList) throws SQLException {
		db.beginTransaction();
		for (Lecture lecture : lectureList) {
			insertLecture(lecture);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	//TODO: fjerner kanskje ikke riktig. generated id blir vel annerledes? bør vel fjerne etter id. (deleteIds()
	public void removeLecture(Lecture lecture) throws SQLException {
		db.delete("my_lectures", "course_code='"+lecture.getCourseCode()+"'", null);
		Util.log("Removing lecture at "+lecture.getStart()+" for course "+lecture.getCourseCode());
	}

	public void removeLecturesFromCourse(Course course) throws SQLException {
		db.beginTransaction();
		for (Lecture lecture: course.getLectureList()) {
			removeLecture(lecture);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public long insertMyCourse(Course course) throws SQLException {
		ContentValues initialValues = new ContentValues();
		initialValues.put("code", course.getCode());
		initialValues.put("name_no", course.getName_no());
		initialValues.put("name_en", course.getName_en());
		initialValues.put("color", course.getColor());

		Util.log("Inserting course "+course.getCode()+" into my_courses table");
		return db.insert("my_courses",null, initialValues);
	}

	public List<Course> getMyCourses() throws SQLException {
		List<Course> myCourses = new ArrayList<Course>();
		Cursor c = db.query("my_courses", new String[] {
				"code",
				"name_no",
				"name_en",
				"color"
		}, null, null, null, null, "code");
		Util.log("cursor count my_courses: "+c.getCount());
		while(c.moveToNext()) {
			myCourses.add(new Course(c.getString(0), c.getString(1), c.getString(2), c.getString(3)));
		}
		c.close();
		return myCourses;
	}


	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		db = null;
	}
}