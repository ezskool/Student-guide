package no.studassen.bus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;


public class BusFinder {
	private URI uri;
	private URLConnection connection;
	private Scanner scanner;


	public BusFinder() throws URISyntaxException {
		uri = new URI("http", "m.atb.no", "/xmlhttprequest.php?service=routeplannerOracle.getOracleAnswer&question=", null);
	}

	/**
	 * Sends a HTTP GET request to the ATB server, and retrieves the bus departures
	 * @param question
	 * @return the answer (bus departures) from the bus oracle
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String askOracle(String question) throws MalformedURLException, IOException{

		String URL = getCompleteURL(question);

		connection = new URL(URL).openConnection();
		scanner = new Scanner(connection.getInputStream());

		if (scanner != null) {
			scanner.useDelimiter("$");
			String content = scanner.next();
			return content.trim();
		}
		return null;
	}

	/**
	 * Makes sure the url has the proper format, and adds the question as parameters
	 * @param question
	 * @return the complete url
	 */
	private String getCompleteURL(String question) {
		String temp = uri.toString().replace("%3F", "?") + question.replace("%3F", "?");
		temp = temp.replaceAll("\\s+", "%20");

		return temp;
	}
}