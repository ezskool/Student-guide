package no.studassen.dinner;
import no.studassen.R;

import no.studassen.entities.Canteen;
import no.studassen.misc.Util;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class DinnerListArrayAdapter extends ArrayAdapter<Canteen> {

	//	private Context context;
	private SelectCampusActivity context;
	private Canteen[] items;
	
	
	public DinnerListArrayAdapter(SelectCampusActivity context, int textViewResourceId,
			Canteen[] items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.items = items;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.dinner_list_item, null);
		}else {
			view = convertView;
		}

		if (items.length>0) {
			TextView text1 = (TextView) view.findViewById(R.id.text1);
			text1.setText(items[position].getName());
			text1.setTextSize(20);
		}
		view.setBackgroundResource(R.drawable.layout_list_item_1);

		final CheckBox checkbox = (CheckBox)view.findViewById(R.id.cb_campus);
		checkbox.setChecked(items[position].isChecked());
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				context.startDinnerActivity(items[position]);
			}
		});
		
		checkbox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				items[position].setSelected(checkbox.isChecked());
				Util.log("sets "+items[position].getName()+" to selected: " +checkbox.isChecked());				
			}
		});
		
		return view;
	}
}
