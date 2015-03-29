package com.quailstreetsoftware.newsreader.common.interfaces;

import java.util.HashMap;

import com.quailstreetsoftware.newsreader.common.NotificationEvent;

public interface EventListener {

	public void eventFired(final NotificationEvent event, final HashMap<String, String> arguments);
	
	public Boolean interested(final NotificationEvent event);
}
