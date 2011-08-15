package studentguiden.ntnu.courses;

import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.storage.DatabaseHelper;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CourseCursorAdapter extends CursorAdapter{
	private DatabaseHelper dbAdapter = null;
	 
    public CourseCursorAdapter(Context context, Cursor c)
    {
        super(context, c);
        dbAdapter = new DatabaseHelper(context);
        dbAdapter.openReadableConnection();
    }
    
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        createView(view, cursor);  
    }
    
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
    	final LayoutInflater inflater = LayoutInflater.from(context);
    	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.list_item, parent, false);

        return view;
    }
    
    @Override
    public CharSequence convertToString(Cursor cursor) {
    	return cursor.getString(1);
    }
 
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        Cursor currentCursor = null;
        
        if (getFilterQueryProvider() != null) {
            return getFilterQueryProvider().runQuery(constraint);
        }
        
        String args = "";
        
        if (constraint != null)
        {
            args = constraint.toString();       
        }
 
        currentCursor = dbAdapter.getAutocompleteCursor(args);
        return currentCursor;
    }
    
    public View createView(View view, Cursor cursor) {
    	TextView text1 = (TextView)view.findViewById(R.id.text1);
    	TextView text2 = (TextView)view.findViewById(R.id.text2);
    	TextView text3 = (TextView)view.findViewById(R.id.text3);
    	
    	
        text1.setText(cursor.getString(1));
        text2.setText(cursor.getString(2));
        text3.setText(cursor.getString(3));
        return view;
    }
    
    public void close()
    {
        dbAdapter.close();
    }

}
