package studentguiden.ntnu.social;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import studentguiden.ntnu.courses.CourseActivity;
import studentguiden.ntnu.courses.FindCourseActivity;
import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.FeedEntry;
import studentguiden.ntnu.main.R;
import studentguiden.ntnu.misc.RSSHandler;
import studentguiden.ntnu.misc.Util;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class SocialActivity extends ListActivity{
	private TextView tv_social_title;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social);

		new SocialDownloader().execute("http://www.samfundet.no/arrangement/rss");
	}

	private void createListContent(List<FeedEntry> entries) {
		this.setListAdapter(new SocialListArrayAdapter(this, R.layout.list_item, entries));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		FeedEntry entry = (FeedEntry) this.getListAdapter().getItem(position);
		
		Intent intent = new Intent(this, EventActivity.class);
		intent.putExtra("title", entry.getTitle());
		intent.putExtra("description", entry.getDescription());
		intent.putExtra("link", entry.getGuid());
		
		startActivity(intent);
	}
	
	private class SocialDownloader extends AsyncTask<String, Void, Integer> {
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

			try {
				URL url = new URL(params[0]);
				entries = feedHandler.getLatestArticles(params[0]);

			}catch(MalformedURLException e){
				Util.log("Social data download failed: invalid URL");
				e.printStackTrace();
				return DOWNLOAD_FAILED_INVALID_URL;
			}
			return DOWNLOAD_SUCCESSFUL;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == DOWNLOAD_SUCCESSFUL) {
				Util.log("Social data download was successful");
				displayEntryElements();

			}	
		}
		private void displayEntryElements() {

			SocialActivity.this.createListContent(entries);
		}
	}
}
