package com.quailstreetsoftware.newsreader.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.Utility;
import com.quailstreetsoftware.newsreader.model.RSSItem;

public class RSSItemsList {

	private ObservableList<RSSItem> rssItems;
	private TableView<RSSItem> table;
	private UIComponents controller;
	
	public RSSItemsList(final UIComponents controller) {
		
		this.table = new TableView<RSSItem>();
		this.controller = controller;
		this.rssItems = FXCollections.observableArrayList(new ArrayList<RSSItem>());
		this.table.setItems(this.rssItems);

		TableColumn<RSSItem,String> titleCol = new TableColumn<RSSItem,String>("Title");
		titleCol.setCellValueFactory(new PropertyValueFactory("title"));
		titleCol.prefWidthProperty().bind(this.table.widthProperty().multiply(0.75));
		
		TableColumn<RSSItem,String> dateCol = new TableColumn<RSSItem,String>("Date");
		dateCol.setCellValueFactory(new PropertyValueFactory("pubDate"));
		dateCol.prefWidthProperty().bind(this.table.widthProperty().divide(4));
		
		table.getColumns().addAll(titleCol, dateCol);
		table.setEditable(Boolean.FALSE);
		table.setOnMouseClicked(new TableRowSelected());
	}
	
	
	private class TableRowSelected implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent t) {
        	int selectedRecord = table.getSelectionModel().getFocusedIndex();
        	if(selectedRecord > -1 && rssItems.size() >= selectedRecord) {
        		controller.notify(NotificationEvent.DISPLAY_ITEM,
        				Utility.getParameterMap(NotificationParameter.ITEM_CONTENT,
        						rssItems.get(selectedRecord).getDescription()));
        	}
        }
    }


	public void update(List<RSSItem> stories) {
		this.rssItems.clear();
		this.rssItems.addAll(stories);
		this.table.autosize();
	}


	public Node getDisplay() {
		return this.table;
	}
}