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
import java.util.Map;
import java.util.function.Consumer;

import com.quailstreetsoftware.newsreader.system.EventBus;
import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.NotificationParameter.ParameterEnum;
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
					Subscription newSub = new Subscription(eventBus, contents[0], contents[1]);
					subscriptions.put(newSub.getId(), newSub);
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

	public Collection<Article> getStories(final String subscription) {
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
	public void eventOccurred(final NotificationEvent event, 
			final HashMap<ParameterEnum, NotificationParameter> arguments) {
		
		switch(event) {
			case REFRESH_SUBSCRIPTION:
				refresh(arguments.get(ParameterEnum.SUBSCRIPTION_ID).getStringValue());
				break;
			case DELETE_SUBSCRIPTION:
				String subscriptionId = arguments.get(ParameterEnum.SUBSCRIPTION_ID).getStringValue();
				if(this.subscriptions.get(subscriptionId) != null) {
					eventBus.fireEvent(NotificationEvent.DEBUG_MESSAGE, ParameterEnum.DEBUG_MESSAGE,
							"Deleting " + subscriptionId);
					this.subscriptions.remove(subscriptionId);
					saveSubscriptions();
				}
				break;
			case DELETE_ARTICLE:
				Subscription subscriptionToDeleteFrom = this.subscriptions.get(arguments.get(ParameterEnum.SUBSCRIPTION_ID));
				if(subscriptionToDeleteFrom != null && arguments.get(ParameterEnum.ARTICLE_ID) != null) {
					subscriptionToDeleteFrom.deleteArticle(arguments.get(ParameterEnum.ARTICLE_ID).getStringValue());
				}
				break;
			case NEW_SUBSCRIPTION:
				String subscriptionTitle = arguments.get(ParameterEnum.SUBSCRIPTION_TITLE).getStringValue();
				String subscriptionUrl = arguments.get(ParameterEnum.SUBSCRIPTION_URL).getStringValue();
				Subscription temp = new Subscription(eventBus, subscriptionTitle, subscriptionUrl);
				if(temp.isValid()) {
					subscriptions.put(temp.getId(), temp);
					saveSubscriptions();
					refresh(subscriptionTitle);
					eventBus.fireEvent(NotificationEvent.ADD_SUBSCRIPTION_UI,
							Utility.getParameterMap(new NotificationParameter(
									ParameterEnum.SUBSCRIPTION, temp)));
				}
				break;
			case DISPLAY_ITEM:
				String articleId = arguments.get(ParameterEnum.ARTICLE_ID).getStringValue();
				String displaySubscriptionId = arguments.get(ParameterEnum.SUBSCRIPTION_ID).getStringValue();
				subscriptions.get(displaySubscriptionId).markRead(articleId);
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
			case NEW_SUBSCRIPTION:
			case DELETE_ARTICLE:
			case DISPLAY_ITEM:
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
