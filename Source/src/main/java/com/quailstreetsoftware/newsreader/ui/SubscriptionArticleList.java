package com.quailstreetsoftware.newsreader.ui;

import java.util.ArrayList;
import java.util.Collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import com.quailstreetsoftware.newsreader.system.EventBus;
import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.Utility;
import com.quailstreetsoftware.newsreader.model.Article;

public class SubscriptionArticleList {

	private ObservableList<Article> rssItems;
	private TableView<Article> table;
	private EventBus eventBus;
	private String subscription;

	@SuppressWarnings("unchecked")
	public SubscriptionArticleList(final EventBus eventBus,
			final UIComponents controller, final String subscription) {

		this.eventBus = eventBus;
		this.subscription = subscription;
		this.table = new TableView<Article>();
		this.rssItems = FXCollections
				.observableArrayList(new ArrayList<Article>());
		this.table.setItems(this.rssItems);

		TableColumn<Article, String> titleCol = new TableColumn<Article, String>("Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<Article, String>("title"));
		titleCol.prefWidthProperty().bind(this.table.widthProperty().multiply(0.75));

		TableColumn<Article, String> dateCol = new TableColumn<Article, String>("Date");
		dateCol.setCellValueFactory(new PropertyValueFactory<Article, String>("pubDate"));
		dateCol.prefWidthProperty().bind(this.table.widthProperty().divide(4));

		table.getColumns().addAll(titleCol, dateCol);
		table.setEditable(Boolean.FALSE);
		table.setOnMouseClicked(new TableRowSelected());
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		final ContextMenu menu = new ContextMenu();

		final MenuItem deleteAllSelectedItem = new MenuItem(
				"Delete all selected articles");
		deleteAllSelectedItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ObservableList<Article> selectedItems = table.getSelectionModel().getSelectedItems();
				table.getItems().removeAll(selectedItems);
				for(Article selectedItem : selectedItems) {
					eventBus.fireEvent(NotificationEvent.DELETE_ARTICLE, 
							Utility.getParameterMap(NotificationParameter.ID, selectedItem.getGuid(),
									NotificationParameter.SELECTED_SUBSCRIPTION, subscription));
				}
			}
		});

		menu.getItems().addAll(deleteAllSelectedItem);
		table.setContextMenu(menu);
	}

	private class TableRowSelected implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent t) {
			int selectedRecord = table.getSelectionModel().getFocusedIndex();
			if (selectedRecord > -1 && rssItems.size() >= selectedRecord) {
				eventBus.fireEvent(NotificationEvent.DISPLAY_ITEM, Utility
						.getParameterMap(NotificationParameter.ITEM_CONTENT,
								rssItems.get(selectedRecord).getDescription(),
								NotificationParameter.SELECTED_SUBSCRIPTION, subscription));
			}
		}
	}

	public void update(final Collection<Article> collection, final String subscription) {
		this.rssItems.clear();
		this.rssItems.addAll(collection);
		this.table.autosize();
		this.subscription = subscription;
	}

	public Node getDisplay() {
		return this.table;
	}
}
