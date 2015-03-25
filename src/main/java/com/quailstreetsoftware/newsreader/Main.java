package com.quailstreetsoftware.newsreader;

import java.util.Random;

import com.quailstreetsoftware.newsreader.model.ModelContainer;
import com.quailstreetsoftware.newsreader.ui.UIComponents;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;


public class Main extends Application {

	private UIComponents ui;
	private ModelContainer mc;
		
	@Override
	public void start(Stage primaryStage) {
		mc = new ModelContainer();
		ui = new UIComponents();

		try {
			GridPane grid = new GridPane();
			ColumnConstraints column1 = new ColumnConstraints();
			column1.setPercentWidth(100);
			grid.getColumnConstraints().addAll(column1);

			ui.update(mc.getStories());

			Node[] components = ui.getComponents();
			for(int i = 0; i < components.length; i++) {
				grid.add(components[i], 0, i);
			}
			
			Scene scene = new Scene(grid, 800, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("NewsReader");
			primaryStage.show();
			
            new Thread() {
                public void run() {
                	while(true) {
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
