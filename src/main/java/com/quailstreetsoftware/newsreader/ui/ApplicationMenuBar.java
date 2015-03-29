package com.quailstreetsoftware.newsreader.ui;

import com.quailstreetsoftware.newsreader.EventBus;
import com.quailstreetsoftware.newsreader.common.NotificationEvent;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class ApplicationMenuBar {

	private MenuBar menuBar;
	private EventBus eventBus;

	public ApplicationMenuBar(EventBus eventBus) {
		this.menuBar = new MenuBar();
		this.eventBus = eventBus;
		initialize();
	}

	public void initialize() {
		this.menuBar = new MenuBar();
		this.menuBar.setUseSystemMenuBar(Boolean.TRUE);

		Menu menuFile = new Menu("File");
		menuFile.setMnemonicParsing(true);
		menuFile.getItems().add(new SeparatorMenuItem());
		menuFile.getItems().add(getExitItem());
		
		Menu menuView = new Menu("View");
		menuView.setMnemonicParsing(true);
		menuView.getItems().add(getDebugItem());

		this.menuBar.getMenus().addAll(menuFile, menuView);
	}

	private MenuItem getDebugItem() {
		MenuItem debugToggleItem = new MenuItem("Show/Hide Debug Log");
		debugToggleItem.setAccelerator(new KeyCodeCombination(KeyCode.F12));
		debugToggleItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				eventBus.eventReceived(NotificationEvent.TOGGLE_DEBUG, null);
			}
		});
		return debugToggleItem;
	}

	private MenuItem getExitItem() {

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setMnemonicParsing(true);
		exitItem.setAccelerator(new KeyCodeCombination(KeyCode.X,
				KeyCombination.CONTROL_DOWN));
		exitItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});
		return exitItem;
	}

	public MenuBar getMenuBar() {
			initialize();
			return this.menuBar;
		
	}

}
