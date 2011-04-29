package studentguiden.ntnu.timetable;

import com.exina.android.calendar.CalendarView;
import com.exina.android.calendar.Cell;

import studentguiden.ntnu.main.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

/**
 * @author Håkon Drolsum Røkenes
 * 
 */

public class TimetableActivity extends Activity implements CalendarView.OnCellTouchListener{
	public static final String MIME_TYPE = "vnd.android.cursor.dir/vnd.exina.android.calendar.date";
	private CalendarView mView = null;
	private TextView mHit;
	private Handler mHandler = new Handler();


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timetable);

		mView = (CalendarView)findViewById(R.id.calendar);
		mView.setOnCellTouchListener(this);

		if(getIntent().getAction().equals(Intent.ACTION_PICK)) {
			findViewById(R.id.hit).setVisibility(View.INVISIBLE);
		}



	}

	@Override
	public void onTouch(Cell cell) {
		// TODO Auto-generated method stub

	}
}
