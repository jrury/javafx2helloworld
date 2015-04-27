package com.quailstreetsoftware.newsreader.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import com.quailstreetsoftware.newsreader.system.EventBus;
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

	private TreeView<Subscription> tree;
	private HashMap<String, Subscription> subscriptionTitles;
	private EventBus eventBus;
	private TreeItem<Subscription> root;

	public NavigationTree(final EventBus eventBus, final Collection<Subscription> subscriptions,
			final UIComponents controller) {

		this.eventBus = eventBus;
		this.subscriptionTitles = new HashMap<String, Subscription>();
		root = new TreeItem<Subscription>(new Subscription("Subscriptions"));
		root.setExpanded(true);
		for (Subscription subscription : subscriptions) {
			TreeItem<Subscription> item = new TreeItem<Subscription>(subscription);
			root.getChildren().add(item);
			this.subscriptionTitles.put(subscription.getTitle(), subscription);
		}

		this.tree = new TreeView<Subscription>(root);
		this.tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Subscription>>() {

					@Override
					public void changed(
							ObservableValue<? extends TreeItem<Subscription>> observable,
							TreeItem<Subscription> previouslySelected,
							TreeItem<Subscription> currentlySelected) {

						if (subscriptionTitles.containsKey(currentlySelected.getValue().getTitle())) {
							eventBus.fireEvent(NotificationEvent.CHANGED_SELECTED_SOURCE,
								Utility.getParameterMap(NotificationParameter.SELECTED_SUBSCRIPTION,
										currentlySelected.getValue().getTitle()));
						}
					}
				});
		tree.setCellFactory(new Callback<TreeView<Subscription>, TreeCell<Subscription>>() {

		    @Override
		    public TreeCell<Subscription> call(TreeView<Subscription> selected) {
		        return new NavigationTreeCell(selected);
		    }
		});
		tree.setBorder(null);
	}

	public TreeView<Subscription> getTree() {
		return this.tree;
	}
	
	public void deleteNode(final TreeItem<Subscription> treeItem) {
		this.tree.getRoot().getChildren().remove(treeItem);
	}
	
	public void addSubscription(final Subscription newSubscription) {
		this.subscriptionTitles.put(newSubscription.getTitle(), newSubscription);
		TreeItem<Subscription> item = new TreeItem<Subscription>();
		root.getChildren().add(item);
	}

	private class NavigationTreeCell extends TextFieldTreeCell<Subscription> {

		private ContextMenu contextMenu;

	    public NavigationTreeCell(TreeView<Subscription> selected) {
	        contextMenu = new ContextMenu();
	        contextMenu.setId(selected.getId());
	        
	        MenuItem deleteItem = new MenuItem("Delete");
	        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                	Alert alert = new Alert(AlertType.CONFIRMATION);
                	alert.setTitle("Confirm Deletion of Subscription");
                	alert.setHeaderText("CONFIRM DELETION!");
                	alert.setContentText("Are you sure you want to delete this subscription?");

                	Optional<ButtonType> result = alert.showAndWait();
                	if (result.get() == ButtonType.OK){
                    	eventBus.fireEvent(NotificationEvent.DELETE_SUBSCRIPTION,
                    			Utility.getParameterMap(NotificationParameter.SELECTED_SUBSCRIPTION,
                						getTreeItem().getValue().getTitle()));  
                    	eventBus.fireEvent(NotificationEvent.PLAY_SOUND,
                    			Utility.getParameterMap(NotificationParameter.SELECTED_SUBSCRIPTION,
                						getTreeItem().getValue().getTitle()));  
                    	deleteNode((TreeItem<Subscription>)tree.getSelectionModel().getSelectedItem());
                	} else {
                	    // don't delete it...
                	}                                   
                }
	        });
	        
	        MenuItem refreshItem = new MenuItem("Refresh");
	        refreshItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                	eventBus.fireEvent(NotificationEvent.REFRESH_SUBSCRIPTION,
                			Utility.getParameterMap(NotificationParameter.SELECTED_SUBSCRIPTION,
            						getTreeItem().getValue().getTitle()));                                      
                }
	        });
	        
	        contextMenu.getItems().addAll(refreshItem, deleteItem);
	    }

	    public void updateItem(Subscription item, boolean empty) {

	        super.updateItem(item, empty);

	        if (!empty && getTreeItem().getParent() != null) {
	            setContextMenu(contextMenu);
	        }
	    }
	}
}
