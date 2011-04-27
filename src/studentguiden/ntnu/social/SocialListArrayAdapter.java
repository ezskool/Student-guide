package studentguiden.ntnu.social;

import java.util.ArrayList;
import java.util.List;


import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.FeedEntry;
import studentguiden.ntnu.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SocialListArrayAdapter extends ArrayAdapter<FeedEntry>{

	private Context context;
	private List<FeedEntry> items;
	
	public SocialListArrayAdapter(Context context, int textViewResourceId,
			List<FeedEntry> items) {
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

        FeedEntry item = items.get(position);
        if (item!= null) {
        	ImageView image = (ImageView) view.findViewById(R.id.img_item_icon);
            TextView text1 = (TextView) view.findViewById(R.id.text1);
            TextView text2 = (TextView) view.findViewById(R.id.text2);
//            TextView text3 = (TextView) view.findViewById(R.id.text3);
            
            image.setImageResource(R.drawable.samfundet_logo2);
            text1.setText(item.getTitle());
            text2.setText("Category: "+item.getCategory());
            }

        return view;
    }
	

}
