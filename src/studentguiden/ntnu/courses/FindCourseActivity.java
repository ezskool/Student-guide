package studentguiden.ntnu.courses;


import studentguiden.ntnu.main.R;
import studentguiden.ntnu.main.Util;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * @author Håkon Drolsum Røkenes
 * 
 */

public class FindCourseActivity extends ListActivity implements OnClickListener, OnEditorActionListener{

	private Button btn_search;
	private EditText et_search_text;
	protected String[] courseList, courseNameList;
	private LayoutInflater inflater;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_menu);

		Bundle extras = getIntent().getExtras();
		if(!extras.isEmpty()) {
			courseList = extras.getStringArray("courseList");
			courseNameList = extras.getStringArray("courseNameList");
		}

		btn_search = (Button)findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);

		et_search_text = (EditText)findViewById(R.id.et_course_search);
		et_search_text.setOnEditorActionListener(this);

		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, courseList) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View row;

				if (null == convertView) {
					row = inflater.inflate(R.layout.list_item, null);
				} else {
					row = convertView;
				}

				TextView tv1 = (TextView) row.findViewById(R.id.text1);
				tv1.setText(getItem(position));

				TextView tv2 = (TextView) row.findViewById(R.id.text2);
				tv2.setText(courseNameList[position]);

				return row;
			}
		});
	}



	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String selectedCourse = this.getListAdapter().getItem(position).toString();
		startCourseActivity(selectedCourse);
	}

	/**
	 * Starts the CourseActivity class for a course
	 * @param courseId the respective course id
	 */
	private void startCourseActivity(String courseId){
		Intent intent = new Intent(FindCourseActivity.this, CourseActivity.class);
		intent.putExtra("courseId", courseId);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if(v==btn_search) {
			startCourseActivity(et_search_text.getText().toString());
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(v==et_search_text) {

		}
		return false;
	}
}
