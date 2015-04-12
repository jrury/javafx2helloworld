package com.quailstreetsoftware.newsreader.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.quailstreetsoftware.newsreader.system.EventBus;
import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.Utility;
import com.quailstreetsoftware.newsreader.common.interfaces.EventListener;

public class ModelContainer implements EventListener, Serializable {

	private static final long serialVersionUID = -6233772875438279910L;
	private Map<String, Subscription> subscriptions;
	private transient EventBus eventBus;

	public ModelContainer(final EventBus eventBus) {

		this.eventBus = eventBus;
		this.subscriptions = new HashMap<String, Subscription>();
		InputStream defaultSubs = this.getClass().getClassLoader()
				.getResourceAsStream("META-INF/defaultSubscriptions");
		ArrayList<String> lines = Utility.readLinesFromStream(defaultSubs);

		lines.forEach(new Consumer<Object>() {
			public void accept(Object line) {
				String[] contents = ((String) line).split("~");
				if (contents.length > 1) {
					subscriptions.put(contents[0], new Subscription(eventBus,
							contents[0], contents[1], contents[0].hashCode() + ""));
				} else {
					// drop it on the floor
				}
			}
		});
	}
	
	public static ModelContainer restore(final EventBus eventBus) {
		if (Files.exists(Paths.get("data", "container.ser"))) {
			ModelContainer mc = null;
			try {
				File dataDirectory = new File("data");
				File serializedFile = new File(dataDirectory, "container.ser");
				FileInputStream fileIn = new FileInputStream(serializedFile);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				mc = (ModelContainer) in.readObject();
				in.close();
				fileIn.close();
			} catch (IOException | ClassNotFoundException i) {
				i.printStackTrace();
			}
			mc.initialize(eventBus);
			return mc;
		}
		return null;
	}

	public List<Article> getStories(final String subscription) {
		if(this.subscriptions.get(subscription) != null) {
			return this.subscriptions.get(subscription).getStories();
		} else {
			return new ArrayList<Article>();
		}
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
	public void eventOccurred(final NotificationEvent event, final HashMap<String, String> arguments) {
		
		switch(event) {
			case REFRESH_SUBSCRIPTION:
				refresh(arguments.get(NotificationParameter.SELECTED_SUBSCRIPTION));
				break;
			case DELETE_SUBSCRIPTION:
				String subscription = arguments.get(NotificationParameter.SELECTED_SUBSCRIPTION);
				if(this.subscriptions.get(subscription) != null) {
					eventBus.fireEvent(NotificationEvent.DEBUG_MESSAGE, Utility.getParameterMap(NotificationParameter.DEBUG_MESSAGE,
							"Deleting " + subscription));
					this.subscriptions.remove(subscription);
					saveSubscriptions();
				}
				break;
			default:
				break;
		}
	}
	
	@Override
	public Boolean interested(NotificationEvent event) {
		switch (event) {
			case REFRESH_SUBSCRIPTION:
			case DELETE_SUBSCRIPTION:
				return Boolean.TRUE;
			default:
				return Boolean.FALSE;
		}

	}
	
	public void saveSubscriptions() {
		try {
			File dataDirectory = new File("data");
			dataDirectory.mkdirs();
			File subscriptionFile = new File(dataDirectory, "container.ser");
			FileOutputStream fileOut = new FileOutputStream(subscriptionFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public void initialize(EventBus eventBus) {
		this.eventBus = eventBus;
		for(Subscription subscription : this.subscriptions.values()) {
			subscription.initialize(eventBus);
		}
	}
}
