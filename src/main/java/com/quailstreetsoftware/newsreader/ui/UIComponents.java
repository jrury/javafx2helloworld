package com.quailstreetsoftware.newsreader.ui;

import java.util.ArrayList;
import java.util.List;

import com.quailstreetsoftware.newsreader.model.RSSItem;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
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

	public UIComponents() {
		
		this.scrollPane = new ScrollPane();
		this.viewArea = new TextArea();
		scrollPane.setContent(browser);
	    webEngine.loadContent("<b>asdf</b>");
		this.rssItems = FXCollections.observableArrayList(new ArrayList<RSSItem>());
		this.menuBar = new TrackingMenuBar();
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
	}

	public Node getMenuBar() {
		return this.menuBar.getMenuBar();
	}

	public void update(List<RSSItem> stories) {
		this.rssItems.addAll(stories);	
	}

	public Node[] getComponents() {
		return new Node[] { this.table, this.scrollPane };
	}
	
	private class TableRowSelected implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent t) {
        	int selectedRecord = table.getSelectionModel().getFocusedIndex();
        	webEngine.loadContent(rssItems.get(selectedRecord).getDescription());
        }
    }

}
