package com.quailstreetsoftware.newsreader.ui;

import java.util.HashMap;
import java.util.Optional;

import com.quailstreetsoftware.newsreader.system.EventBus;
import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.common.NotificationParameter.ParameterEnum;
import com.quailstreetsoftware.newsreader.common.Utility;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

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

		MenuItem newSubscription = new MenuItem("New Subscription");
		newSubscription.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Dialog<HashMap<ParameterEnum, String>> dialog = new Dialog<>();
				dialog.setTitle("Subscribe To A New RSS Feed");
				dialog.setHeaderText("Enter the title and URL for the RSS Feed to subscribe to.");
				dialog.setResizable(true);
				dialog.setWidth(600);

				Label titleLabel = new Label("Title: ");
				Label urlLabel = new Label("URL: ");
				TextField titleText = new TextField();
				titleText.setMinWidth(300);
				TextField urlText = new TextField("http://");
				urlText.setMinWidth(500);
						
				GridPane grid = new GridPane();
				grid.add(titleLabel, 1, 1);
				grid.add(titleText, 2, 1);
				grid.add(urlLabel, 1, 2);
				grid.add(urlText, 2, 2);
				dialog.getDialogPane().setContent(grid);
						
				ButtonType buttonTypeOk = new ButtonType("Save", ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

				dialog.setResultConverter(new Callback<ButtonType, HashMap<ParameterEnum, String>>() {
				    @Override
				    public HashMap<ParameterEnum, String> call(ButtonType b) {

				        if (b == buttonTypeOk) {
				        	HashMap<ParameterEnum, String> params = new HashMap<ParameterEnum, String>();
				        	params.put(ParameterEnum.SELECTED_SUBSCRIPTION, titleText.getText());
				        	params.put(ParameterEnum.SUBSCRIPTION_URL, urlText.getText());
				        	return params;
				        }

				        return null;
				    }
				});
				Optional<HashMap<ParameterEnum, String>> result = dialog.showAndWait();
				
				if (result.isPresent()) {
					HashMap<ParameterEnum, String> temp = (HashMap<ParameterEnum, String>) result.get();
					if(temp != null) {
						eventBus.fireEvent(NotificationEvent.NEW_SUBSCRIPTION,
							Utility.getParameterMap(
									new NotificationParameter(ParameterEnum.SELECTED_SUBSCRIPTION, temp.get(ParameterEnum.SELECTED_SUBSCRIPTION)),
									new NotificationParameter(ParameterEnum.SUBSCRIPTION_URL, temp.get(ParameterEnum.SUBSCRIPTION_URL))));
					}
				}
			}
		});
		
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setMnemonicParsing(true);
		exitItem.setAccelerator(new KeyCodeCombination(KeyCode.X,
				KeyCombination.CONTROL_DOWN));
		exitItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});
		return new MenuItem[] { newSubscription, exitItem };
	}

	public MenuBar getMenuBar() {
			initialize();
			return this.menuBar;
		
	}

}
