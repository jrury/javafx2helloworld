package com.quailstreetsoftware.newsreader.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ModelContainer {

	private List<RSSSubscription> subscriptions;

	public ModelContainer() {
		subscriptions = new ArrayList<RSSSubscription>();
		Stream<String> lines;
		try {
			lines = Files.lines(Paths.get("X:/Temp", "subscribed"));
			lines.forEach(new Consumer<Object>() {
				public void accept(Object line) {
					String[] contents = ((String) line).split("~");
					if (contents.length > 1) {
						subscriptions.add(new RSSSubscription(contents[0], contents[1]));
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
	
	public List<RSSItem> getStories() {
		return this.subscriptions.get(0).getStories();
	}

	public void refresh() {
		Stream<RSSSubscription> parallelStream = subscriptions.parallelStream();
		parallelStream.forEach(new Consumer<Object>() {
			@Override
			public void accept(Object subscription) {
				((RSSSubscription)subscription).refresh();
			}
		});
		
	}
}
