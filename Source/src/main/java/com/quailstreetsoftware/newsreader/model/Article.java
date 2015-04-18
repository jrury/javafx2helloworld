package com.quailstreetsoftware.newsreader.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Article implements Serializable {

	private static final long serialVersionUID = 7444031207035060403L;

	private final static String TITLE = "title";
	private final static String LINK = "link";
	private final static String DESCRIPTION = "description";
	private final static String CATEGORY = "category";
	private final static String PUB_DATE = "pubDate";
	private final static String GUID = "guid";
	
	private DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
	private DateFormat externalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");	
	private Date date;
	private String title, link, description, pubDate, guid;
	private List<String> categories;

	public Article(Node node) {
		initialize(node);
	}

	public void initialize(final Node node) {

		this.categories = new ArrayList<String>();

		NodeList childNodes = node.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++) {
			Node cNode = childNodes.item(j);
			if (cNode instanceof Element && cNode.getLastChild() != null && cNode.getLastChild().getTextContent() != null) {
				String content = cNode.getLastChild().getTextContent().trim();
				switch (cNode.getNodeName()) {
				case TITLE:
					this.title = content;
					break;
				case LINK:
					this.link = content;
					break;
				case DESCRIPTION:
					this.description = content;
					break;
				case CATEGORY:
					this.categories.add(content);
					break;
				case PUB_DATE:
					try {
						this.date = formatter.parse(content);
						this.pubDate = externalFormat.format(this.date);
					} catch (ParseException e) {
						this.pubDate = "";
					}
					break;
				case GUID:
					this.guid = content;
					break;
				default:
					break;
				}
			}
		}
	}

	public String getLink() {
		return link;
	}

	public String getDescription() {
		return description;
	}

	public String getPubDate() {
		return pubDate;
	}

	public String getGuid() {
		return guid;
	}

	public List<String> getCategories() {
		return categories;
	}
	
    public String getTitle() { 
    	return title;
    }

}
