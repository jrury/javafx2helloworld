package com.quailstreetsoftware.newsreader.common;

import java.util.HashMap;

public class Utility {

	public static HashMap<String, String> getParameterMap(String... parameters) {
		
		HashMap<String, String> parameterMap = new HashMap<String, String>();
		
		if(parameters.length % 2 != 0) {
			// throw exception here
			return parameterMap;
		}
		int index = 0;
		while(index < parameters.length) {
			parameterMap.put(parameters[index], parameters[index+1]);
			index += 2;
		}
		return parameterMap;
	}

}
