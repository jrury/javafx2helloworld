package com.quailstreetsoftware.newsreader.ui;

import java.util.Collection;
import java.util.HashMap;

import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.Utility;
import com.quailstreetsoftware.newsreader.model.RSSSubscription;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class NavigationTree {

	private TreeView<String> tree;
	private HashMap<String, String> subscriptionTitles;

	public NavigationTree(final Collection<RSSSubscription> subscriptions,
			final UIComponents controller) {

		this.subscriptionTitles = new HashMap<String, String>();
		TreeItem<String> rootNode = new TreeItem<String>("Subscriptions");
		rootNode.setExpanded(true);
		for (RSSSubscription subscription : subscriptions) {
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
							controller.notify(NotificationEvent.CHANGED_SELECTED_SOURCE,
								Utility.getParameterMap(NotificationParameter.SELECTED_SUBSCRIPTION,
									currentlySelected.getValue()));
						}
					}
				});
		this.tree.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new NavMouseHandler());
	}

	public TreeView<String> getTree() {
		return this.tree;
	}

	public class NavMouseHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			if (event.getButton().equals(MouseButton.SECONDARY)) {
				// open up menu offering to refresh subscription
				// delete subscription?
			}
		}

	};
}
