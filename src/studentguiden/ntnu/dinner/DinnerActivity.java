package studentguiden.ntnu.dinner;


import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


import studentguiden.ntnu.entities.FeedEntry;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.RSSHandler;
import studentguiden.ntnu.misc.Util;
import studentguiden.ntnu.social.SocialActivity;
import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class DinnerActivity extends Activity implements OnClickListener{
	private TextView tv_dinner_title, tv_dinner_description, tv_statusbar;
	private ImageButton btn_refresh, btn_back;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dinner);		
		
		tv_dinner_title = (TextView)findViewById(R.id.tv_dinner_title);
		tv_dinner_description = (TextView)findViewById(R.id.tv_dinner_description);
		tv_statusbar = (TextView)findViewById(R.id.tv_statusbar);
		
		btn_refresh = (ImageButton)findViewById(R.id.btn_refresh);
		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_refresh.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		
		Bundle extras = getIntent().getExtras();
		tv_statusbar.setText(extras.getString("canteen"));
		url = extras.getString("URL");
		new DinnerDownloader().execute(url);
	}
	
	@Override
	public void onClick(View v) {
		if(v==btn_back) {
			super.finish();
		}else if(v==btn_refresh) {
			if(!url.equals("")) {
				tv_dinner_description.setText("");
				new DinnerDownloader().execute(url);
			}
		}
		
	}

	private class DinnerDownloader extends AsyncTask<String, Void, Integer> {
		private final int DOWNLOAD_SUCCESSFUL = 0;
		private final int DOWNLOAD_FAILED = 1;
		private final int DOWNLOAD_FAILED_INVALID_URL = 2;
		private final int PARSING_FAILED = 3;
		private String rawText = "";
		private RSSHandler feedHandler;
		private List<FeedEntry> entries;

		@Override
		protected void onPreExecute() {
			feedHandler = new RSSHandler();
		}

		@Override
		protected Integer doInBackground(String... params) {
			
			try {
				entries = feedHandler.getLatestArticles(params[0]+getCurrentDayURI());
			} catch (IOException e) {
				e.printStackTrace();
				return DOWNLOAD_FAILED;
			} catch (SAXException e) {
				e.printStackTrace();
				return PARSING_FAILED;
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				return PARSING_FAILED;
			}

			
			return DOWNLOAD_SUCCESSFUL;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == DOWNLOAD_SUCCESSFUL) {
				Util.log("Dinner data download was successful");
				displayEntryElements();
			}else if(result == DOWNLOAD_FAILED || result == DOWNLOAD_FAILED_INVALID_URL) {
				//TODO: si fra at download faila
				Util.log("Dinner data download failed");
				Util.displayToastMessage(getString(R.string.download_failed_toast),DinnerActivity.this.getApplicationContext());
			}
		}

		private void displayEntryElements() {
			DinnerActivity.this.tv_dinner_title.setText(Html.fromHtml(entries.get(0).getTitle()));
			DinnerActivity.this.tv_dinner_description.append(Html.fromHtml(entries.get(0).getDescription()));
		}

		private String getCurrentDayURI() {
//			Calendar calendar = Calendar.getInstance();
//			int weekday = calendar.get(Calendar.DAY_OF_WEEK);
//
//			switch(weekday){
//
//			case 2:
//				return "&mo=on";
//			case 3:
//				return "&ti=on";
//			case 4:
//				return "&on=on";
//			case 5:
//				return "&tu=on";
//			case 6:
//				return "&fr=on";
//			case 7:
//				return "&sa=on";
//			case 1:
//				return "&su=on";
//			}
			return "&ma=on&ti=on&on=on&to=on&fr=on";
		}
	}

	
	}

