package com.quailstreetsoftware.newsreader.ui;

import java.util.HashMap;

import javafx.scene.Node;
import javafx.scene.control.TextArea;

import com.quailstreetsoftware.newsreader.EventBus;
import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.interfaces.EventListener;

public class DebugLog implements EventListener {

	private TextArea textArea;
	
	public DebugLog(EventBus eventBus) {
		this.textArea = new TextArea("DEBUG LOG\n");
		eventBus.addListener(this);
	}

	@Override
	public void eventFired(NotificationEvent event,
			HashMap<String, String> arguments) {
		
		switch (event) {
		case DEBUG_MESSAGE:
			this.textArea.appendText(arguments.get(NotificationParameter.DEBUG_MESSAGE));
			this.textArea.appendText("\n");
			break;
		default:
			break;
	}
		
	}

	@Override
	public Boolean interested(NotificationEvent event) {
		switch (event) {
		case DEBUG_MESSAGE:
			return Boolean.TRUE;
		default:
			return Boolean.FALSE;
	}
	}

	public Node getUI() {
		return this.textArea;
	}

}
