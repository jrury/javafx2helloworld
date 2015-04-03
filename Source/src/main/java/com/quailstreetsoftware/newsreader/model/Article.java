package com.quailstreetsoftware.newsreader.model;

import java.io.Serializable;
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

import com.quailstreetsoftware.newsreader.common.Utility;

public class Article implements Serializable {

	private static final long serialVersionUID = 7444031207035060403L;
	public final static String TITLE = "title";
	public final static String LINK = "link";
	public final static String DESCRIPTION = "description";
	public final static String CATEGORY = "category";
	public final static String PUB_DATE = "pubDate";
	public final static String GUID = "guid";
	
	private DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
	private DateFormat externalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");	
	private Date date;
	private transient StringProperty title, link, description, pubDate, guid;
	private String titleS, linkS, descriptionS, pubDateS, guidS;
	private List<String> categories;

	public Article(Node node) {
		initialize(node);
	}

	public void initialize() {
		this.title = Utility.initializeStringProperty(this, this.titleS, "title");
		this.link = new SimpleStringProperty(this, this.linkS, "link");
		this.description = new SimpleStringProperty(this, this.descriptionS, "description");
		this.pubDate = new SimpleStringProperty(this, "pubDate");
		try {
			this.date = formatter.parse(this.pubDateS);
			this.pubDate.set(externalFormat.format(this.date));
		} catch (ParseException e) {
			this.pubDate.set("");
		}
		this.description = new SimpleStringProperty(this, this.descriptionS, "guid");
	}

	public void initialize(final Node node) {

		this.categories = new ArrayList<String>();

		NodeList childNodes = node.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++) {
			Node cNode = childNodes.item(j);
			if (cNode instanceof Element) {
				String content = cNode.getLastChild().getTextContent().trim();
				switch (cNode.getNodeName()) {
				case TITLE:
					this.titleS = content;
					this.title = Utility.initializeStringProperty(this, this.titleS, "title");
					break;
				case LINK:
					this.linkS = content;
					this.link = new SimpleStringProperty(this, this.linkS, "link");
				case DESCRIPTION:
					this.descriptionS = content;
					this.description = new SimpleStringProperty(this, this.descriptionS, "description");
					break;
				case CATEGORY:
					this.categories.add(content);
					break;
				case PUB_DATE:
					this.pubDateS = content;
					this.pubDate = new SimpleStringProperty(this, "pubDate");
					try {
						this.date = formatter.parse(content);
						this.pubDate.set(externalFormat.format(this.date));
					} catch (ParseException e) {
						this.pubDate.set("");
					}
					break;
				case GUID:
					this.descriptionS = content;
					this.description = new SimpleStringProperty(this, this.descriptionS, "guid");
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
