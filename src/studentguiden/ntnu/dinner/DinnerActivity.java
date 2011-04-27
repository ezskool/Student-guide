package studentguiden.ntnu.dinner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;


import studentguiden.ntnu.entities.FeedEntry;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.RSSHandler;
import studentguiden.ntnu.misc.Util;
import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class DinnerActivity extends Activity {
	private TextView tv_dinner_title, tv_dinner_description;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dinner);		
		
		tv_dinner_title = (TextView)findViewById(R.id.tv_dinner_title);
		tv_dinner_description = (TextView)findViewById(R.id.tv_dinner_description);
		
		Bundle extras = getIntent().getExtras();
		new DinnerDownloader().execute(extras.getString("URL"));
	}

	private class DinnerDownloader extends AsyncTask<String, Void, Integer> {
		private final int DOWNLOAD_SUCCESSFUL = 0;
		private final int DOWNLOAD_FAILED = 1;
		private final int DOWNLOAD_FAILED_INVALID_URL = 2;
		private String rawText = "";
		private RSSHandler feedHandler;
		private List<FeedEntry> entries;

		@Override
		protected void onPreExecute() {
			feedHandler = new RSSHandler();
		}

		@Override
		protected Integer doInBackground(String... params) {
			entries = feedHandler.getLatestArticles(params[0]+getCurrentDayURI());

			if(entries==null) {
				Util.log("Dinner data download failed");
				return DOWNLOAD_FAILED_INVALID_URL;
			}
			return DOWNLOAD_SUCCESSFUL;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == DOWNLOAD_SUCCESSFUL) {
				Util.log("Dinner data download was successful");
				displayEntryElements();

			}	
		}

		private void displayEntryElements() {
			DinnerActivity.this.tv_dinner_title.setText(Html.fromHtml(entries.get(0).getTitle()));
			DinnerActivity.this.tv_dinner_description.append(Html.fromHtml(entries.get(0).getDescription()));
		}

		private String getCurrentDayURI() {
			Calendar calendar = Calendar.getInstance();
			int weekday = calendar.get(Calendar.DAY_OF_WEEK);

			switch(weekday){

			case 2:
				return "&mo=on";
			case 3:
				return "&ti=on";
			case 4:
				return "&on=on";
			case 5:
				return "&tu=on";
			case 6:
				return "&fr=on";
			case 7:
				return "&sa=on";
			case 1:
				return "&su=on";
			}
			return "";
		}
	}
	}

