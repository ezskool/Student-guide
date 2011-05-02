package studentguiden.ntnu.dinner;

import java.util.ArrayList;
import java.util.List;


import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.FeedEntry;
import studentguiden.ntnu.entities.Canteen;
import studentguiden.ntnu.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DinnerListArrayAdapter extends ArrayAdapter<String>{

	private Context context;
	private String[] items;
	
	public DinnerListArrayAdapter(Context context, int textViewResourceId,
			String[] items) {
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

        String item = items[position];
        if (item!= null) {
//        	ImageView image = (ImageView) view.findViewById(R.id.img_item_icon);
            TextView text1 = (TextView) view.findViewById(R.id.text1);
         
//            TextView text3 = (TextView) view.findViewById(R.id.text3);
            
//            image.setImageResource(R.drawable.samfundet_logo2);
            text1.setText(item);
            text1.setTextSize(20);
//            text2.setText("Category: "+item.getCategory());
            }
        
        if((position % 2)==1) {
        	view.setBackgroundResource(R.drawable.layout_list_item_1);
        }else {
        	view.setBackgroundResource(R.drawable.layout_list_item_2);
        }

        return view;
    }
	

}
