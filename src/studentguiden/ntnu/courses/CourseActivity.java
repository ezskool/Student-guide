package studentguiden.ntnu.courses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import studentguiden.ntnu.main.R;
import studentguiden.ntnu.main.Util;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

public class CourseActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);
		
		Bundle extras = getIntent().getExtras();
		
		if(extras !=null)
		{
		new ContentDownloader().execute(extras.getString("courseId"));
		
		}
	}
	
	public void populateCourseDescription(String courseDescription) throws JSONException {
		//		JSONObject jObject = new JSONObject();
//		
//		jObject = new JSONObject(courseDescription); 
//		
//		JSONObject menuObject = jObject.getJSONObject("menu");
//		
//		String attributeId = menuObject.getString("id");
//		System.out.println(attributeId);
//		String attributeValue = menuObject.getString("value");
//		System.out.println(attributeValue);
//		JSONObject popupObject = menuObject.getJSONObject("popup");
//		
//		JSONArray menuitemArray = popupObject.getJSONArray("menuitem");
//		
//		for (int i = 0; i < 3; i++) {
//			System.out.println(menuitemArray.getJSONObject(i)
//					.getString("value").toString());
//			System.out.println(menuitemArray.getJSONObject(i).getString(
//					"onclick").toString());
//		}
//		
	}
	
	private class ContentDownloader extends AsyncTask<String, Void, Integer>{
		
		private String textContent = "";
		private final int DOWNLOAD_SUCCESSFUL = 0;
		private final int DOWNLOAD_FAILED = 1;
		
		@Override
		protected Integer doInBackground(String... params) {
			try {
				URL url = new URL("http://www.ime.ntnu.no/api/course/"+params[0]);
				Util.log("Downloading content from url: "+url.toString());
				InputStream in = url.openStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
				textContent = read(reader);
				in.close();
			}catch(IOException e) {
				Util.log("Content download failed: IOException");
				e.printStackTrace();
				return DOWNLOAD_FAILED;
			}
			return DOWNLOAD_SUCCESSFUL;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
		System.out.println(textContent);
//			try {
////				CourseActivity.this.populateCourseDescription(textContent);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

		/**
		 * Reads all content of a Reader object. Used for reading the content of an URL
		 * @param reader
		 * @return
		 * @throws IOException
		 */
		private String read(Reader reader) throws IOException {
			StringBuilder sb = new StringBuilder();
			int cp;
			while ((cp = reader.read()) != -1) {
				sb.append((char) cp);
			}
			return sb.toString();
		}
	}

}
