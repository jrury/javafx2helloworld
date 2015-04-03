package com.quailstreetsoftware.newsreader.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.quailstreetsoftware.newsreader.EventBus;

public class ModelContainerFactory {

	public static ModelContainer build(EventBus eventBus) {

		if (Files.exists(Paths.get("data", "container.ser"))) {
			ModelContainer mc = null;
			try {
				File dataDirectory = new File("data");
				File serializedFile = new File(dataDirectory, "container.ser");
				FileInputStream fileIn = new FileInputStream(serializedFile);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				mc = (ModelContainer) in.readObject();
				in.close();
				fileIn.close();
			} catch (IOException | ClassNotFoundException i) {
				i.printStackTrace();
			}
			mc.initialize(eventBus);
			return mc;
		} else {
			return new ModelContainer(eventBus);
		}

	}

}
