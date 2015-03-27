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

import com.quailstreetsoftware.newsreader.common.Utility;

public class ModelContainer {

	private Map<String, RSSSubscription> subscriptions;

	public ModelContainer() {
		this.subscriptions = new HashMap<String, RSSSubscription>();
		Stream<String> lines;
		try {
			if(!Files.exists(Paths.get("data", "subscribed"))) {
				InputStream defaultSubs = this.getClass().getClassLoader().getResourceAsStream("META-INF/defaultSubscriptions");
				RSSSubscription.createDefaultSubscriptions(Utility.readLinesFromStream(defaultSubs));
			}
			lines = Files.lines(Paths.get("data", "subscribed"));
			lines.forEach(new Consumer<Object>() {
				public void accept(Object line) {
					String[] contents = ((String) line).split("~");
					if (contents.length > 1) {
						subscriptions.put(contents[0], new RSSSubscription(contents[0], contents[1]));
						
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

	public List<RSSItem> getStories(final String subscription) {
		if(this.subscriptions.get(subscription) != null) {
			return this.subscriptions.get(subscription).getStories();
		} else {
			return new ArrayList<RSSItem>();
		}
	}

	public void refreshAll() {
		Stream<RSSSubscription> parallelStream = subscriptions.values().parallelStream();
		parallelStream.forEach(new Consumer<Object>() {
			@Override
			public void accept(Object subscription) {
				((RSSSubscription) subscription).refresh();
			}
		});
	}
	
	public void refresh(final String subscription) {
		if(this.subscriptions.get(subscription) != null) {
			this.subscriptions.get(subscription).refresh();
		}
	}

	public Collection<RSSSubscription> getSubscriptions() {
		return this.subscriptions.values();
	}
}
