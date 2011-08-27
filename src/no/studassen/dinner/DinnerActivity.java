package no.studassen.dinner;

import java.io.IOException;
import no.studassen.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import no.studassen.entities.Canteen;
import no.studassen.entities.Dinner;
import no.studassen.entities.FeedEntry;
import no.studassen.misc.RSSHandler;
import no.studassen.misc.Util;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DinnerActivity extends Activity implements OnClickListener {

	private LinearLayout layout_dinner_entry;
	private Bundle savedInstanceState;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dinner);
		this.savedInstanceState = savedInstanceState;

		layout_dinner_entry = (LinearLayout)findViewById(R.id.layout_dinner_entry);
		new DinnerDownloader(this).execute("");
	}


	@Override
	public void onClick(View v) {

	}

	private class DinnerDownloader extends AsyncTask<String, Void, Integer> {
		private final int DOWNLOAD_SUCCESSFUL = 0;
		private final int DOWNLOAD_FAILED = 1;
		private final int DOWNLOAD_FAILED_INVALID_URL = 2;
		private final int PARSING_FAILED = 3;
		private Context context;
		private ArrayList<Canteen> canteens;
		private RSSHandler feedHandler;

		public DinnerDownloader(Context context) {
			this.context = context;
			feedHandler = new RSSHandler();

		}
		@Override
		protected Integer doInBackground(String... params) {
			canteens = DinnerUtilities.getSelectedCanteens(context);

			try {
				for (Canteen canteen : canteens) {
					List<FeedEntry> tempList = feedHandler.getLatestArticles(canteen.getUrl()+getCurrentDayURI());
					for (FeedEntry feedEntry : tempList) {
						canteen.addDinners(DinnerUtilities.parseDinnerText(feedEntry));	
					}

				}
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
				Util.log("Dinner data download failed");
				Util.displayToastMessage(getString(R.string.download_failed_toast),DinnerActivity.this.getApplicationContext());
			}
		}

		private void displayEntryElements() {
			for (Canteen canteen : canteens) {
				TextView canteenName = new TextView(DinnerActivity.this);
				canteenName.setText(canteen.getName());
				DinnerActivity.this.layout_dinner_entry.addView(canteenName);
				Util.log("displaying "+canteen.getDinners().size()+" dinners");
				
				for (Dinner dinner : canteen.getDinners()) {
					TextView dinnerDescription = new TextView(DinnerActivity.this);
					dinnerDescription.setText(dinner.getContent());
					DinnerActivity.this.layout_dinner_entry.addView(dinnerDescription);

					TextView dinnerPrice = new TextView(DinnerActivity.this);
					dinnerPrice.setText(dinner.getPrice());
					DinnerActivity.this.layout_dinner_entry.addView(dinnerPrice);
				}
			}
		}
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
