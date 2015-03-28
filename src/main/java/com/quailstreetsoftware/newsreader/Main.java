package com.quailstreetsoftware.newsreader;

import java.util.HashMap;

import com.quailstreetsoftware.newsreader.common.NotificationEvent;
import com.quailstreetsoftware.newsreader.common.NotificationParameter;
import com.quailstreetsoftware.newsreader.model.ModelContainer;
import com.quailstreetsoftware.newsreader.ui.UIComponents;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;


public class Main extends Application {

	private UIComponents ui;
	private ModelContainer mc;
	private Boolean running;
		
	@Override
	public void start(Stage primaryStage) {
		running = Boolean.TRUE;
		mc = new ModelContainer();
		ui = new UIComponents(mc.getSubscriptions(), this);

		try {		    
			GridPane grid = new GridPane();
			grid.setGridLinesVisible(false);
			ColumnConstraints navColumn = new ColumnConstraints();
			navColumn.setPercentWidth(25);
			ColumnConstraints contentColumn = new ColumnConstraints();
			contentColumn.setPercentWidth(75);
			grid.getColumnConstraints().addAll(navColumn, contentColumn);

			// this is goofy, do this better.
			Node[] components = ui.getComponents();
			for(int i = 0; i < components.length; i++) {
				if(i == 0) {
					grid.add(components[i], 0, i, 2, 1);
				} else if(i == 2) {
					grid.add(components[i], 0, i, 2, 2);
				} else {
					grid.add(components[i], 1, i);
				}
			}
			grid.add(ui.getNavigation(), 0, 1, 1, 2);
					  
		    
			Scene scene = new Scene(grid, 1000, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("NewsReader");
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent event) {
			        try {
			        	running = Boolean.FALSE;
			        	Platform.exit();
						stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
			    }
			});
			primaryStage.getIcons().add(new Image(this.getClass().getClassLoader().getResourceAsStream("META-INF/images/icon.png")));
			primaryStage.show();
			
            Thread backgroundUpdater = new Thread() {
                public void run() {
                	while(running) {
                		try {
							Thread.sleep(90000);
	                		mc.refreshAll();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                	}
                }
            };
            backgroundUpdater.setDaemon(true);
            backgroundUpdater.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void notify(NotificationEvent event, HashMap<String, String> arguments) {
		
		// tear this apart
		switch(event) {
			case CHANGED_SELECTED_SOURCE:
				ui.update(mc.getStories(arguments.get(NotificationParameter.SELECTED_SUBSCRIPTION)));
				break;
			case REFRESH_SUBSCRIPTION:
				mc.refresh(arguments.get(NotificationParameter.SELECTED_SUBSCRIPTION));
				ui.update(mc.getStories(arguments.get(NotificationParameter.SELECTED_SUBSCRIPTION)));
			default:
				break;
		}
	}
}
