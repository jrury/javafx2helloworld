package com.quailstreetsoftware.newsreader.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Utility {

	public static HashMap<String, Object> getParameterMap(Object... parameters) {

		HashMap<String, Object> parameterMap = new HashMap<String, Object>();

		if (parameters.length % 2 != 0) {
			// throw exception here
			return parameterMap;
		}
		int index = 0;
		while (index < parameters.length) {
			parameterMap.put((String) parameters[index], parameters[index + 1]);
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

	public static StringProperty initializeStringProperty(Object owner, String content,
			String propertyName) {
		SimpleStringProperty prop = new SimpleStringProperty(owner, propertyName);
		prop.set(content);
		return prop;
	}

	/**
	 * Method to extract text content from the children of a given node - either text found within a CDATA tag 
	 * or regular text content, with preference given to text found within a CDATA section.
	 * @param node the node whose children should be searched.
	 * @return the text content found or an empty string
	 */
	public static String getTextFromNode(Element node) {
		String cDataContent = null;
		String textContent = "";
		Boolean hadCData = Boolean.FALSE;
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() == Node.CDATA_SECTION_NODE) {
				cDataContent = nodeList.item(i).getNodeValue().trim();
				hadCData = Boolean.TRUE;
				break;
			} else if(nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
				textContent += nodeList.item(i).getTextContent().trim();
			}
		}
		return hadCData && cDataContent != null ? cDataContent : textContent;
	}

}
