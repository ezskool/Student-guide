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
import android.database.sqlite.SQLiteStatement;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


/**
 * Database helper class used to manage the creation and upgrading of the database, as well as insertion/removal of database entities.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "studassen.db";
	private static final int DATABASE_VERSION = 1;
	private Dao<Course, String> myCoursesDao;
	private Dao<Lecture, Integer> lectureDao;
	private SQLiteDatabase db;
	private Context context;
	private static final String CREATE_TABLE_COURSES =
		"create table IF NOT EXISTS courses (_id integer primary key, code text unique not null, name_no text not null, name_en text not null);";


	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}


	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try{
			db.execSQL(CREATE_TABLE_COURSES);
			//			TableUtils.createTableIfNotExists(connectionSource, MetaCourse.class);
			TableUtils.createTableIfNotExists(connectionSource, Lecture.class);
			TableUtils.createTableIfNotExists(connectionSource, Course.class);
			this.db = db;
		}catch (SQLException e) {
			Util.log("Unable to create database: SQLException");
			e.printStackTrace();
		} 

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			//TODO: drop table courses?
			TableUtils.dropTable(connectionSource, Lecture.class, true);
			TableUtils.dropTable(connectionSource, Course.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Util.log("Unable to upgrade database");
			throw new RuntimeException(e);
		}
	}

	public void openWritableConnection() {
		db = getWritableDatabase();
	}

	public void openReadableConnection() {
		db = getReadableDatabase();
	}



	/**
	 * Returns the Database Access Object (DAO) for the Lecture class. It will create it or just give the cached
	 * value.
	 */
	public Dao<Lecture, Integer> getLectureDao() throws SQLException {
		if(lectureDao == null) {
			lectureDao = getDao(Lecture.class);
		}
		return lectureDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for the Course class. It will create it or just give the cached value. 
	 * @return
	 * @throws SQLException
	 */
	public Dao<Course, String> getMyCourseDao() throws SQLException {
		if(myCoursesDao == null) {
			myCoursesDao = getDao(Course.class);
		}
		return myCoursesDao;
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
		Cursor c =  db.rawQuery("SELECT * FROM courses WHERE name_no LIKE '"+query+"%' "+
				"UNION SELECT * FROM courses WHERE name_en LIKE '"+query+"%' " +
				"UNION SELECT * FROM courses WHERE code LIKE '"+query+"%' LIMIT 0,10"
				, null);
		return c;
	}
	
	//TODO: preparedstatements
//	public List<Course> getAutocomplete(String query) {
//		List<Course> courses = new ArrayList<Course>();
//		Cursor c =  db.rawQuery("SELECT * FROM courses WHERE name_no LIKE '"+query+"%' "+
//				"UNION SELECT * FROM courses WHERE name_en LIKE '"+query+"%' " +
//				"UNION SELECT * FROM courses WHERE code LIKE '"+query+"%' LIMIT 0,10"
//				, null);
//
//		while(c.moveToNext()) {
//			courses.add(new Course(c.getString(0), c.getString(1), c.getString(2)));
//		}
//		c.close();
//
//		return courses;
//
//	}

	/**
	 * Removes a saved course 
	 * @param course
	 * @throws SQLException
	 */
	public void removeMyCourse(Course course) throws SQLException {
		removeLecturesFromCourse(course);
		getMyCourseDao().delete(course);
	}

	/**
	 * Retrieves all Lecture objects from the database, sorted by starting time so lectures get chronologically sorted in the list
	 * @return
	 * @throws SQLException
	 */
	public List<Lecture> getMyLectures() throws SQLException {
		QueryBuilder<Lecture, Integer> builder = getLectureDao().queryBuilder();
		builder.orderBy("start", true);

		return getLectureDao().query(builder.prepare());
	}

	/**
	 * Inserts lecture into database
	 * @param lecture
	 * @throws SQLException
	 */
	public void insertLecture(Lecture lecture) throws SQLException {
		getLectureDao().create(lecture);
	}

	/**
	 * Inserts a list of lectures into database
	 * @param lectureList
	 * @throws SQLException
	 */
	public void insertLectures(List<Lecture> lectureList) throws SQLException {
		for (Lecture lecture : lectureList) {
			getLectureDao().create(lecture);
		}
	}

	//TODO: fjerner kanskje ikke riktig. generated id blir vel annerledes? bør vel fjerne etter id. (deleteIds()
	public void removeLecture(Lecture lecture) throws SQLException {
		getLectureDao().delete(lecture);
		Util.log("Removing lecture at "+lecture.getStart()+" for course "+lecture.getCourseCode());
	}
	
	public void removeLecturesFromCourse(Course course) throws SQLException {
		for (Lecture lecture: course.getLectureList()) {
			removeLecture(lecture);
		}
	}

	public void insertMyCourse(Course course) throws SQLException {
		getMyCourseDao().create(course);
		Util.log("Inserting course "+course.getCode()+" into my courses table");
	}

	public List<Course> getMyCourses() throws SQLException {
		QueryBuilder<Course, String> builder = getMyCourseDao().queryBuilder();
		builder.orderBy("code", true);

		return getMyCourseDao().query(builder.prepare());
	}


	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		db = null;
		lectureDao = null;
		myCoursesDao = null;
	}
}