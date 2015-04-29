package com.quailstreetsoftware.newsreader.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.NotificationParameter.ParameterEnum;
import com.quailstreetsoftware.newsreader.common.Utility;
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

	/**
	 * Fire event with a the full map of parameters.
	 * @param event
	 * @param arguments
	 */
	public void fireEvent(final NotificationEvent event, final HashMap<ParameterEnum,
			NotificationParameter> arguments) {

		// Notify everybody that may be interested.
		for (EventListener eventListener : listeners) {
			if(eventListener.interested(event)) {
				eventListener.eventOccurred(event, arguments);
			}
		}
	}
	
	/**
	 * Helper method to pass in a single parameter with a string value.
	 * @param event
	 * @param paramType
	 * @param paramValue
	 */
	public void fireEvent(final NotificationEvent event, final ParameterEnum paramType, final String paramValue) {

		fireEvent(event, Utility.getParameterMap(new NotificationParameter(paramType, paramValue)));
	}
}
