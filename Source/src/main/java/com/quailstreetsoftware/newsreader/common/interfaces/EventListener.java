package com.quailstreetsoftware.newsreader.common.interfaces;

import java.util.HashMap;

import com.quailstreetsoftware.newsreader.common.NotificationEvent;

public interface EventListener {

	/**
	 * Method to signal that a given event has occurred.
	 * @param event the event that has happened
	 * @param arguments a map of parameters that were supplied by the invoker of the event.
	 */
	public void eventOccurred(final NotificationEvent event, final HashMap<String, Object> arguments);
	
	/**
	 * Method to signal that a particular EventListener is interested in a particular event.
	 * @param event the event to perform the interest check on
	 * @return true if the listener is interested, false otherwise
	 */
	public Boolean interested(final NotificationEvent event);
}
