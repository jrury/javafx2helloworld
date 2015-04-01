package com.quailstreetsoftware.newsreader.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Utility {

	public static HashMap<String, String> getParameterMap(String... parameters) {

		HashMap<String, String> parameterMap = new HashMap<String, String>();

		if (parameters.length % 2 != 0) {
			// throw exception here
			return parameterMap;
		}
		int index = 0;
		while (index < parameters.length) {
			parameterMap.put(parameters[index], parameters[index + 1]);
			index += 2;
		}
		return parameterMap;
	}

	public static ArrayList<String> readLinesFromStream(InputStream input) {

		ArrayList<String> lines = new ArrayList<String>();
		InputStreamReader isr = new InputStreamReader(input);
		BufferedReader reader = new BufferedReader(isr);
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			reader.close();
		} catch (IOException e) {

		}
		return lines;
	}

}
