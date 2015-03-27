package com.quailstreetsoftware.newsreader.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RSSSubscription {

	public static String URL = "url";
	public static String TITLE = "title";

	private URL url;
	private String title;
	private Boolean valid;
	private List<RSSItem> stories;
	private CloseableHttpClient httpClient;
	private HttpGet httpGet;
	private ResponseHandler<String> responseHandler;
	private DocumentBuilderFactory factory = DocumentBuilderFactory
			.newInstance();

	public RSSSubscription(final String passedTitle, final String passedUrl) {

		this.stories = new ArrayList<RSSItem>();
		this.httpClient = HttpClients.createDefault();
		this.valid = Boolean.TRUE;
		this.title = passedTitle;
		try {
			this.url = new URL(passedUrl);
		} catch (MalformedURLException e) {
			this.valid = Boolean.FALSE;
		}
		this.httpGet = new HttpGet(passedUrl);
		this.responseHandler = new ResponseHandler<String>() {

			public String handleResponse(final HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException(
							"Unexpected response status: " + status);
				}
			}

		};
		this.refresh();
	}

	public void refresh() {
		try {
			System.out.println("Checking for new stories in ." + this.title);
			String result = httpClient.execute(httpGet, this.responseHandler);
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(result));
			Document document = builder.parse(is);
			NodeList nodes = document.getElementsByTagName("item");
			for (int i = 0; i < nodes.getLength(); i++) {
				RSSItem item = new RSSItem(nodes.item(i));
				if (!this.stories.contains(item)) {
					this.stories.add(item);
					System.out.println("Found a new one.");
					System.out.println(this.title + "---------------/");
				}
			}
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}

	public URL getURL() {
		return this.url;
	}

	public Boolean isValid() {
		return this.valid;
	}

	public String getTitle() {
		return this.title;
	}

	public String handleResponse(final HttpResponse response)
			throws ClientProtocolException, IOException {
		int status = response.getStatusLine().getStatusCode();
		if (status >= 200 && status < 300) {
			HttpEntity entity = response.getEntity();
			return entity != null ? EntityUtils.toString(entity) : null;
		} else {
			throw new ClientProtocolException("Unexpected response status: "
					+ status);
		}
	}

	public List<RSSItem> getStories() {
		return this.stories;
	}

	public static void createDefaultSubscriptions(ArrayList<String> defaults) {

		try {
			File dataDirectory = new File("data");
			dataDirectory.mkdirs();
			File subscriptionFile = new File(dataDirectory, "subscribed");
			PrintWriter writer;
			writer = new PrintWriter(subscriptionFile, "UTF-8");
			for (String defaultSub : defaults) {
				writer.println(defaultSub);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}
