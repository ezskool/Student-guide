package studentguiden.ntnu.social;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import studentguiden.ntnu.courses.CourseActivity;
import studentguiden.ntnu.courses.FindCourseActivity;
import studentguiden.ntnu.entities.Course;
import studentguiden.ntnu.entities.Event;
import studentguiden.ntnu.entities.FeedEntry;
import studentguiden.ntnu.main.Globals;
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
	private TextView tv_list_title;
	private ArrayList<Event> currentEventList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		tv_list_title = (TextView)findViewById(R.id.tv_list_title);
		tv_list_title.setText(getString(R.string.tv_social_title));

		new EventDownloader().execute("http://www.samfundet.no/arrangement/rss");
		new SongkickDownloader().execute("http://api.songkick.com/api/3.0/events.json?apikey=34K3IoUdXDTHcSz3&location=sk:31425");
	}

	private void createListContent(ArrayList<Event> entries) {
		this.setListAdapter(new SocialListArrayAdapter(this, R.layout.list_item, entries));
	}
	
	public void addEventList(ArrayList<Event> entries) {
		if(currentEventList == null) {
			currentEventList = new ArrayList<Event>();
		}
		for (Event event : entries) {
			currentEventList.add(event);
		}
		
		createListContent(currentEventList);
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Event event = (Event) this.getListAdapter().getItem(position);

		Intent intent = new Intent(this, EventActivity.class);
		intent.putExtra("title", event.getTitle());
		intent.putExtra("description", event.getDescription());
		intent.putExtra("link", event.getGuid());
		intent.putExtra("bannerResource", event.getBannerResource());

		startActivity(intent);
	}

	private class EventDownloader extends AsyncTask<String, Void, Integer> {
		private final int DOWNLOAD_SUCCESSFUL = 0;
		private final int DOWNLOAD_FAILED = 1;
		private final int DOWNLOAD_FAILED_INVALID_URL = 2;
		private String rawText = "";
		private RSSHandler feedHandler;
		private ArrayList<Event> eventList;

		@Override
		protected void onPreExecute() {
			feedHandler = new RSSHandler();
		}

		@Override
		protected Integer doInBackground(String... params) {
			List<FeedEntry> entries = feedHandler.getLatestArticles(params[0]);
			eventList = getEvents(entries);
			if(entries == null){
				Util.log("Social data download failed");
				return DOWNLOAD_FAILED;
			}
			return DOWNLOAD_SUCCESSFUL;
		}
		
		private ArrayList<Event> getEvents(List<FeedEntry> entries) {
			ArrayList<Event> temp = new ArrayList<Event>();
			
			for (FeedEntry entry : entries) {
				temp.add(new Event(entry.getTitle(), entry.getDescription(), entry.getCategory(), entry.getGuid(), R.drawable.samfundet_banner, R.drawable.ic_samfundet));
			}
			return temp;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == DOWNLOAD_SUCCESSFUL) {
				Util.log("Samfunnet data download was successful");
				SocialActivity.this.addEventList(eventList);
			}	
		}
	}

	private class SongkickDownloader extends AsyncTask<String, Void, Integer> {
		private final int DOWNLOAD_FAILED_INVALID_URL = 0;
		private final int DOWNLOAD_FAILED_NETWORK_ERROR =1;
		private final int DOWNLOAD_SUCCESSFUL = 2;
		private final int PARSING_FAILED = 3;
		private String rawData;
		private ArrayList<Event> eventList = new ArrayList<Event>();
		

		@Override
		protected Integer doInBackground(String... params) {
			try {
				URL url = new URL(params[0]);
				rawData = Util.downloadContent(url);

				JSONArray eventJsonArray= new JSONObject(rawData).getJSONObject("resultsPage").getJSONObject("results").getJSONArray("event");
				

				int n = eventJsonArray.length();
				for (int i = 0; i < n; i++) {
					JSONObject temp = eventJsonArray.getJSONObject(i);
					eventList.add(new Event(temp.getString("displayName"), "", temp.getString("type"), temp.getString("uri"), R.drawable.songkick_banner, R.drawable.ic_songkick));
				}
				
			}catch(MalformedURLException e) {
				Util.log("Songkick download failed: MalformedURLException"+e.getMessage());
				return DOWNLOAD_FAILED_INVALID_URL;
			}catch(IOException e) {
				Util.log("Songkick download failed: IOEXception"+e.getMessage());
				return DOWNLOAD_FAILED_NETWORK_ERROR;
			} catch (JSONException e) {
				Util.log("Songkick parsing failed: JSONException" +e.getMessage());
				return PARSING_FAILED;
			}
			return DOWNLOAD_SUCCESSFUL;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			if(result==DOWNLOAD_SUCCESSFUL) {
				Util.log("Songkick data was successfully downloaded");
				SocialActivity.this.addEventList(eventList);
			}
		}

	}
}
