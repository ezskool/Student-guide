package studentguiden.ntnu.courses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.Lecture;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.main.R.id;
import studentguiden.ntnu.misc.Util;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author Håkon Drolsum Røkenes
 * 
 */
public class CourseActivity extends Activity{
	private TextView courseName, courseDescription, courseCredit, courseLevel, courseGoals, courseDescriptionTitle, 
	courseGoalsTitle, courseType, courseSemesterTaught, coursePrerequisites, courseSchedule;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);

		Bundle extras = getIntent().getExtras();

		if(extras !=null){
			new ContentDownloader().execute(extras.getString("courseId"));
		}

		courseName = (TextView)findViewById(R.id.tv_coursename);
		courseDescription = (TextView)findViewById(R.id.tv_coursedescription);
		courseLevel = (TextView)findViewById(R.id.tv_courseLevel);
		courseCredit = (TextView)findViewById(R.id.tv_courseCredit);
		courseGoals = (TextView)findViewById(R.id.tv_courseGoals);
		courseDescriptionTitle = (TextView)findViewById(R.id.tv_courseDescription_title);
		courseGoalsTitle = (TextView)findViewById(R.id.tv_courseGoals_title);
		courseType = (TextView)findViewById(R.id.tv_course_type);
		courseSemesterTaught = (TextView)findViewById(R.id.tv_course_semester);
		coursePrerequisites = (TextView)findViewById(R.id.tv_course_prerequisites);
		courseSchedule = (TextView)findViewById(R.id.tv_course_schedule);

	}

	/**
	 * Updates all view elements with course data
	 * @param course
	 */
	public void updateView(Course course) {
		courseName.setText(course.getName());
		courseLevel.setText(course.getStudyLevel());
		courseCredit.setText(course.getCredit()+" "+getString(R.string.student_points));
		courseType.setText(getString(R.string.course_type)+" "+course.getCourseType());

		if(course.isTaughtInSpring()) {
			courseSemesterTaught.setText(getString(R.string.taught_in_semester_spring));
		}else if(course.isTaughtInAutumn()) {
			courseSemesterTaught.setText(getString(R.string.taught_in_semester_autumn));
		}

		coursePrerequisites.setText(course.getPrerequisites());
		courseGoalsTitle.setText(R.string.course_goals);
		courseGoals.setText(course.getGoals());
		courseDescriptionTitle.setText(R.string.course_description);
		courseDescription.setText(course.getDescription());

		for (Lecture lecture : course.getLectureList()) {
			courseSchedule.append(lecture.getDay()+" ");
			courseSchedule.append(lecture.getStart()+"-");
			courseSchedule.append(lecture.getEnd()+"\n");
			courseSchedule.append(getString(R.string.room)+lecture.getRoom()+"\n");
			courseSchedule.append(getString(R.string.taught_in_weeks)+lecture.getWeeks());
			courseSchedule.append("\n\n");
		}

	}


	private class ContentDownloader extends AsyncTask<String, Void, Integer>{

		private String textContent = "";
		private String scheduleContent = "";
		private final int DOWNLOAD_SUCCESSFUL = 0;
		private final int DOWNLOAD_FAILED = 1;
		private final int DOWNLOAD_FAILED_INVALID_URL = 2;
		private final int PARSING_FAILED = 3;
		private Course currentCourse;

		@Override
		protected Integer doInBackground(String... params) {
			try {
				URL courseURL = new URL("http://www.ime.ntnu.no/api/course/"+params[0]);
				textContent = Util.downloadContent(courseURL);
				URL scheduleURL = new URL("http://www.ime.ntnu.no/api/schedule/"+params[0]);
				scheduleContent = Util.downloadContent(scheduleURL);

				currentCourse  = createCourseObject(textContent, scheduleContent);
			}catch(MalformedURLException e) {
				Util.log("Content download failed: MalformedURLException");
				return DOWNLOAD_FAILED_INVALID_URL;
			}catch(IOException e) {
				Util.log("Content download failed: IOException");
				e.printStackTrace();
				return DOWNLOAD_FAILED;
			}catch(JSONException e) {
				Util.log("Parsing failed: JSONException");
				return PARSING_FAILED;

			}
			return DOWNLOAD_SUCCESSFUL;
		}

		/**
		 * parses the raw schedule and course data, and creates a course object
		 * @param description
		 * @param schedule
		 * @return 
		 */
		private Course createCourseObject(String description, String schedule) throws JSONException{

			Course temp = new Course();
			JSONObject jsonCourseObject = new JSONObject(description).getJSONObject("course"); 
			temp.setCode(jsonCourseObject.getString("code"));
			temp.setName(jsonCourseObject.getString("name"));
			temp.setCourseType(jsonCourseObject.getString("courseTypeName"));
			temp.setCredit(jsonCourseObject.getString("credit"));
			temp.setStudyLevel(jsonCourseObject.getString("studyLevelName"));

			JSONArray infoArray = jsonCourseObject.getJSONArray("infoType");

			temp.setDescription(infoArray.getJSONObject(1).getString("text"));
			temp.setGoals(infoArray.getJSONObject(0).getString("text"));
			temp.setPrerequisites(infoArray.getJSONObject(3).getString("name")+"\n"+infoArray.getJSONObject(3).getString("text"));

			JSONArray jsonScheduleList = new JSONObject(schedule).getJSONArray("activity");

			int n = jsonScheduleList.length();
			for (int i = 0; i < n; i++) {
				Lecture lecture = new Lecture();
				JSONObject item = jsonScheduleList.getJSONObject(i);
				lecture.setActivityDescription(item.getString("activityDescription"));
				lecture.setWeeks(item.getString("weeks"));

				//TODO: iterate for more "schedules"? are there more schedules?
				JSONObject jsonSchedule = item.getJSONArray("activitySchedules").getJSONObject(0);

				lecture.setDay(jsonSchedule.getString("dayName"));
				lecture.setDayNumber(jsonSchedule.getInt("dayNumber"));
				lecture.setStart(jsonSchedule.getString("start"));
				lecture.setEnd(jsonSchedule.getString("end"));

				JSONObject jsonRooms = jsonSchedule.getJSONArray("rooms").getJSONObject(0);
				lecture.setRoom(jsonRooms.getString("location"));
				lecture.setRoomCode(jsonRooms.getString("lydiaCode"));

				temp.addLecture(lecture);
			}
			return temp;
		}

		/**
		 * Called when doInBackground is finished, and updates the UI thread with course information if successful.
		 * If the download failed, a toast message will appear.
		 * @param Integer - indicating whether or not download was successful
		 */
		@Override
		protected void onPostExecute(Integer result) {
			if(result==DOWNLOAD_SUCCESSFUL) {
				updateView(currentCourse);
			}else if(result==DOWNLOAD_FAILED) {
				Util.displayToastMessage(getString(R.string.download_failed_toast),CourseActivity.this.getApplicationContext());
			}else if(result==DOWNLOAD_FAILED_INVALID_URL) {
				Util.displayToastMessage(getString(R.string.invalid_url_toast),CourseActivity.this.getApplicationContext());
			}
		}


	}

}
