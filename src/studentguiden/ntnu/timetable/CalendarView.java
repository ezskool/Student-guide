package studentguiden.ntnu.timetable;

import java.util.Calendar;

import studentguiden.ntnu.main.R;

import com.exina.android.calendar.Cell;
import com.exina.android.calendar.CalendarView.OnCellTouchListener;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.widget.ImageView;

public class CalendarView extends ImageView {
    private static int WEEK_TOP_MARGIN = 74;
    private static int WEEK_LEFT_MARGIN = 40;
    private static int CELL_WIDTH = 58;
    private static int CELL_HEIGH = 53;
    private static int CELL_MARGIN_TOP = 92;
    private static int CELL_MARGIN_LEFT = 39;
    private static float CELL_TEXT_SIZE;
    
    private static final String TAG = "CalendarView"; 
	private Calendar mRightNow = null;
    private Drawable mWeekTitle = null;
    private Cell mToday = null;
    private Cell[][] mCells = new Cell[6][7];
    private OnCellTouchListener mOnCellTouchListener = null;
    private MonthDisplayHelper mHelper;
    private Drawable mDecoration = null;
    
    public interface OnCellTouchListener {
    	public void onTouch(Cell cell);
    }

	public CalendarView(Context context) {
		this(context, null);
	}
	
	public CalendarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	

	public CalendarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDecoration = context.getResources().getDrawable(R.drawable.typeb_calendar_today);		
		initCalendarView();
	}
	
	private void initCalendarView() {
		Resources res = getResources();
		
		WEEK_TOP_MARGIN  = (int) res.getDimension(R.dimen.week_top_margin);
		WEEK_LEFT_MARGIN = (int) res.getDimension(R.dimen.week_left_margin);
		
		CELL_WIDTH = (int) res.getDimension(R.dimen.cell_width);
		CELL_HEIGH = (int) res.getDimension(R.dimen.cell_heigh);
		CELL_MARGIN_TOP = (int) res.getDimension(R.dimen.cell_margin_top);
		CELL_MARGIN_LEFT = (int) res.getDimension(R.dimen.cell_margin_left);
		CELL_TEXT_SIZE = res.getDimension(R.dimen.cell_text_size);
	}
}
