package com.quailstreetsoftware.newsreader.ui;

import java.util.Collection;
import java.util.HashMap;

import com.quailstreetsoftware.newsreader.EventBus;
import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.Utility;
import com.quailstreetsoftware.newsreader.model.Subscription;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.Callback;

public class NavigationTree {

	private TreeView<String> tree;
	private HashMap<String, String> subscriptionTitles;
	private EventBus eventBus;

	public NavigationTree(final EventBus eventBus, final Collection<Subscription> subscriptions,
			final UIComponents controller) {

		this.eventBus = eventBus;
		this.subscriptionTitles = new HashMap<String, String>();
		TreeItem<String> rootNode = new TreeItem<String>("Subscriptions");
		rootNode.setExpanded(true);
		for (Subscription subscription : subscriptions) {
			TreeItem<String> item = new TreeItem<String>(
					subscription.getTitle());
			rootNode.getChildren().add(item);
			this.subscriptionTitles.put(subscription.getTitle(), subscription
					.getURL().toString());
		}

		this.tree = new TreeView<String>(rootNode);
		this.tree.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<TreeItem<String>>() {

					@Override
					public void changed(
							ObservableValue<? extends TreeItem<String>> observable,
							TreeItem<String> previouslySelected,
							TreeItem<String> currentlySelected) {

						if (subscriptionTitles.containsKey(currentlySelected.getValue())) {
							eventBus.eventReceived(NotificationEvent.CHANGED_SELECTED_SOURCE,
								Utility.getParameterMap(NotificationParameter.SELECTED_SUBSCRIPTION,
										currentlySelected.getValue()));
						}
					}
				});
		tree.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {

		    @Override
		    public TreeCell<String> call(TreeView<String> selected) {
		        return new NavigationTreeCell(selected);
		    }
		});
		tree.setBorder(null);
	}

	public TreeView<String> getTree() {
		return this.tree;
	}

	private class NavigationTreeCell extends TextFieldTreeCell<String> {

		private ContextMenu contextMenu;

	    public NavigationTreeCell(TreeView<String> selected) {
	        contextMenu = new ContextMenu();
	        contextMenu.setId(selected.getId());
	        MenuItem menuItem = new MenuItem("Refresh");
	        contextMenu.getItems().add(menuItem);
	        contextMenu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                	eventBus.eventReceived(NotificationEvent.REFRESH_SUBSCRIPTION,
                			Utility.getParameterMap(NotificationParameter.SELECTED_SUBSCRIPTION,
            						getTreeItem().getValue()));                                      
                }
            });
	    }

	    @Override
	    public void updateItem(String item, boolean empty) {

	        super.updateItem(item, empty);

	        if (!empty && getTreeItem().getParent() != null) {
	            setContextMenu(contextMenu);
	        }
	    }
	}
}
