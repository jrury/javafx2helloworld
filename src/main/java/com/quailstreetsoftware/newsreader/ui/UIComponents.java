package com.quailstreetsoftware.newsreader.ui;

import java.util.ArrayList;
import java.util.List;

import com.quailstreetsoftware.newsreader.model.RSSItem;
import com.quailstreetsoftware.newsreader.model.RSSSubscription;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.beans.property.ReadOnlyDoubleProperty;
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
	private TableView<RSSItem> table;
	private TrackingMenuBar menuBar = null;
	private ObservableList<RSSItem> rssItems;
	private final WebView browser = new WebView();
    private final WebEngine webEngine = browser.getEngine();
    private ScrollPane scrollPane;
    private TreeView<String> tree;
    private BorderPane viewPane;

	@SuppressWarnings("unchecked")
	public UIComponents(List<RSSSubscription> subscriptions) {
	     
		this.viewPane = new BorderPane();
		this.scrollPane = new ScrollPane();
		viewPane.setCenter(browser);
	    webEngine.loadContent("");

	 
		this.rssItems = FXCollections.observableArrayList(new ArrayList<RSSItem>());
		this.menuBar = new TrackingMenuBar();
		this.menuBar.initialize();
		this.table = new TableView<RSSItem>();
		this.table.setItems(this.rssItems);

		TableColumn<RSSItem,String> titleCol = new TableColumn<RSSItem,String>("Title");
		titleCol.setCellValueFactory(new PropertyValueFactory("title"));
		
		TableColumn<RSSItem,String> dateCol = new TableColumn<RSSItem,String>("Date");
		dateCol.setCellValueFactory(new PropertyValueFactory("pubDate"));
		
		table.getColumns().addAll(titleCol, dateCol);
		table.autosize();
		table.setEditable(Boolean.FALSE);
		table.setOnMouseClicked(new TableRowSelected());
		 
	    TreeItem<String> rootNode = new TreeItem<String>("Subscriptions");
        rootNode.setExpanded(true);
        for(RSSSubscription subscription : subscriptions) {
            TreeItem<String> item = new TreeItem<String>(subscription.getTitle());     
            rootNode.getChildren().add(item);
        }  
        this.tree = new TreeView<String>(rootNode);  
	}

	public Node getMenuBar() {
		return this.menuBar.getMenuBar();
	}

	public void update(List<RSSItem> stories) {
		this.rssItems.addAll(stories);	
	}

	public Node[] getComponents() {
		return new Node[] { this.menuBar.getMenuBar(), this.table, this.viewPane };
	}
	
	public Node getNavigation() {
		return this.tree;
	}
	
	private class TableRowSelected implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent t) {
        	int selectedRecord = table.getSelectionModel().getFocusedIndex();
        	webEngine.loadContent(rssItems.get(selectedRecord).getDescription());
        }
    }

}
