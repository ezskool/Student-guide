package studentguiden.ntnu.misc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import studentguiden.ntnu.entities.FeedEntry;

import android.util.Log;


public class RSSHandler extends DefaultHandler {

	// Feed and Article objects to use for temporary storage
	private FeedEntry currentArticle = new FeedEntry();
	private List<FeedEntry> articleList = new ArrayList<FeedEntry>();

	// Number of articles added so far
	private int articlesAdded = 0;

	// Number of articles to download
	private static final int ARTICLES_LIMIT = 15;
	
	//Current characters being accumulated
	StringBuffer chars = new StringBuffer();

	
	/* 
	 * This method is called everytime a start element is found (an opening XML marker)
	 * here we always reset the characters StringBuffer as we are only currently interested
	 * in the the text values stored at leaf nodes
	 * 
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes atts) {
		chars = new StringBuffer();
	}



	/* 
	 * This method is called everytime an end element is found (a closing XML marker)
	 * here we check what element is being closed, if it is a relevant leaf node that we are
	 * checking, such as Title, then we get the characters we have accumulated in the StringBuffer
	 * and set the current Article's title to the value
	 * 
	 * If this is closing the "Item", it means it is the end of the article, so we add that to the list
	 * and then reset our Article object for the next one on the stream
	 * 
	 * 
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (localName.equalsIgnoreCase("title"))
		{
			currentArticle.setTitle(chars.toString());

		}
		else if (localName.equalsIgnoreCase("description"))
		{
			currentArticle.setDescription(chars.toString());
		}
		else if (localName.equalsIgnoreCase("pubDate"))
		{
			currentArticle.setPubDate(chars.toString());
		}
		else if (localName.equalsIgnoreCase("encoded"))
		{
			currentArticle.setEncodedContent(chars.toString());
		}
		else if (localName.equalsIgnoreCase("item"))
		{

		}
		else if(localName.equalsIgnoreCase("guid")) {
			currentArticle.setGuid(chars.toString());
		}
		else if(localName.equalsIgnoreCase("category")) {
			currentArticle.setCategory(chars.toString());
		}
		else if (localName.equalsIgnoreCase("link"))
		{
			try {
				currentArticle.setUrl(new URL(chars.toString()));
			} catch (MalformedURLException e) {
				
			}

		}




		// Check if looking for article, and if article is complete
		if (localName.equalsIgnoreCase("item")) {

			articleList.add(currentArticle);
			Util.log("adding new article: "+currentArticle.getTitle());
			
			currentArticle = new FeedEntry();

			// Lets check if we've hit our limit on number of articles
			articlesAdded++;
			if (articlesAdded >= ARTICLES_LIMIT)
			{
				throw new SAXException();
			}
		}
	}
	
	



	/* 
	 * This method is called when characters are found in between XML markers, however, there is no
	 * guarante that this will be called at the end of the node, or that it will be called only once
	 * , so we just accumulate these and then deal with them in endElement() to be sure we have all the
	 * text
	 * 
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char ch[], int start, int length) {
		chars.append(new String(ch, start, length));
	}

	



	/**
	 * This is the entry point to the parser and creates the feed to be parsed
	 * 
	 * @param feedUrl
	 * @return
	 */
	public List<FeedEntry> getLatestArticles(String feedUrl) {
		URL url = null;
		try {

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			url = new URL(feedUrl);
			
			xr.setContentHandler(this);
			xr.parse(new InputSource(url.openStream()));


		} catch (IOException e) {
			
		} catch (SAXException e) {
			
		} catch (ParserConfigurationException e) {
		
		}
		
		Util.log("Returning "+articleList.size()+" articles");
		return articleList;
	}

}
