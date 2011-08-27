package no.studassen.storage;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import no.studassen.entities.Course;
import no.studassen.entities.Lecture;
import no.studassen.misc.Util;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



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
		"create table IF NOT EXISTS my_courses (code text primary key, name_no text not null, name_en text not null, color_id integer not null);";
	private static final String CREATE_TABLE_LECTURES =
		"create table IF NOT EXISTS my_lectures (_id integer primary key, course_code text not null, day text not null, day_number integer not null, start text not null, end text not null, room text not null, room_code text, weeks text, activity_description text, color_id integer not null);";
	private static final String CREATE_TABLE_COLORS = 
		"create table IF NOT EXISTS colors (id integer primary key, color_code text not null);";


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
		db.execSQL(CREATE_TABLE_COLORS);
		this.db = db;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db,  int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS courses");
		db.execSQL("DROP TABLE IF EXISTS my_courses");
		db.execSQL("DROP TABLE IF EXISTS my_lectures");
		db.execSQL("DROP TABLE IF EXISTS colors");
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
	//TODO: update hvis lista finnes? db.replace?
	public void insertCourse(Course course) throws SQLException{
		ContentValues initialValues = new ContentValues();
		initialValues.put("code", course.getCode());
		initialValues.put("name_no", course.getName_no());
		initialValues.put("name_en", course.getName_en());
		db.replace("courses", null, initialValues);
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

	/**
	 * Retrieves the cursor when autocompleting the query. 
	 * @param query
	 * @return
	 */
	public Cursor getAutocompleteCursor(String query) {	
		Cursor c =  db.rawQuery("SELECT * FROM courses WHERE name_no LIKE ? "+
				"UNION SELECT * FROM courses WHERE name_en LIKE ? " +
				"UNION SELECT * FROM courses WHERE code LIKE ? LIMIT 0,100"
				, new String[] {"%"+query+"%", "%"+query+"%", query+"%" });
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
	//TODO: fjern farge fra table colors
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
	//TODO: add color: , c.getString(9)
	public List<Lecture> getMyLectures() throws SQLException {
		List<Lecture> lectures = new ArrayList<Lecture>();
		Cursor c = getMyLecturesCursor();
		while(c.moveToNext()) {
			Util.log("fetching lectures. at index: "+c.getPosition()+". total lectures: "+c.getCount());

			lectures.add(new Lecture(c.getString(0), c.getString(1), c.getInt(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7), c.getString(8), c.getString(9)));
		}
		c.close();

		Util.log("Retrieved "+lectures.size()+" lectures from db");
		return lectures;
	}

	public Cursor getMyLecturesCursor() {
		return db.rawQuery("SELECT L.course_code, L.day, L.day_number, L.start, L.end, L.room, L.room_code, L.weeks, L.activity_description, C.color_code " +
				"FROM my_lectures L INNER JOIN colors C ON L.color_id=C.id ORDER BY L.start", null);
	}

	/**
	 * Inserts lecture into database
	 * @param lecture
	 * @throws SQLException
	 */
	public void insertLecture(Lecture lecture) throws SQLException {
		Util.log("Inserting lecture for course: "+lecture.getCourseCode());
		
		db.execSQL("INSERT INTO my_lectures (course_code, day, day_number, start, end, room, room_code, weeks, activity_description, color_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT color_id FROM my_courses WHERE code=?))", 
				new String[] {lecture.getCourseCode(), lecture.getDay(), Integer.toString(lecture.getDayNumber()), lecture.getStart(), lecture.getEnd(),
				lecture.getRoom(), lecture.getRoomCode(), lecture.getWeeks(), lecture.getActivityDescription(), lecture.getCourseCode() });
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

	/**
	 * Inserts the course as my course, and its corresponding lectures. A free color is assigned to the course, and its lectures.
	 * @param course
	 * @throws SQLException
	 */
	public void insertMyCourse(Course course) throws SQLException {
		db.execSQL("INSERT INTO my_courses VALUES (?, ?, ?, (SELECT id FROM colors WHERE id NOT IN (SELECT color_id FROM my_courses) LIMIT 0,1))", 
				new String[] {course.getCode(), course.getName_no(), course.getName_en()} );
		insertLectures(course.getLectureList());

		Util.log("Inserting course "+course.getCode()+" into my_courses table");
	}
	
	

	public List<Course> getMyCourses() throws SQLException {
		List<Course> myCourses = new ArrayList<Course>();
		Cursor c = db.query("my_courses", new String[] {
				"code",
				"name_no",
				"name_en",
				"color_id"
		}, null, null, null, null, "code");
		Util.log("cursor count my_courses: "+c.getCount());
		while(c.moveToNext()) {
			Course course = new Course(c.getString(0), c.getString(1), c.getString(2));
			course.setColor(getColorById(Integer.toString(c.getInt(3))));
			myCourses.add(course);
		}

		c.close();
		return myCourses;
	}

	public String getCourseColor(String code) {
		Cursor c = db.rawQuery("SELECT color_code FROM colors WHERE id=(SELECT color_id FROM my_courses WHERE code=?)", new String[] {code});
		String colorCode = "";
		if(c.moveToFirst()) {
			colorCode = c.getString(0);
		}
		c.close();
		return colorCode;
	}
	
	public String getColorById(String id) {
		Cursor c = db.rawQuery("SELECT color_code FROM colors WHERE id=?", new String[] {id});
		c.moveToFirst();
		String colorCode = c.getString(0);
		c.close();

		return colorCode;
	}

	public long insertColor(String colorCode) throws SQLException {
		ContentValues initialValues = new ContentValues();
		initialValues.put("color_code", colorCode);
		return db.replace("colors",null, initialValues);
	}

	public void insertColorList(String[] colors) throws SQLException {
		db.beginTransaction();
		for (String colorCode: colors) {
			insertColor(colorCode);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public int getUnusedColorId() throws SQLException {
		Cursor c = db.rawQuery("SELECT id FROM colors WHERE id NOT IN (SELECT color_id FROM my_courses) LIMIT 0,1", null);
		
		c.moveToFirst();
		int colorId = c.getInt(0);
		c.close();
		Util.log("Retrieved unused color: "+colorId);
		return colorId;
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