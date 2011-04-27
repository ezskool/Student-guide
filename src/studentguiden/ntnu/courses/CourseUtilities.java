package studentguiden.ntnu.courses;

import java.util.ArrayList;

import studentguiden.ntnu.entities.Course;

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
	
	
}
