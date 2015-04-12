package com.quailstreetsoftware.newsreader.ui;

import com.quailstreetsoftware.newsreader.system.EventBus;
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
		menuFile.getItems().addAll(getFileItems());
		
		Menu menuView = new Menu("View");
		menuView.setMnemonicParsing(true);
		menuView.getItems().addAll(getViewItems());
		
		Menu optionsView = new Menu("Options");
		optionsView.setMnemonicParsing(true);
		optionsView.getItems().addAll(getOptionsItems());
		
		Menu aboutView = new Menu("About");
		aboutView.getItems().addAll(getAboutItems());

		this.menuBar.getMenus().addAll(menuFile, menuView, optionsView, aboutView);
	}

	private MenuItem[] getOptionsItems() {
		MenuItem soundsToggleItem = new MenuItem("Enable/Disable Sounds");
		soundsToggleItem.setAccelerator(new KeyCodeCombination(KeyCode.F11));
		soundsToggleItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				eventBus.fireEvent(NotificationEvent.TOGGLE_SOUNDS, null);
			}
		});
		return new MenuItem[] { soundsToggleItem };
	}

	private MenuItem[] getAboutItems() {
		MenuItem helpItem = new MenuItem("Help");
		return new MenuItem[] { helpItem };
	}

	private MenuItem[] getViewItems() {
		MenuItem debugToggleItem = new MenuItem("Show/Hide Debug Log");
		debugToggleItem.setAccelerator(new KeyCodeCombination(KeyCode.F12));
		debugToggleItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				eventBus.fireEvent(NotificationEvent.TOGGLE_DEBUG, null);
			}
		});
		return new MenuItem[] { debugToggleItem };
	}

	private MenuItem[] getFileItems() {

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setMnemonicParsing(true);
		exitItem.setAccelerator(new KeyCodeCombination(KeyCode.X,
				KeyCombination.CONTROL_DOWN));
		exitItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});
		return new MenuItem[] { exitItem };
	}

	public MenuBar getMenuBar() {
			initialize();
			return this.menuBar;
		
	}

}
