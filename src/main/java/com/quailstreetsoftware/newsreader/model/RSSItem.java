package com.quailstreetsoftware.newsreader.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RSSItem {

	public final static String TITLE = "title";
	public final static String LINK = "link";
	public final static String DESCRIPTION = "description";
	public final static String CATEGORY = "category";
	public final static String PUB_DATE = "pubDate";
	public final static String GUID = "guid";
	
	private DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
	private DateFormat externalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");	
	private Date date;
	private StringProperty title, link, description, pubDate, guid;
	private List<String> categories;

	public RSSItem(Node node) {

		this.categories = new ArrayList<String>();

		NodeList childNodes = node.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++) {
			Node cNode = childNodes.item(j);
			if (cNode instanceof Element) {
				String content = cNode.getLastChild().getTextContent().trim();
				switch (cNode.getNodeName()) {
				case TITLE:
					this.title = new SimpleStringProperty(this, "title");
					this.title.set(content);
					break;
				case LINK:
					this.link = new SimpleStringProperty(this, "link");
					this.link.set(content);
				case DESCRIPTION:
					this.description = new SimpleStringProperty(this, "description");
					this.description.set(content);
					break;
				case CATEGORY:
					this.categories.add(content);
					break;
				case PUB_DATE:
					this.pubDate = new SimpleStringProperty(this, "pubDate");
					try {
						this.date = formatter.parse(content);
						this.pubDate.set(externalFormat.format(this.date));
					} catch (ParseException e) {
						this.pubDate.set("");
					}
					break;
				case GUID:
					this.guid = new SimpleStringProperty(this, "guid");
					this.guid.set(content);
					break;
				default:
					break;
				}
			}
		}
	}

	public String getLink() {
		return link.getValue();
	}

	public String getDescription() {
		return description.getValue();
	}

	public String getPubDate() {
		return pubDate.getValue();
	}

	public String getGuid() {
		return guid.getValue();
	}

	public List<String> getCategories() {
		return categories;
	}
	
    public String getTitle() { 
    	return title.getValue();
    }
    public StringProperty titleProperty() { 
        if (this.title == null){
        	return new SimpleStringProperty(this, "title");
        }
        return this.title;
    } 

}
