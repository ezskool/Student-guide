package studentguiden.ntnu.main;

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
}
