package studentguiden.ntnu.dinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import studentguiden.ntnu.courses.CourseListArrayAdapter;
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
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SelectCampusActivity extends ListActivity implements OnClickListener{
	private TextView tv_statusbar;
	private ImageView btn_refresh, btn_back;

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
				
	
		this.setListAdapter(new DinnerListArrayAdapter(this, android.R.layout.simple_list_item_1, canteenList));
//		this.setListAdapter(new CourseListArrayAdapter(this, android.R.layout.simple_list_item_1, filteredCourseList));
		
		btn_refresh = (ImageView)findViewById(R.id.btn_refresh);
		btn_refresh.setOnClickListener(this);
		btn_back = (ImageView)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		tv_statusbar = (TextView)findViewById(R.id.tv_statusbar);
		tv_statusbar.setText(getString(R.string.dinner));
	}
	
	@Override
	public void onClick(View v) {
		if(v==btn_back) {
			super.finish();
		}else if(v==btn_refresh) {
			setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, canteenList));
		}
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
