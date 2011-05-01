package studentguiden.ntnu.dinner;

import java.util.Calendar;
import java.util.List;

import studentguiden.ntnu.entities.Canteen;
import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.FeedEntry;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.RSSHandler;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.social.SocialListArrayAdapter;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SelectCampusActivity extends ListActivity{
	private TextView title;

	private String[] canteenList = {"Hangaren",  
			"Realfagsbygget", 
			"Dragvoll", 
			"Tyholt", 
			"Øya", 
			"Kalvskinnet", 
			"Moholt", 
			"Ranheimsveien", 
			"Rotvoll", 
			"Dronning Mauds Minne"
	};

	private String[] dinnerURL  = {"http://www.sit.no/rss.ap?thisId=36444", 
			"http://www.sit.no/rss.ap?thisId=36447", 
			"http://www.sit.no/rss.ap?thisId=36441", 
			"http://www.sit.no/rss.ap?thisId=36450", 
			"http://www.sit.no/rss.ap?thisId=37228", 
			"http://www.sit.no/rss.ap?thisId=36453", 
			"http://www.sit.no/rss.ap?thisId=36456",
			"http://www.sit.no/rss.ap?thisId=38753",
			"http://www.sit.no/rss.ap?thisId=38910",
			"http://www.sit.no/rss.ap?thisId=38798"
	};



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dinner_campus_list);
		
		title = (TextView)findViewById(R.id.tv_list_title);
		title.setText(getString(R.string.text_select_place));
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, canteenList));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, DinnerActivity.class);
		intent.putExtra("URL", dinnerURL[position]);
		intent.putExtra("canteen", canteenList[position]);
		startActivity(intent);
	}
}
