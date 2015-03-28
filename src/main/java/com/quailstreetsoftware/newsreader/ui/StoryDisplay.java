package com.quailstreetsoftware.newsreader.ui;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class StoryDisplay {

    private BorderPane viewPane;
	private final WebView browser;
    private final WebEngine webEngine;
    
	public StoryDisplay(UIComponents uiComponents) {

		this.browser = new WebView();
		this.webEngine = browser.getEngine();
		this.viewPane = new BorderPane();
		viewPane.setCenter(browser);
	    webEngine.loadContent("");
	}

	public Node getDisplay() {
		return this.viewPane;
	}

	public void loadContent(String htmlContent) {
		this.webEngine.loadContent(htmlContent);
		
	}

}
