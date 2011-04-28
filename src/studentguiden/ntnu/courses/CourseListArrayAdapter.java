package studentguiden.ntnu.courses;

import java.util.ArrayList;


import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.MetaCourse;
import studentguiden.ntnu.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CourseListArrayAdapter extends ArrayAdapter<MetaCourse>{

	private Context context;
	private ArrayList<MetaCourse> items;
	
	public CourseListArrayAdapter(Context context, int textViewResourceId,
			ArrayList<MetaCourse> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.items = items;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }else {
        	view = convertView;
        }

       MetaCourse item = items.get(position);
        if (item!= null) {
            TextView text1 = (TextView) view.findViewById(R.id.text1);
            TextView text2 = (TextView) view.findViewById(R.id.text2);
            
            text1.setText(item.getCode());
            text2.setText(item.getName());
            }

        return view;
    }
	

}
