package com.quailstreetsoftware.newsreader.model;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.quailstreetsoftware.newsreader.EventBus;
import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.Utility;
import com.quailstreetsoftware.newsreader.common.interfaces.EventListener;

public class ModelContainer implements EventListener {

	private Map<String, Subscription> subscriptions;

	public ModelContainer(final EventBus eventBus) {

		this.subscriptions = new HashMap<String, Subscription>();
		Stream<String> lines;
		try {
			if(!Files.exists(Paths.get("data", "subscribed"))) {
				InputStream defaultSubs = this.getClass().getClassLoader().getResourceAsStream("META-INF/defaultSubscriptions");
				Subscription.createDefaultSubscriptions(Utility.readLinesFromStream(defaultSubs));
			}
			lines = Files.lines(Paths.get("data", "subscribed"));
			lines.forEach(new Consumer<Object>() {
				public void accept(Object line) {
					String[] contents = ((String) line).split("~");
					if (contents.length > 1) {
						subscriptions.put(contents[0], new Subscription(eventBus, contents[0], contents[1]));
						
					} else {
						// drop it on the floor
					}
				}
			});
			lines.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Article> getStories(final String subscription) {
		if(this.subscriptions.get(subscription) != null) {
			return this.subscriptions.get(subscription).getStories();
		} else {
			return new ArrayList<Article>();
		}
	}

	/**
	 * Refresh all the subscriptions using parallel streams.
	 */
	public void refreshAll() {
		Stream<Subscription> parallelStream = subscriptions.values().parallelStream();
		parallelStream.forEach(new Consumer<Object>() {
			@Override
			public void accept(Object subscription) {
				((Subscription) subscription).refresh();
			}
		});
	}
	
	/**
	 * Refresh the subscription with the title passed in.
	 * @param subscriptionTitle the title of the subscription to refresh.
	 */
	public void refresh(final String subscriptionTitle) {
		if(this.subscriptions.get(subscriptionTitle) != null) {
			this.subscriptions.get(subscriptionTitle).refresh();
		}
	}

	public Collection<Subscription> getSubscriptions() {
		return this.subscriptions.values();
	}

	@Override
	public void eventFired(final NotificationEvent event, final HashMap<String, String> arguments) {
		
		switch(event) {
			case REFRESH_SUBSCRIPTION:
				refresh(arguments.get(NotificationParameter.SELECTED_SUBSCRIPTION));
				break;
			default:
				break;
		}
	}
	
	@Override
	public Boolean interested(NotificationEvent event) {
		switch (event) {
			case REFRESH_SUBSCRIPTION:
				return Boolean.TRUE;
			default:
				return Boolean.FALSE;
		}

	}
}
