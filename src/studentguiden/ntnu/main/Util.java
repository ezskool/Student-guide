package studentguiden.ntnu.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Håkon Drolsum Røkenes
 * 
 */

public class Util {

	public static void log(String msg) {
		Log.d("student-guide", msg);
	}
	
	public static void displayToastMessage(String msg, Context context) {
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, msg, duration);
		toast.show();
	}
	
	/**
	 * Reads all content from a Reader object. Used for reading the content of an URL
	 * @param reader
	 * @return the string which was read
	 * @throws IOException
	 */
	public static String read(Reader reader) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = reader.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	/**
	 * Downloads raw content from an url
	 * @param url the url to download from
	 * @return String the raw text string containing the content
	 * @throws IOException
	 */
	public static String downloadContent(URL url) throws IOException {
		Util.log("Downloading content from url: "+url.toString());
		InputStream in = url.openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
		String textContent = Util.read(reader);
		in.close();
		
		return textContent;
	}
}
