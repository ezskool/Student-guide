package studentguiden.ntnu.courses;

import java.util.ArrayList;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.Lecture;
import android.content.Context;

public class CourseUtilities {
	private static ArrayList<Course> courseList;

	/**
	 * @return the courseList
	 */
	public static ArrayList<Course> getCourseList() {
		return courseList;
	}

	/**
	 * @param courseList the courseList to set
	 */
	public static void setCourseList(ArrayList<Course> courseList) {
		CourseUtilities.courseList = courseList;
	}

	/**
	 * Updates the courselist with the sorted list from database.
	 * @param database
	 */
//	public static void updateCourseList(DataBaseAdapter database) {
//		database.open();
//		ArrayList<MetaCourse> courses = new ArrayList<MetaCourse>();
//
//		Cursor c = database.getAllCourses();
//		if (c.moveToFirst())
//		{
//			do {          
//				//				courses.add(new MetaCourse(c.getString(0), c.getString(1)));
//			} while (c.moveToNext());
//		}
//		database.close();
//		setCourseList(courses);
//	}

//	public static void addToMyCourses(Course course, Context context) {
//		DataBaseAdapter database = new DataBaseAdapter(context);
//		database.open();
//
//		database.insertMyCourse(course.getCode(), course.getName());
//
//		for (Lecture lecture : course.getLectureList()) {
//			Util.log("inserting "+course.getLectureList().size()+" lectures");
//			database.insertLecture(course.getCode(), lecture.getStart(), lecture.getEnd(), lecture.getDay(), lecture.getDayNumber(), lecture.getWeeksText(), lecture.getRoom());
//		}
//		database.close();
//	}

//	/**
//	 * Returns the list of courses added by the user.
//	 * @throws SQLException 
//	 */
//	public static List<Course> getMyCourses(Context context) {
//		DatabaseHelper db = new DatabaseHelper(context);
//		try {
//			return db.getAllCourses();
//		}catch(SQLException e){
//			Util.log("Retrieving my courses database failed");
//			e.printStackTrace();
//		}
//
//		return null;
//	}

	public static ArrayList<Lecture> getMyLectures(Context context) {
		ArrayList<Lecture> lectures = new ArrayList<Lecture>();
//
//		DataBaseAdapter database = new DataBaseAdapter(context);
//		database.open();
//		Cursor c = database.getMyLectures();
//		if (c.moveToFirst()) {
//			do {          
//				Util.log("found lecture in db: starting "+c.getString(2)+" on day "+c.getString(4)+". daynumber "+c.getInt(5));
//				lectures.add(new Lecture(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), c.getString(6), c.getString(7)));
//			} while (c.moveToNext());
//		}
//		database.close();
		return lectures;
	}
}
