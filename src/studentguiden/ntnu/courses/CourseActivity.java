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
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author Håkon Drolsum Røkenes
 * 
 */
public class CourseActivity extends Activity{
	private TextView courseName, courseDescription, courseCredit, courseLevel, courseGoals, courseDescriptionTitle, 
	courseGoalsTitle, courseType, courseSemesterTaught, coursePrerequisites, courseSchedule, courseScheduleTitle;
	private ProgressDialog pd;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);

		Bundle extras = getIntent().getExtras();
		pd = ProgressDialog.show(this, "", getString(R.string.downloading_content));

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
		courseScheduleTitle = (TextView)findViewById(R.id.tv_course_schedule_title);

	}

	/**
	 * Updates all view elements with course data
	 * @param course
	 */
	public void updateView(Course course) {
		Util.log("Updating course view with course data");
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
		if(course.getLectureList().size()>0) {
			courseScheduleTitle.setText(getString(R.string.tv_course_schedule_header));
			for (Lecture lecture : course.getLectureList()) {
				courseSchedule.append(lecture.getDay()+" ");
				courseSchedule.append(lecture.getStart()+"-");
				courseSchedule.append(lecture.getEnd()+"\n");
				courseSchedule.append(getString(R.string.room)+lecture.getRoom()+"\n");
				courseSchedule.append(getString(R.string.taught_in_weeks)+lecture.getWeeks());
				courseSchedule.append("\n\n");
			}
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
				e.printStackTrace();
				return DOWNLOAD_FAILED_INVALID_URL;
			}catch(IOException e) {
				Util.log("Content download failed: IOException");
				e.printStackTrace();
				return DOWNLOAD_FAILED;
			}catch(JSONException e) {
				Util.log("Parsing failed: JSONException");
				e.printStackTrace();
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
			//TODO: kaller jsonexception hvis node ikke finnes, noe det ikke gjøre for mange fag
			Course temp = new Course();
			JSONObject jsonCourseObject = new JSONObject(description).optJSONObject("course"); 

			temp.setCode(getStringFromObject(jsonCourseObject, "code"));
			temp.setName(getStringFromObject(jsonCourseObject, "name"));
			temp.setCourseType(getStringFromObject(jsonCourseObject, "courseTypeName"));
			temp.setCredit(getStringFromObject(jsonCourseObject, "credit"));
			temp.setStudyLevel(getStringFromObject(jsonCourseObject, "studyLevelName"));

			JSONArray infoArray = jsonCourseObject.optJSONArray("infoType");
			if(infoArray!= null) {
				temp.setDescription(getStringFromObject(infoArray.getJSONObject(1), "text"));

				temp.setGoals(getStringFromObject(infoArray.getJSONObject(0), "text"));
				temp.setPrerequisites(getStringFromObject(infoArray.getJSONObject(3), "name")+"\n"+getStringFromObject(infoArray.getJSONObject(3), "text"));
			}
			JSONArray jsonScheduleList = new JSONObject(schedule).getJSONArray("activity");
			if(jsonScheduleList!=null) {
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
			}
			return temp;
		}

		private String getStringFromObject(JSONObject object, String query) throws JSONException{
			if(object.has(query)) {
				return object.getString(query);
			}
			return "";
		}



		/**
		 * Called when doInBackground is finished, and updates the UI thread with course information if successful.
		 * If the download failed, a toast message will appear.
		 * @param Integer - indicating whether or not download was successful
		 */
		@Override
		protected void onPostExecute(Integer result) {
			if(result==DOWNLOAD_SUCCESSFUL) {
				Util.log("download of course successful. Course code: "+currentCourse.getCode());
				updateView(currentCourse);
			}else if(result==DOWNLOAD_FAILED) {
				Util.displayToastMessage(getString(R.string.download_failed_toast),CourseActivity.this.getApplicationContext());
			}else if(result==DOWNLOAD_FAILED_INVALID_URL) {
				Util.displayToastMessage(getString(R.string.invalid_url_toast),CourseActivity.this.getApplicationContext());
			}
			CourseActivity.this.pd.cancel();
		}


	}

}
