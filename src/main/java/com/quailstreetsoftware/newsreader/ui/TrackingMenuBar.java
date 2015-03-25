package com.quailstreetsoftware.newsreader.ui;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class TrackingMenuBar {

	private MenuBar menuBar;

	public TrackingMenuBar() {
		this.menuBar = new MenuBar();
	}

	public void initialize() {
		this.menuBar = new MenuBar();
		this.menuBar.setUseSystemMenuBar(Boolean.TRUE);

		// --- Menu File
		Menu menuFile = new Menu("File");
		menuFile.getItems().add(new MenuItem("New"));
		menuFile.getItems().add(new MenuItem("Open"));
		menuFile.getItems().add(new MenuItem("Save"));
		menuFile.getItems().add(new MenuItem("Save As"));
		menuFile.getItems().add(new SeparatorMenuItem());
		menuFile.getItems().add(getExitItem());

		this.menuBar.getMenus().addAll(menuFile);
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
