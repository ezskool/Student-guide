package studentguiden.ntnu.courses;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.Lecture;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.JSONHelper;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.storage.DatabaseHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Håkon Drolsum Røkenes
 * 
 */
public class CourseActivity extends Activity implements OnClickListener, OnItemClickListener {
	private TextView courseName, courseDescription, courseCredit, courseLevel, courseGoals, courseDescriptionTitle, 
	courseGoalsTitle, courseType, courseSemesterTaught, coursePrerequisites, courseSchedule, courseScheduleTitle, tv_statusbar_page, tv_home;
	private ProgressDialog pd;
	private Bundle extras;
	private String courseCode;
	private Button btn_add_my_course;
	private Course thisCourse;
	private AutoCompleteTextView ac_search_courses;
	private Cursor c;
	private CourseCursorAdapter cursorAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);

		//		Bundle extras = getIntent().getExtras();
		//		pd = 
		//
		//		if(extras !=null){
		//			courseCode = extras.getString("code");
		//			new ContentDownloader().execute(courseCode);
		//		}

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
		tv_statusbar_page = (TextView)findViewById(R.id.tv_statusbar_page);
		tv_statusbar_page.setText(getString(R.string.courses_page));
		btn_add_my_course = (Button)findViewById(R.id.btn_add_to_my_courses);
		btn_add_my_course.setOnClickListener(this);

		tv_home = (TextView)findViewById(R.id.tv_statusbar);
		tv_home.setOnClickListener(this);


		initCursorAdapter();
	}


	//TODO: async
	@Override
	public void onClick(View v) {
		if(v==btn_add_my_course) {
			DatabaseHelper db = new DatabaseHelper(this);
			db.openWritableConnection();
			try {
				db.insertMyCourse(thisCourse);
				db.insertLectures(thisCourse.getLectureList());
			} catch (SQLException e) {
				Util.log("Insertion of my courses and lectures failed");
				e.printStackTrace();
			}

			db.close();
		}else if(v==tv_home) {
			this.finish();
		}

	}

	/**
	 * Updates all view elements with course data
	 * @param course
	 */
	public void updateView(Course course) {
		btn_add_my_course.setVisibility(Button.VISIBLE);
		thisCourse = course;
		Util.log("Updating course view with course data");
		tv_statusbar_page.setText(getString(R.string.courses_page)+"/"+course.getCode());
		courseName.setText(course.getCode()+" - "+course.getNameNo());
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



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		TextView text = (TextView) arg0.findViewById(R.id.text1);
		new ContentDownloader(this).execute(text.getText().toString());
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(ac_search_courses.getWindowToken(), 0);
		ac_search_courses.setText("");
	}


	public void initCursorAdapter(){
		ac_search_courses = (AutoCompleteTextView) findViewById(R.id.ac_search_course);
		DatabaseHelper db = new DatabaseHelper(this);
		db.openReadableConnection();

		c = db.getAutocompleteCursor("");
		startManagingCursor(c);

		cursorAdapter = new CourseCursorAdapter(this, c);

		ac_search_courses = (AutoCompleteTextView) findViewById(R.id.ac_search_course);
		ac_search_courses.setAdapter(cursorAdapter);
		ac_search_courses.setThreshold(1);
		ac_search_courses.setOnItemClickListener(this);
		db.close();
	}


	private class ContentDownloader extends AsyncTask<String, Void, Integer>{

		private String textContent = "";
		private String scheduleContent = "";
		private final int DOWNLOAD_SUCCESSFUL = 0;
		private final int DOWNLOAD_FAILED = 1;
		private final int DOWNLOAD_FAILED_INVALID_URL = 2;
		private final int PARSING_FAILED = 3;
		private Course currentCourse;
		private Context context;

		public ContentDownloader(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute(){
			CourseActivity.this.pd = ProgressDialog.show(context, "", getString(R.string.downloading_content));
		}

		@Override
		protected Integer doInBackground(String... params) {
			currentCourse = new Course();
			try {
				textContent = Util.downloadContent("http://www.ime.ntnu.no/api/course/", params[0]);
				scheduleContent = Util.downloadContent("http://www.ime.ntnu.no/api/schedule/", params[0]);

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
