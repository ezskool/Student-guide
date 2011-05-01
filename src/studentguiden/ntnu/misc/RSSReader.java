//package studentguiden.ntnu.misc;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.text.Html;
//
//import studentguiden.ntnu.entities.FeedEntry;
//
//public class RSSReader {
//
//	private final static String BOLD_OPEN = "<B>";
//	private final static String BOLD_CLOSE = "</B>";
//	private final static String BREAK = "<BR>";
//	private final static String ITALIC_OPEN = "<I>";
//	private final static String ITALIC_CLOSE = "</I>";
//	private final static String SMALL_OPEN = "<SMALL>";
//	private final static String SMALL_CLOSE = "</SMALL>";
//	
//	/**
//	 * This method defines a feed URL and then calles our SAX Handler to read the article list
//	 * from the stream
//	 * @param String the feed url
//	 * @return List<JSONObject> - suitable for the List View activity
//	 */
//	public static List<JSONObject> getLatestRssFeed(String feed){
//		
//		RSSHandler rh = new RSSHandler();
//		List<FeedEntry> articles =  rh.getLatestArticles(feed);
//		return fillData(articles);
//	}
//	
//
//	/**
//	 * This method takes a list of Articl				// TODO Auto-generated catch blocke objects and converts them in to the 
//	 * correct JSON format so the info can be processed by our list view
//	 * 
//	 * @param feedentries - list<FeedEntry>
//	 * @return List<JSONObject> - suitable for the List View activity
//	 */
//	private static List<JSONObject> fillData(List<FeedEntry> articles) {
//
//        List<JSONObject> items = new ArrayList<JSONObject>();
//        for (FeedEntry article : articles) {
//            JSONObject current = new JSONObject();
//            try {
//            	buildJsonObject(article, current);
//			} catch (JSONException e) {
////				Log.e("RSS ERROR", "Error creating JSON Object from RSS feed");
//			}
//			items.add(current);
//        }
//        
//        return items;
//	}
//
//
//	/**
//	 * This method takes a single Article Object and converts it in to a single JSON object
//	 * including some additional HTML formating so they can be displayed nicely
//	 * 
//	 * @param article
//	 * @param current
//	 * @throws JSONException
//	 */
//	private static void buildJsonObject(FeedEntry article, JSONObject current) throws JSONException {
//		String title = article.getTitle();
//		String description = article.getDescription();
//		String date = article.getPubDate();
//		
//		StringBuffer sb = new StringBuffer();
//		sb.append(BOLD_OPEN).append(title).append(BOLD_CLOSE);
//		sb.append(BREAK);
//		sb.append(description);
//		sb.append(BREAK);
//		sb.append(SMALL_OPEN).append(ITALIC_OPEN).append(date).append(ITALIC_CLOSE).append(SMALL_CLOSE);
//		
//		current.put("text", Html.fromHtml(sb.toString()));
//	}
//}
