package no.studassen.dinner;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.studassen.entities.Canteen;
import no.studassen.entities.Dinner;
import no.studassen.entities.FeedEntry;
import android.content.Context;
import android.content.SharedPreferences;


public class DinnerUtilities {

	private static String[] canteenUrls  = {"http://www.sit.no/rss.ap?thisId=36444", 
		"http://www.sit.no/rss.ap?thisId=36447", 
		"http://www.sit.no/rss.ap?thisId=36441", 
		"http://www.sit.no/rss.ap?thisId=36450", 
		"http://www.sit.no/rss.ap?thisId=37228", 
		"http://www.sit.no/rss.ap?thisId=36453", 
		"http://www.sit.no/rss.ap?thisId=36456",
		"http://www.sit.no/rss.ap?thisId=38753",
		"http://www.sit.no/rss.ap?thisId=38910",
		"http://www.sit.no/rss.ap?thisId=38798"
	};

	private static String[] canteenList = {"Hangaren",  
		"Realfagsbygget", 
		"Dragvoll", 
		"Tyholt", 
		"Øya", 
		"Kalvskinnet", 
		"Moholt", 
		"Ranheimsveien", 
		"Rotvoll", 
		"Dronning Mauds Minne"
	};

	/**
	 * Checks sharedpreferences (phone memory) for selected canteens.
	 * @param context
	 * @return a list of selected canteens
	 */
	public static ArrayList<Canteen> getSelectedCanteens(Context context) {
		ArrayList<Canteen> temp = new ArrayList<Canteen>();
		SharedPreferences prefs = context.getSharedPreferences("studassen", Context.MODE_PRIVATE);

		for (int i = 0; i < canteenList.length; i++) {
			if(prefs.getBoolean(canteenList[i], false)) {
				temp.add(new Canteen(canteenList[i], 0, canteenUrls[i]));
			}
		}

		return temp;
	}

	public static ArrayList<Dinner> parseDinnerText(FeedEntry entry) {
		ArrayList<Dinner> dinners = new ArrayList<Dinner>();

		String[] rawTexts = entry.getDescription().split("<br>");
		for (int i = 0; i < rawTexts.length; i++) {
			String raw = rawTexts[i].trim();
			Dinner dinner = new Dinner();
			Pattern dinnerpattern = Pattern.compile(".*?(?>:)");
			Pattern categoryPattern = Pattern.compile("\\([,LGV]*\\)");
			Pattern pricePattern = Pattern.compile("\\d*?(?>,- kroner)");

			Matcher dinnerMatcher = dinnerpattern.matcher(raw);
			Matcher categoryMatcher = categoryPattern.matcher(raw);
			Matcher priceMatcher = pricePattern.matcher(raw);

			while(dinnerMatcher.find()) {
				if(!dinnerMatcher.group().contains("<b>")) {
					dinner.setContent(dinnerMatcher.group().replaceAll("\\(.+\\)", ""));

					if(categoryMatcher.find()) {
						String category = categoryMatcher.group();
						dinner.setLactoseFree(category.contains("L"));
						dinner.setVegetarian(category.contains("V"));
						dinner.setGlutenFree(category.contains("G"));
					}
				}

				if(priceMatcher.find()) {
					dinner.setPrice(priceMatcher.group());
				}
				dinners.add(dinner);
			}
		}
		return dinners;
	}

}
