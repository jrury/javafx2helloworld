package com.quailstreetsoftware.newsreader.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import com.quailstreetsoftware.newsreader.EventBus;
import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.Utility;
import com.quailstreetsoftware.newsreader.model.Subscription;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.Callback;

/**
 * UI component that will display in a tree the list of news feeds that the user is subscribed to.
 * @author Jonathan
 */
public class NavigationTree {

	private TreeView<String> tree;
	private HashMap<String, String> subscriptionTitles;
	private EventBus eventBus;
	private TreeItem<String> root;

	public NavigationTree(final EventBus eventBus, final Collection<Subscription> subscriptions,
			final UIComponents controller) {

		this.eventBus = eventBus;
		this.subscriptionTitles = new HashMap<String, String>();
		root = new TreeItem<String>("Subscriptions");
		root.setExpanded(true);
		for (Subscription subscription : subscriptions) {
			TreeItem<String> item = new TreeItem<String>(subscription.getTitle());
			root.getChildren().add(item);
			this.subscriptionTitles.put(subscription.getTitle(), subscription.getURL().toString());
		}

		this.tree = new TreeView<String>(root);
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
	        
	        MenuItem deleteItem = new MenuItem("Delete");
	        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                	Alert alert = new Alert(AlertType.CONFIRMATION);
                	alert.setTitle("Confirmation Dialog");
                	alert.setHeaderText("CONFIRM DELETION!");
                	alert.setContentText("Are you sure you want to delete this subscription?");

                	Optional<ButtonType> result = alert.showAndWait();
                	if (result.get() == ButtonType.OK){
                    	eventBus.eventReceived(NotificationEvent.DELETE_SUBSCRIPTION,
                    			Utility.getParameterMap(NotificationParameter.SELECTED_SUBSCRIPTION,
                						getTreeItem().getValue()));   
                	} else {
                	    // don't delete it...
                	}                                   
                }
	        });
	        
	        MenuItem refreshItem = new MenuItem("Refresh");
	        refreshItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                	eventBus.eventReceived(NotificationEvent.REFRESH_SUBSCRIPTION,
                			Utility.getParameterMap(NotificationParameter.SELECTED_SUBSCRIPTION,
            						getTreeItem().getValue()));                                      
                }
	        });
	        
	        contextMenu.getItems().addAll(refreshItem, deleteItem);
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
