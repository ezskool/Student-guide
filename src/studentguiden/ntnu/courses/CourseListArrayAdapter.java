package studentguiden.ntnu.courses;

import java.util.List;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.main.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CourseListArrayAdapter extends ArrayAdapter<Course>{

	private Context context;
	private List<Course> items;

	public CourseListArrayAdapter(Context context, int textViewResourceId,
			List<Course> items) {
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

		Course item = items.get(position);
		if (item!= null) {
			TextView text1 = (TextView) view.findViewById(R.id.text1);
			TextView text2 = (TextView) view.findViewById(R.id.text2);
			TextView text3 = (TextView) view.findViewById(R.id.text3);

			text1.setText(item.getCode());
			text2.setText(item.getName());
			text3.setText(item.getName_en());

			if((position % 2)==1) {
				view.setBackgroundResource(R.drawable.layout_list_item_1);
			}else {
				view.setBackgroundResource(R.drawable.layout_list_item_2);
			}

		}
		return view;
	}
}
