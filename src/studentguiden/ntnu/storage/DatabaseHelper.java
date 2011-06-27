package studentguiden.ntnu.storage;


import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import studentguiden.ntnu.misc.CourseDownloader;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.storage.entities.Course;
import studentguiden.ntnu.storage.entities.Lecture;
import studentguiden.ntnu.storage.entities.MetaCourse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


/**
 * Database helper class used to manage the creation and upgrading of the database, as well as insertion/removal of database entities.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "studassen.db";
//	private static final String DATABASE_PATH = "/data/data/studentguiden/ntnu/storage/databases";
	private static final int DATABASE_VERSION = 1;
	private Dao<Course, String> savedCoursesDao;
	private Dao<Lecture, Integer> lectureDao;
	private Dao<MetaCourse, String> courseDao;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try{
			TableUtils.createTableIfNotExists(connectionSource, MetaCourse.class);
			TableUtils.createTableIfNotExists(connectionSource, Lecture.class);
			TableUtils.createTableIfNotExists(connectionSource, Course.class);
		}catch (SQLException e) {
			Util.log("Unable to create database");
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, MetaCourse.class, true);
			TableUtils.dropTable(connectionSource, Lecture.class, true);
			TableUtils.dropTable(connectionSource, Course.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Util.log("Unable to upgrade database");
			throw new RuntimeException(e);
		}
	}
	
//    /**
//     * Check if the database already exist to avoid re-copying the file each time you open the application.
//     * @return true if it exists, false if it doesn't
//     */
//	public void  checkDatabase() {
//		
//		try {
//			SQLiteDatabase db = SQLiteDatabase.openDatabase(DATABASE_PATH+DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
//		}catch(SQLiteException e) {
//			
//		}
//	}

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
	 * Returns the Database Access Object (DAO) for the Course class. It will create it or just give the cached
	 * value.
	 */
	public Dao<MetaCourse, String> getCourseDao() throws SQLException {
		if (courseDao == null) {
			courseDao = getDao(MetaCourse.class);
		}
		return courseDao;
	}
	/**
	 * Returns the Database Access Object (DAO) for the Course class. It will create it or just give the cached value. 
	 * @return
	 * @throws SQLException
	 */
	public Dao<Course, String> getSavedCoursesDao() throws SQLException {
		if(savedCoursesDao == null) {
			savedCoursesDao = getDao(Course.class);
		}
		return savedCoursesDao;
	}
	
	/**
	 * Inserts the course object into the database
	 * @param course
	 * @throws SQLException
	 */
	public void insertCourse(MetaCourse course) throws SQLException{
		getCourseDao().createOrUpdate(course);		
	}

	/**
	 * Inserts a list of courses into database
	 * @param courses
	 * @throws SQLException
	 */
	public void insertCourses(List<MetaCourse> courses) throws SQLException {
		for (MetaCourse course: courses) {
			getCourseDao().createOrUpdate(course);
		}
	}
	
	/**
	 * Retrieves all course objects from the database, ordered by code
	 * @return
	 * @throws SQLException
	 */
	public List<MetaCourse> getAllCourses() throws SQLException {
		QueryBuilder<MetaCourse, String> builder = getCourseDao().queryBuilder();
		builder.orderBy("code", true);
		return getCourseDao().query(builder.prepare());
	}

	/**
	 * Retrieves a given course object
	 * @param code - the course code
	 * @return
	 * @throws SQLException
	 */
	public MetaCourse getCourse(String code) throws SQLException {
		return getCourseDao().queryForId(code);
	}


	/**
	 * Removes a given course from the database
	 * @param course
	 * @throws SQLException
	 */
	public void removeCourse(MetaCourse course) throws SQLException {
		getCourseDao().delete(course);
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
	}
	
	public void insertSavedCourse(Course course) throws SQLException {
		getSavedCoursesDao().create(course);
	}
	
	public List<Course> getSavedCourses() throws SQLException {
		QueryBuilder<Course, String> builder = getSavedCoursesDao().queryBuilder();
		builder.orderBy("code", true);
		
		return getSavedCoursesDao().query(builder.prepare());
	}
	
	public void removeSavedCourse(Course course) throws SQLException {
		getSavedCoursesDao().delete(course);
	}
	
	
	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		courseDao = null;
		lectureDao = null;
		savedCoursesDao = null;
	}
}