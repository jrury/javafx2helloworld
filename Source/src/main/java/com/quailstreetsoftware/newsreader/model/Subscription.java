package com.quailstreetsoftware.newsreader.model;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

import com.quailstreetsoftware.newsreader.system.EventBus;
import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.Utility;

public class Subscription implements Serializable {

	private static final long serialVersionUID = 4575598134385729855L;
	public static String URL = "url";
	public static String TITLE = "title";

	private String id;
	private transient EventBus eventBus;
	private URL url;
	private String title;
	private Boolean valid;
	private List<Article> stories;
	private transient String urlString;
	private transient CloseableHttpClient httpClient;
	private transient HttpGet httpGet;
	private transient DocumentBuilderFactory factory = DocumentBuilderFactory
			.newInstance();

	public Subscription(final EventBus eventBus, final String passedTitle,
			final String passedUrl, final String id) {

		this.id = id;
		this.eventBus = eventBus;
		this.stories = new ArrayList<Article>();
		this.httpClient = HttpClients.createDefault();
		this.valid = Boolean.TRUE;
		this.title = passedTitle;
		try {
			this.url = new URL(passedUrl);
		} catch (MalformedURLException e) {
			this.valid = Boolean.FALSE;
		}
		this.httpGet = new HttpGet(passedUrl);
		this.refresh();
	}

	/**
	 * Probably need to delete this.
	 * 
	 * @param passedTitle
	 * @param passedUrl
	 */
	public Subscription(final String passedTitle, final String passedUrl) {
		this.title = passedTitle;
		this.urlString = passedUrl;
		this.valid = Boolean.TRUE;
		try {
			this.url = new URL(passedUrl);
		} catch (MalformedURLException e) {
			this.valid = Boolean.FALSE;
		}
	}

	public void refresh() {

		if (factory == null) {
			DocumentBuilderFactory.newInstance();
		}

		this.eventBus.fireEvent(NotificationEvent.DEBUG_MESSAGE, Utility
				.getParameterMap(NotificationParameter.DEBUG_MESSAGE,
						"Refreshing subscription " + this.title));

		Task<ArrayList<Article>> task = new Task<ArrayList<Article>>() {

			@Override
			protected ArrayList<Article> call() throws Exception {

				ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
					public String handleResponse(final HttpResponse response)
							throws ClientProtocolException, IOException {
						int status = response.getStatusLine().getStatusCode();
						if (status >= 200 && status < 300) {
							HttpEntity entity = response.getEntity();
							return entity != null ? EntityUtils
									.toString(entity) : null;
						} else {
							throw new ClientProtocolException(
									"Unexpected response status: " + status);
						}
					}
				};
				ArrayList<Article> articles = new ArrayList<Article>();
				String threadName = Thread.currentThread().getName();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						eventBus.fireEvent(NotificationEvent.DEBUG_MESSAGE,
								Utility.getParameterMap(
										NotificationParameter.DEBUG_MESSAGE,
										"Sending http request to "
												+ url.toString(),
										NotificationParameter.THREAD_NAME,
										threadName));
					}
				});
				String result = httpClient.execute(httpGet, responseHandler);
				if (factory == null) {
					factory = DocumentBuilderFactory.newInstance();
				}
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(result));
				Document document = builder.parse(is);
				NodeList nodes = document.getElementsByTagName("item");
				for (int i = 0; i < nodes.getLength(); i++) {
					articles.add(new Article(nodes.item(i)));
				}
				return articles;
			}
		};

		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
						ArrayList<Article> tempArticles = task.getValue();
						Boolean foundNew = Boolean.FALSE;
						for (Article item : tempArticles) {
							if (!stories.contains(item)) {
								stories.add(item);
								foundNew = Boolean.TRUE;
							}
						}
						if (foundNew) {
							eventBus.fireEvent(
									NotificationEvent.REFRESH_SUBSCRIPTION_UI,
									Utility.getParameterMap(
											NotificationParameter.SELECTED_SUBSCRIPTION,
											title));
						}
					}
				});
		Thread updaterThread = new Thread(task);
		updaterThread.setName("UpdaterThread_" + title);
		updaterThread.setDaemon(true);
		updaterThread.start();
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

	public String getId() {
		return this.id;
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

	public List<Article> getStories() {
		return this.stories;
	}

	public void initialize(EventBus passedEventBus) {
		this.eventBus = passedEventBus;
		this.httpClient = HttpClients.createDefault();
		this.valid = Boolean.TRUE;
		this.httpGet = new HttpGet(this.url.toString());
		this.refresh();
	}

	public String getURLString() {
		return this.urlString;
	}

}
