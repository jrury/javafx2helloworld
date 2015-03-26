package com.quailstreetsoftware.newsreader;

import com.quailstreetsoftware.newsreader.model.ModelContainer;
import com.quailstreetsoftware.newsreader.ui.UIComponents;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
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
		ui = new UIComponents(mc.getSubscriptions());

		try {		    
			GridPane grid = new GridPane();
			grid.setStyle("-fx-background-color: palegreen; -fx-padding: 2; -fx-hgap: 2; -fx-vgap: 2;");
			grid.setGridLinesVisible(false);
			ColumnConstraints navColumn = new ColumnConstraints();
			navColumn.setPercentWidth(25);
			ColumnConstraints contentColumn = new ColumnConstraints();
			contentColumn.setPercentWidth(75);
			grid.getColumnConstraints().addAll(navColumn, contentColumn);

			ui.update(mc.getStories());

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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			});
			primaryStage.show();
			
            new Thread() {
                public void run() {
                	while(running) {
                		try {
							Thread.sleep(90000);
	                		mc.refresh();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                	}
                }
            }.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
