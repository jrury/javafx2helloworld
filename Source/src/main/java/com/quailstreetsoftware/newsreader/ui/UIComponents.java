package com.quailstreetsoftware.newsreader.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.quailstreetsoftware.newsreader.system.EventBus;
import com.quailstreetsoftware.newsreader.Main;
import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.Utility;
import com.quailstreetsoftware.newsreader.common.interfaces.EventListener;
import com.quailstreetsoftware.newsreader.model.Article;
import com.quailstreetsoftware.newsreader.model.Subscription;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;

@SuppressWarnings("unused")
public class UIComponents implements EventListener {

	private ApplicationMenuBar menuBar = null;
    private Main controller;
    private NavigationTree tree;
    private SubscriptionArticleList itemList;
    private StoryDisplay storyDisplay;
    private EventBus eventBus;
    private DebugLog debugLog;

	public UIComponents(final EventBus eventBus, final Collection<Subscription> subscriptions, final Main controller) {
	     
		this.debugLog = new DebugLog(eventBus);
		this.eventBus = eventBus;
		this.controller = controller;
		
		// NAVIGATION TREE
		this.tree = new NavigationTree(eventBus, subscriptions, this);

		// DISPLAY FOR INDIVIDUAL ITEMS
		this.storyDisplay = new StoryDisplay(this);

	    // MENU BAR
		this.menuBar = new ApplicationMenuBar(eventBus);
		
		// LIST OF RSS STORIES FOR SUBSCRIPTION
		this.itemList = new SubscriptionArticleList(eventBus, this, null);
	}

	public Node getMenuBar() {
		return this.menuBar.getMenuBar();
	}

	public void update(Collection<Article> collection, final String subscription) {
		this.itemList.update(collection, subscription);
	}

	public Node[] getComponents() {
		return new Node[] { this.menuBar.getMenuBar(), this.itemList.getDisplay(), this.storyDisplay.getDisplay() };
	}
	
	public Node getNavigation() {
		return this.tree.getTree();
	}

	@Override
	public void eventOccurred(NotificationEvent event,
			HashMap<String, String> arguments) {
		
		switch(event) {
			case DISPLAY_ITEM:
				this.storyDisplay.loadContent(arguments.get(NotificationParameter.ITEM_CONTENT));
				break;
			case ADD_SUBSCRIPTION_UI:
				this.tree.addSubscription(arguments.get(NotificationParameter.SELECTED_SUBSCRIPTION),
						arguments.get(NotificationParameter.SUBSCRIPTION_URL));
			default:
				break;
		}
		
	}
	
	@Override
	public Boolean interested(NotificationEvent event) {
		switch (event) {
			case DISPLAY_ITEM:
			case ADD_SUBSCRIPTION_UI:
				return Boolean.TRUE;
			default:
				return Boolean.FALSE;
		}

	}

	public Node getDebugMenu() {
		return this.debugLog.getUI();
	}

}
