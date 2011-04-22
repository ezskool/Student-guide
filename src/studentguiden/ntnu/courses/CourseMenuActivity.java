package studentguiden.ntnu.courses;

import studentguiden.ntnu.main.R;
import studentguiden.ntnu.main.R.id;
import studentguiden.ntnu.main.R.layout;
import studentguiden.ntnu.main.R.string;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CourseMenuActivity extends Activity implements OnClickListener{

	private Button btn_search_course, btn_search;
	private final int DIALOG_SEARCH_COURSE = 0;
	private EditText et_search_text;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_menu);

		btn_search_course = (Button)findViewById(R.id.btn_search_course);
		btn_search_course.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v==btn_search_course) {
			showDialog(DIALOG_SEARCH_COURSE);
		}else if(v==btn_search) {
			Intent intent = new Intent(CourseMenuActivity.this, CourseActivity.class);
			intent.putExtra("courseId", et_search_text.getText().toString());
			removeDialog(DIALOG_SEARCH_COURSE);
			
			startActivity(intent);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch(id) {
		case DIALOG_SEARCH_COURSE:
			dialog = new Dialog(this);
			dialog.setTitle(getString(R.string.title_search_course));
			dialog.setContentView(R.layout.dialog_search_course);
			btn_search = (Button)dialog.findViewById(R.id.btn_search);
			btn_search.setOnClickListener(this);
			et_search_text = (EditText)dialog.findViewById(R.id.et_searchtext);
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

}
