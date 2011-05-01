package studentguiden.ntnu.courses;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.Lecture;
import studentguiden.ntnu.entities.MetaCourse;
import studentguiden.ntnu.misc.Util;
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
				courses.add(new MetaCourse(c.getString(0), c.getString(1)));
			} while (c.moveToNext());
		}
		database.close();
		setCourseList(courses);
	}
	
	public static void addToMyCourses(Course course, Context context) {
		DataBaseAdapter database = new DataBaseAdapter(context);
		database.open();
		
		database.insertMyCourse(course.getCode());
		
		for (Lecture lecture : course.getLectureList()) {
			Util.log("inserting "+course.getLectureList().size()+" lectures");
			database.insertLecture(course.getCode(), lecture.getStart(), lecture.getEnd(), lecture.getDay(), lecture.getWeeksText(), lecture.getRoom());
		}
		database.close();
		printMyCourses(context);
		printMyLectures(context);
	}
	
	public static void printMyCourses(Context context) {
		DataBaseAdapter database = new DataBaseAdapter(context);
		database.open();
		
		Cursor c = database.getMyCourses();
		  if (c.moveToFirst())
	        {
	            do {          
	                Util.log("following course is mine: "+c.getString(0));
	            } while (c.moveToNext());
	        }
		  database.close();
	}
	
	public static void printMyLectures(Context context) {
		DataBaseAdapter database = new DataBaseAdapter(context);
		database.open();
		
		Cursor c = database.getMyLectures();
		  if (c.moveToFirst()) {
	            do {          
	                Util.log("following lecture is mine: "+c.getString(1)+"starting "+c.getString(2)+"-"+c.getString(3));
	            } while (c.moveToNext());
	        }
		  database.close();
	}
	
	public static ArrayList<Lecture> getMyLectures(Context context) {
		ArrayList<Lecture> lectures = new ArrayList<Lecture>();
		
		DataBaseAdapter database = new DataBaseAdapter(context);
		database.open();
		Cursor c = database.getMyLectures();
		if (c.moveToFirst()) {
            do {          
            	Util.log("found lecture in db: starting "+c.getString(1));
              lectures.add(new Lecture(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6)));
            } while (c.moveToNext());
        }
		database.close();
		return lectures;
	}
}
