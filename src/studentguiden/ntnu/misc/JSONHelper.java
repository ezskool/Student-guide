package studentguiden.ntnu.misc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.Lecture;

public class JSONHelper {

	
	/**
	 * updates the course object with schedule data from json string schedule 
	 * @param course
	 * @param schedule
	 */
	public static void updateScheduleData(Course course, String schedule) {
		JSONArray jsonScheduleList;
		try {
			jsonScheduleList = new JSONObject(schedule).optJSONArray("activity");

			if(jsonScheduleList!=null) {
				int n = jsonScheduleList.length();

				for (int i = 0; i < n; i++) {
					Lecture lecture = new Lecture();
					JSONObject item = jsonScheduleList.getJSONObject(i);
					course.setCode(item.getString("courseCode"));
					lecture.setActivityDescription(item.getString("activityDescription"));
//					lecture.setWeeks(item.getString("weeks"));

					//TODO: iterate for more "schedules"? are there more schedules?
					JSONObject jsonSchedule = item.getJSONArray("activitySchedules").getJSONObject(0);
					lecture.setWeeks(jsonSchedule.getString("weeks"));
					lecture.setDay(jsonSchedule.getString("dayName"));
					lecture.setDayNumber(jsonSchedule.getInt("dayNumber"));
					lecture.setStart(jsonSchedule.getString("start"));
					lecture.setEnd(jsonSchedule.getString("end"));

					JSONObject jsonRooms = jsonSchedule.getJSONArray("rooms").getJSONObject(0);
					lecture.setRoom(jsonRooms.getString("location"));
					lecture.setRoomCode(jsonRooms.getString("lydiaCode"));

					course.addLecture(lecture);
				}
			}
		} catch (JSONException e) {
			Util.log("Parsing of schedule content failed: JSONException");
			e.printStackTrace();
		}
	}
	
	/**
	 * parses the raw course data, and updates the course object
	 * @param description
	 * @param schedule
	 * @return 
	 */
	public static void updateCourseData(Course course, String description) {
		
		try {
		JSONObject jsonCourseObject = new JSONObject(description).optJSONObject("course"); 
		if(jsonCourseObject != null) {
			course.setCode(getStringFromObject(jsonCourseObject, "code"));
			course.setName(getStringFromObject(jsonCourseObject, "name"));
			course.setCourseType(getStringFromObject(jsonCourseObject, "courseTypeName"));
			course.setCredit(getStringFromObject(jsonCourseObject, "credit"));
			course.setStudyLevel(getStringFromObject(jsonCourseObject, "studyLevelName"));

			JSONArray infoArray = jsonCourseObject.optJSONArray("infoType");
			if(infoArray!= null) {
				course.setDescription(getStringFromObject(infoArray.getJSONObject(1), "text"));

				course.setGoals(getStringFromObject(infoArray.getJSONObject(0), "text"));
				course.setPrerequisites(getStringFromObject(infoArray.getJSONObject(3), "name")+"\n"+getStringFromObject(infoArray.getJSONObject(3), "text"));
			}
		}
		}catch (JSONException e) {
			Util.log("parsing of course description content failed");
			e.printStackTrace();
		}
	}

	/**
	 * returns the string from json object if it exists, else returns "". This is done to avoid jsonexception, thus skipping the rest of the parsing process
	 * @param object
	 * @param query
	 * @return
	 * @throws JSONException
	 */
	private static String getStringFromObject(JSONObject object, String query) throws JSONException{
		if(object.has(query)) {
			return object.getString(query);
		}
		return "";
	}

}
