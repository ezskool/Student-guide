package studentguiden.ntnu.courses;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.Lecture;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.JSONHelper;
import studentguiden.ntnu.misc.Util;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author Håkon Drolsum Røkenes
 * 
 */
public class CourseActivity extends Activity implements OnClickListener{
	private TextView courseName, courseDescription, courseCredit, courseLevel, courseGoals, courseDescriptionTitle, 
	courseGoalsTitle, courseType, courseSemesterTaught, coursePrerequisites, courseSchedule, courseScheduleTitle, tv_statusbar;
	private ProgressDialog pd;
	private ImageButton btn_back, btn_refresh;
	private Bundle extras;
	private String courseCode;
	private Button btn_add_my_course;
	private Course thisCourse;
	private String thisCourseId;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);

		Bundle extras = getIntent().getExtras();
		pd = ProgressDialog.show(this, "", getString(R.string.downloading_content));

		if(extras !=null){
			courseCode = extras.getString("code");
			thisCourseId = extras.getString("id");
			new ContentDownloader().execute(courseCode);
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
		tv_statusbar = (TextView)findViewById(R.id.tv_statusbar);

		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_refresh = (ImageButton)findViewById(R.id.btn_refresh);
		btn_back.setOnClickListener(this);
		btn_refresh.setOnClickListener(this);
		
		btn_add_my_course = (Button)findViewById(R.id.btn_add_to_my_courses);
		btn_add_my_course.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v==btn_back) {
			super.finish();
		}else if(v==btn_refresh) {
			if(!courseCode.equals("")) {
				new ContentDownloader().execute(courseCode);
			}
		}else if(v==btn_add_my_course) {
			CourseUtilities.addToMyCourses(thisCourse, this);			
		}
	}

	/**
	 * Updates all view elements with course data
	 * @param course
	 */
	public void updateView(Course course) {
		thisCourse = course;
		Util.log("Updating course view with course data");
		tv_statusbar.setText(course.getCode());
		courseName.setText(course.getName());
		courseLevel.setText(course.getStudyLevel());
		courseCredit.setText(course.getCredit()+" "+getString(R.string.student_points));

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
				courseSchedule.append(getString(R.string.taught_in_weeks)+lecture.getWeeksText());
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
			currentCourse = new Course();
			try {
				URL courseURL = new URL("http://www.ime.ntnu.no/api/course/"+params[0]);
				textContent = Util.downloadContent(courseURL);
				URL scheduleURL = new URL("http://www.ime.ntnu.no/api/schedule/"+params[0]);
				scheduleContent = Util.downloadContent(scheduleURL);
				
				JSONHelper.updateCourseData(currentCourse, textContent);
				JSONHelper.updateScheduleData(currentCourse, scheduleContent);
			}catch(MalformedURLException e) {
				Util.log("Content download failed: MalformedURLException");
				e.printStackTrace();
				return DOWNLOAD_FAILED_INVALID_URL;
			}catch(IOException e) {
				Util.log("Content download failed: IOException");
				e.printStackTrace();
				return DOWNLOAD_FAILED;
			}
			return DOWNLOAD_SUCCESSFUL;
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
