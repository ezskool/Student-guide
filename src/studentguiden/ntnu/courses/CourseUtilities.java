package studentguiden.ntnu.courses;

import java.util.ArrayList;

import studentguiden.ntnu.entities.MetaCourse;

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
	
	
}
