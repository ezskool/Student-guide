//package studentguiden.ntnu.storage;
//
//
//import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.sql.SQLException;
//import java.util.List;
//
//import studentguiden.ntnu.misc.Util;
//import studentguiden.ntnu.storage.entities.Course;
//import studentguiden.ntnu.storage.entities.Lecture;
//import studentguiden.ntnu.storage.entities.MetaCourse;
//import android.R;
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
//import com.j256.ormlite.dao.Dao;
//import com.j256.ormlite.stmt.QueryBuilder;
//import com.j256.ormlite.support.ConnectionSource;
//import com.j256.ormlite.table.TableUtils;
//
//
///**
// * Database helper class used to manage the creation and upgrading of the database, as well as insertion/removal of database entities.
// */
//public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
//
//	private static final String DATABASE_NAME = "studassen.db";
//	//	private static final String DATABASE_PATH = "/data/data/studentguiden/ntnu/storage/databases";
//	private static final int DATABASE_VERSION = 1;
//	private Dao<Course, String> savedCoursesDao;
//	private Dao<Lecture, Integer> lectureDao;
//	private Dao<MetaCourse, String> courseDao;
//	private SQLiteDatabase db;
//	private Context context;
//
//	public DatabaseHelper(Context context) {
//		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//		this.context = context;
////		copyDataBase();
//	}
//
//
//	@Override
//	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
//		try{
//		
//			TableUtils.createTableIfNotExists(connectionSource, MetaCourse.class);
//			TableUtils.createTableIfNotExists(connectionSource, Lecture.class);
//			TableUtils.createTableIfNotExists(connectionSource, Course.class);
//			this.db = db;
//		}catch (SQLException e) {
//			Util.log("Unable to create database: SQLException");
//			e.printStackTrace();
//		} 
//	
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//		try {
//			TableUtils.dropTable(connectionSource, MetaCourse.class, true);
//			TableUtils.dropTable(connectionSource, Lecture.class, true);
//			TableUtils.dropTable(connectionSource, Course.class, true);
//			onCreate(db, connectionSource);
//		} catch (SQLException e) {
//			Util.log("Unable to upgrade database");
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * Copies database from the assets folder. This is done due to the time it takes to create all the courses. Only metadata for each course is retrieved. 
//	 * If the database has already been imported, nothing will be done.
//	 * @throws SQLException
//	 * @throws IOException
//	 */
//	private void copyDataBase() {
//		Util.log("Copying database from file");
//		try {
//			courseDao = getCourseDao();
////			Util.log("Amount of courses copied from db: "+courseDao.countOf());
//			
//				courseDao.updateRaw("DELETE FROM courses");
//				InputStream is = context.getAssets().open("studassen.db");
//				DataInputStream in = new DataInputStream(is);
//				BufferedReader br = new BufferedReader(new InputStreamReader(in));
//				String strLine;
//				while((strLine = br.readLine()) != null) {
//					courseDao.updateRaw(strLine);
//				}
//				in.close();
//			
//			Util.log("Database copied successfully");	
//		} catch (SQLException e) {
//			Util.log("Unable to copy database from file: SQLException");
//			e.printStackTrace();
//		}catch(IOException e) {
//			Util.log("Unable to copy database from file: IOException");
//			e.printStackTrace();
//		}
//		
//	}
//
//	/**
//	 * Returns the Database Access Object (DAO) for the Lecture class. It will create it or just give the cached
//	 * value.
//	 */
//	public Dao<Lecture, Integer> getLectureDao() throws SQLException {
//		if(lectureDao == null) {
//			lectureDao = getDao(Lecture.class);
//		}
//		return lectureDao;
//	}
//
//	/**
//	 * Returns the Database Access Object (DAO) for the Course class. It will create it or just give the cached
//	 * value.
//	 */
//	public Dao<MetaCourse, String> getCourseDao() throws SQLException {
//		if (courseDao == null) {
//			courseDao = getDao(MetaCourse.class);
//		}
//		return courseDao;
//	}
//	/**
//	 * Returns the Database Access Object (DAO) for the Course class. It will create it or just give the cached value. 
//	 * @return
//	 * @throws SQLException
//	 */
//	public Dao<Course, String> getSavedCoursesDao() throws SQLException {
//		if(savedCoursesDao == null) {
//			savedCoursesDao = getDao(Course.class);
//		}
//		return savedCoursesDao;
//	}
//
//	/**
//	 * Inserts the course object into the database
//	 * @param course
//	 * @throws SQLException
//	 */
//	public void insertCourse(MetaCourse course) throws SQLException{
//		getCourseDao().createOrUpdate(course);		
//	}
//
//	/**
//	 * Inserts a list of courses into database
//	 * @param courses
//	 * @throws SQLException
//	 */
//	public void insertCourses(List<MetaCourse> courses) throws SQLException {
//		for (MetaCourse course: courses) {
//			getCourseDao().createOrUpdate(course);
//		}
//	}
//
//	/**
//	 * Retrieves all course objects from the database, ordered by code
//	 * @return
//	 * @throws SQLException
//	 */
//	public List<MetaCourse> getAllCourses() throws SQLException {
//		QueryBuilder<MetaCourse, String> builder = getCourseDao().queryBuilder();
//		builder.orderBy("code", true);
//		return getCourseDao().query(builder.prepare());
//	}
//
//	/**
//	 * Retrieves a given course object
//	 * @param code - the course code
//	 * @return
//	 * @throws SQLException
//	 */
//	public MetaCourse getCourse(String code) throws SQLException {
//		return getCourseDao().queryForId(code);
//	}
//
//
//	/**
//	 * Removes a given course from the database
//	 * @param course
//	 * @throws SQLException
//	 */
//	public void removeCourse(MetaCourse course) throws SQLException {
//		getCourseDao().delete(course);
//	}
//
//	/**
//	 * Retrieves all Lecture objects from the database, sorted by starting time so lectures get chronologically sorted in the list
//	 * @return
//	 * @throws SQLException
//	 */
//	public List<Lecture> getMyLectures() throws SQLException {
//		QueryBuilder<Lecture, Integer> builder = getLectureDao().queryBuilder();
//		builder.orderBy("start", true);
//
//		return getLectureDao().query(builder.prepare());
//	}
//
//	/**
//	 * Inserts lecture into database
//	 * @param lecture
//	 * @throws SQLException
//	 */
//	public void insertLecture(Lecture lecture) throws SQLException {
//		getLectureDao().create(lecture);
//	}
//
//	/**
//	 * Inserts a list of lectures into database
//	 * @param lectureList
//	 * @throws SQLException
//	 */
//	public void insertLectures(List<Lecture> lectureList) throws SQLException {
//		for (Lecture lecture : lectureList) {
//			getLectureDao().create(lecture);
//		}
//	}
//
//	//TODO: fjerner kanskje ikke riktig. generated id blir vel annerledes? bør vel fjerne etter id. (deleteIds()
//	public void removeLecture(Lecture lecture) throws SQLException {
//		getLectureDao().delete(lecture);
//	}
//
//	public void insertSavedCourse(Course course) throws SQLException {
//		getSavedCoursesDao().create(course);
//	}
//
//	public List<Course> getSavedCourses() throws SQLException {
//		QueryBuilder<Course, String> builder = getSavedCoursesDao().queryBuilder();
//		builder.orderBy("code", true);
//
//		return getSavedCoursesDao().query(builder.prepare());
//	}
//
//	public void removeSavedCourse(Course course) throws SQLException {
//		getSavedCoursesDao().delete(course);
//	}
//
//
//	/**
//	 * Close the database connections and clear any cached DAOs.
//	 */
//	@Override
//	public void close() {
//		super.close();
//		courseDao = null;
//		lectureDao = null;
//		savedCoursesDao = null;
//	}
//}