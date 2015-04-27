package com.quailstreetsoftware.newsreader.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

	@SuppressWarnings("unchecked")
	public SubscriptionArticleList(final EventBus eventBus,
			final UIComponents controller) {

		this.eventBus = eventBus;
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
		table.addEventHandler(KeyEvent.KEY_PRESSED,
				new EventHandler<KeyEvent>() {
					public void handle(final KeyEvent keyEvent) {
						if (keyEvent.getCode() == KeyCode.DELETE) {
							ObservableList<Article> selectedItems = table.getSelectionModel().getSelectedItems();
							for(Article selectedItem : selectedItems) {
								HashMap<String, Object> parameters = Utility.getParameterMap(NotificationParameter.ID, selectedItem.getGuid(),
										NotificationParameter.SELECTED_SUBSCRIPTION, selectedItem.getSubscription());
								eventBus.fireEvent(NotificationEvent.DELETE_ARTICLE, parameters);
							}
							table.getItems().removeAll(selectedItems);
						}
					}
				});

		final ContextMenu menu = new ContextMenu();

		final MenuItem deleteAllSelectedItem = new MenuItem(
				"Delete all selected articles");
		deleteAllSelectedItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ObservableList<Article> selectedItems = table.getSelectionModel().getSelectedItems();
				for(Article selectedItem : selectedItems) {
					HashMap<String, Object> parameters = Utility.getParameterMap(NotificationParameter.ID, selectedItem.getGuid(),
							NotificationParameter.SELECTED_SUBSCRIPTION, selectedItem.getSubscription());
					eventBus.fireEvent(NotificationEvent.DELETE_ARTICLE, parameters);
				}
				table.getItems().removeAll(selectedItems);
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
				eventBus.fireEvent(NotificationEvent.DISPLAY_ITEM, Utility.getParameterMap(NotificationParameter.ITEM_CONTENT,
						rssItems.get(selectedRecord).getDescription()));
			}
		}
	}

	public void update(final Collection<Article> collection) {
		this.rssItems.clear();
		this.rssItems.addAll(collection);
		this.table.autosize();
	}

	public Node getDisplay() {
		return this.table;
	}
}
