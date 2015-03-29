package com.quailstreetsoftware.newsreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.interfaces.EventListener;

public class EventBus {

	List<EventListener> listeners = new ArrayList<EventListener>();

	public void addListener(EventListener toAdd) {
		listeners.add(toAdd);
	}

	public void eventReceived(final NotificationEvent event,
			final HashMap<String, String> arguments) {

		// Notify everybody that may be interested.
		for (EventListener eventListener : listeners) {
			eventListener.eventFired(event, arguments);
		}
	}
}
