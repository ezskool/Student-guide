package studentguiden.ntnu.courses;

import java.util.ArrayList;

import android.database.Cursor;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.MetaCourse;
import studentguiden.ntnu.storage.DataBaseAdapter;

public class CourseUtilities {
	private static ArrayList<MetaCourse> courseList;

	/**
	 * @return the courseList
	 */
	public static ArrayList<MetaCourse> getCourseList() {
		return courseList;
	}

	/**
	 * @param courseList the courseList to set
	 */
	public static void setCourseList(ArrayList<MetaCourse> courseList) {
		CourseUtilities.courseList = courseList;
	}
	
	/**
	 * Updates the courselist with the sorted list from database.
	 * @param database
	 */
	public static void updateCourseList(DataBaseAdapter database) {
		database.open();
		ArrayList<MetaCourse> courses = new ArrayList<MetaCourse>();

		Cursor c = database.getAllCourses();
		if (c.moveToFirst())
		{
			do {          
				courses.add(new MetaCourse(c.getString(1), c.getString(2)));
			} while (c.moveToNext());
		}
		database.close();
		setCourseList(courses);
	}
	
	public static void addToMyCourses(Course course) {
		
	}
}
