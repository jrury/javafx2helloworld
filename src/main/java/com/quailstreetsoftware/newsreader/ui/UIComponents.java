package com.quailstreetsoftware.newsreader.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.quailstreetsoftware.newsreader.Main;
import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.Utility;
import com.quailstreetsoftware.newsreader.model.RSSItem;
import com.quailstreetsoftware.newsreader.model.RSSSubscription;

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
public class UIComponents {

	private Text text = null;
	private TextArea viewArea = null;
	private TrackingMenuBar menuBar = null;
    private Main controller;
    private NavigationTree tree;
    private RSSItemsList itemList;
    private StoryDisplay storyDisplay;

	public UIComponents(Collection<RSSSubscription> subscriptions, final Main controller) {
	     
		this.controller = controller;
		
		// NAVIGATION TREE
		this.tree = new NavigationTree(subscriptions, this);

		// DISPLAY FOR INDIVIDUAL ITEMS
		this.storyDisplay = new StoryDisplay(this);

	    // MENU BAR
		this.menuBar = new TrackingMenuBar();
		
		// LIST OF RSS STORIES FOR SUBSCRIPTION
		this.itemList = new RSSItemsList(this);
	}

	public Node getMenuBar() {
		return this.menuBar.getMenuBar();
	}

	public void update(List<RSSItem> stories) {
		this.itemList.update(stories);
	}

	public Node[] getComponents() {
		return new Node[] { this.menuBar.getMenuBar(), this.itemList.getDisplay(), this.storyDisplay.getDisplay() };
	}
	
	public Node getNavigation() {
		return this.tree.getTree();
	}

	public void notify(final NotificationEvent event, final HashMap<String, String> arguments) {

		switch(event) {
		case DISPLAY_ITEM:
			this.storyDisplay.loadContent(arguments.get(NotificationParameter.ITEM_CONTENT));
			break;
		default:
			this.controller.notify(event, arguments);
			break;
		}
		
	}

}
