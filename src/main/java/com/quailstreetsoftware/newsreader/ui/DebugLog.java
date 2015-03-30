package com.quailstreetsoftware.newsreader.ui;


import java.util.HashMap;

import org.apache.log4j.Logger;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import com.quailstreetsoftware.newsreader.EventBus;
import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.interfaces.EventListener;

public class DebugLog implements EventListener {

	private ScrollPane textContainer = new ScrollPane();
	private TextFlow textFlow = new TextFlow();
	private static final Logger logger = Logger.getLogger(DebugLog.class);
	
	public DebugLog(EventBus eventBus) {
		this.textFlow = new TextFlow();
		this.textFlow.setMaxHeight(20);
		eventBus.addListener(this);
	    Text text = new Text("Debug Log:\n");
	    text.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
	    this.textFlow.getChildren().add(text);
	    textContainer.setContent(textFlow);
	}

	@Override
	public void eventFired(NotificationEvent event,
			HashMap<String, String> arguments) {
		
		switch (event) {
		case DEBUG_MESSAGE:
		    Text text = new Text(arguments.get(NotificationParameter.DEBUG_MESSAGE) + "\n");
		    text.setFont(Font.font("Helvetica", FontWeight.NORMAL, 12));
			this.textFlow.getChildren().add(text);
			textFlow.layout();
            textContainer.layout();
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
		return this.textContainer;
	}

}
