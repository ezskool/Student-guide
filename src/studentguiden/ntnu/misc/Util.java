package studentguiden.ntnu.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import studentguiden.ntnu.storage.entities.Lecture;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
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
	
	/**
	 * Displays  a toast message on the screen
	 * @param msg - the message to display
	 * @param context - the context (Activity) it should display for
	 */
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
	
	public static boolean isLectureToday(Lecture lecture) {
		if(lecture.getDayNumber()+2==(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) && isLectureThisWeek(lecture) ) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isLectureThisWeek(Lecture lecture) {
		String[] weeks = lecture.retrieveWeeks();
		for (int i = 0; i < weeks.length; i++) {
			if(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) == Integer.parseInt(weeks[i])) {
				return true;
			}
		}
		return false;
	}	

	public static String getDate(String day) {

		int dayNumber = 0;
		
		if(day.equalsIgnoreCase("mandag")) {
			dayNumber = 2;
		}else if(day.equalsIgnoreCase("tirsdag")) {
			dayNumber = 3;
		}else if(day.equalsIgnoreCase("onsdag")) {
			dayNumber = 4;
		}else if(day.equalsIgnoreCase("torsdag")) {
			dayNumber = 5;
		}else if(day.equalsIgnoreCase("fredag")) {
			dayNumber  = 6;
		}else if(day.equalsIgnoreCase("lørdag")) {
			dayNumber = 7;
		}else if(day.equalsIgnoreCase("søndag")) {
			dayNumber = 1;
		}
		
		Calendar cal = Calendar.getInstance();
		int currentDay = cal.get(Calendar.DAY_OF_WEEK);
		if(currentDay != dayNumber) {
			int days = (dayNumber - currentDay + 2) % 7;
			cal.set(Calendar.DAY_OF_YEAR, days);
		}
		
		return cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasCourseDataExpired(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("studassen", Context.MODE_PRIVATE);
		String expirationDate = prefs.getString("CourseDataExpirationDate", "");
		if(expirationDate == "" || expirationDate == null) {
			return true;
		}else {
			return hasDateExpired(expirationDate);
		}
	}
	
	/** Compares a date string to current date, to check if it has expired or not. The date string must conform with ISO8601 Java calendar system
	 * example string: 2011-06-24T14:25:22+02:00
	 * 
	 * @param expirationDate
	 * @return
	 */
	public static boolean hasDateExpired(String expirationDate) {
		DateTimeFormatter parser = ISODateTimeFormat.dateTimeNoMillis();
		
		DateTime expDate = parser.parseDateTime(expirationDate);
		return expDate.isBeforeNow();
	}
}
