package studentguiden.ntnu.courses;

import java.sql.SQLException;
import java.util.List;

import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.storage.DatabaseHelper;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CourseListArrayAdapter extends ArrayAdapter<Course> {

	private Context context;
	private List<Course> items;
	private LinearLayout list_item_text;
	private int listItemLayoutResource;
	private ImageButton btn_remove_course;

	public CourseListArrayAdapter(Context context, int textViewResourceId,
			List<Course> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.items = items;
		this.listItemLayoutResource = textViewResourceId;
	}


	//TODO: implement viewholder pattern
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(listItemLayoutResource, null);
		}else {
			view = convertView;
		}
		btn_remove_course = (ImageButton) view.findViewById(R.id.btn_remove_course);
		btn_remove_course.setFocusable(false);
		btn_remove_course.setOnClickListener(new OnButtonClickListener(position));
		list_item_text = (LinearLayout) view.findViewById(R.id.list_item_text);
		list_item_text.setOnClickListener(new OnItemClickListener(position));

		Course item = items.get(position);
		if (item!= null) {
			TextView text1 = (TextView) view.findViewById(R.id.tv_code);
			TextView text2 = (TextView) view.findViewById(R.id.tv_course_name_no);
			TextView text3 = (TextView) view.findViewById(R.id.tv_course_name_en);

			text1.setText(item.getCode());
			text2.setText(item.getName_no());
			text3.setText(item.getName_en());

			if(item.getColor() != null) {
				view.setBackgroundColor(Integer.parseInt(item.getColor()));
				//btn_add_my_course.setBackgroundColor(Integer.parseInt(thisCourse.getColor()));


			}

		}
		return view;
	}

	private class OnItemClickListener implements OnClickListener{           
		private int mPosition;
		OnItemClickListener(int position){
			mPosition = position;
		}
		@Override
		public void onClick(View v) {
			startCourseActivity(getItem(mPosition).getCode());
		}               
	}

	private void startCourseActivity(String code) {
		Intent intent = new Intent(context, CourseActivity.class);
		intent.putExtra("code", code);
		context.startActivity(intent);
	}


	private class OnButtonClickListener implements OnClickListener{           
		private int mPosition;
		OnButtonClickListener(int position){
			mPosition = position;
		}
		@Override
		public void onClick(View v) {
			removeCourse(getItem(mPosition)); 
		}               
	}

	private void removeCourse(Course course) {
		DatabaseHelper db = new DatabaseHelper(context);
		db.openWritableConnection();
		try {
			db.removeMyCourse(course);

		} catch (SQLException e) {
			Util.log("Unable to delete item from db: "+course.getCode());
			e.printStackTrace();
		} finally {
			this.remove(course);
		}
		db.close();

	}
}
