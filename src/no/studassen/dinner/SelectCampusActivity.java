package no.studassen.dinner;

import java.util.ArrayList;

import no.studassen.R;
import no.studassen.entities.Canteen;
import no.studassen.misc.Util;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class SelectCampusActivity extends ListActivity implements OnClickListener{
	private TextView tv_statusbar;
	private CheckBox cb;
	private Button btn_todays_dinner;

	//	private String[] canteenList = {"Hangaren",  
	//			"Realfagsbygget", 
	//			"Dragvoll", 
	//			"Tyholt", 
	//			"Øya", 
	//			"Kalvskinnet", 
	//			"Moholt", 
	//			"Ranheimsveien", 
	//			"Rotvoll", 
	//			"Dronning Mauds Minne"
	//	};

	private Canteen[] canteenList = {
			new Canteen("Hangaren", R.drawable.campus_hangaren, "http://www.sit.no/rss.ap?thisId=36444"),
			new Canteen("Realfag", R.drawable.campus_realfag, "http://www.sit.no/rss.ap?thisId=36447")
	};

	//	private String[] dinnerURL  = {"http://www.sit.no/rss.ap?thisId=36444", 
	//			"http://www.sit.no/rss.ap?thisId=36447", 
	//			"http://www.sit.no/rss.ap?thisId=36441", 
	//			"http://www.sit.no/rss.ap?thisId=36450", 
	//			"http://www.sit.no/rss.ap?thisId=37228", 
	//			"http://www.sit.no/rss.ap?thisId=36453", 
	//			"http://www.sit.no/rss.ap?thisId=36456",
	//			"http://www.sit.no/rss.ap?thisId=38753",
	//			"http://www.sit.no/rss.ap?thisId=38910",
	//			"http://www.sit.no/rss.ap?thisId=38798"
	//	};



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_campus);
		this.setListAdapter(new DinnerListArrayAdapter(this, android.R.layout.simple_list_item_1, canteenList));

		tv_statusbar = (TextView)findViewById(R.id.tv_statusbar);
		tv_statusbar.setText(getString(R.string.dinner));
		btn_todays_dinner = (Button)findViewById(R.id.btn_todays_dinner);
		btn_todays_dinner.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v==btn_todays_dinner) {
			Intent intent = new Intent(this, DinnerActivity.class);

			ArrayList<Canteen> temp = new ArrayList<Canteen>();

			for (Canteen canteen: canteenList) {
				if(canteen.isChecked()) {
					temp.add(canteen);
				}
			}
			int size = temp.size();
			String[] selectedCanteens = new String[size];
			String[] selectedUrls =  new String[size];

			for (int i = 0; i < size; i++) {
				selectedCanteens[i] = temp.get(i).getName();
				selectedUrls[i] = temp.get(i).getUrl();
			}
			intent.putExtra("canteens", selectedCanteens);
			Util.log("starting dinneractivity. url count: "+selectedUrls.length);
			intent.putExtra("URLs", selectedUrls);
			startActivity(intent);
		}
	}

	//	@Override
	//	protected void onListItemClick(ListView l, View v, int position, long id) {
	//		super.onListItemClick(l, v, position, id);
	//		Intent intent = new Intent(this, DinnerActivity.class);
	//		
	//		String[] url = {canteenList[position].getUrl()};
	//		String[] canteen = {canteenList[position].getName()};
	//		
	//		intent.putExtra("URLs", url);
	//		intent.putExtra("canteens", canteen);
	//		startActivity(intent);
	//	}

	public void startDinnerActivity(Canteen canteen) {
		String[] urls = {canteen.getUrl()};
		String [] canteens = {canteen.getName()};

		Intent intent = new Intent(this, DinnerActivity.class);	
		intent.putExtra("URLs", urls);
		intent.putExtra("canteens", canteens);

		startActivity(intent);
	}
}
