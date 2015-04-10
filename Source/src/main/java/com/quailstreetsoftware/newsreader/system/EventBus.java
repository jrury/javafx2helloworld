package com.quailstreetsoftware.newsreader.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.interfaces.EventListener;

/**
 * Primitive event bus for firing events around.
 * @author Jonathan
 *
 */
public class EventBus {

	List<EventListener> listeners = new ArrayList<EventListener>();

	public void addListener(EventListener toAdd) {
		listeners.add(toAdd);
	}

	public void eventReceived(final NotificationEvent event,
			final HashMap<String, String> arguments) {

		// Notify everybody that may be interested.
		for (EventListener eventListener : listeners) {
			if(eventListener.interested(event)) {
				eventListener.eventOccurred(event, arguments);
			}
		}
	}
}
