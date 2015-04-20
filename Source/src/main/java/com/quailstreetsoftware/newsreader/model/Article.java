package com.quailstreetsoftware.newsreader.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.quailstreetsoftware.newsreader.common.Utility;

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
	private String subscription;
	private Boolean hasTitle = Boolean.FALSE, hasDescription = Boolean.FALSE, hasLink = Boolean.FALSE;

	public Article(final Node node, final String subscription) {
		this.subscription = subscription;
		initialize(node);
	}

	public void initialize(final Node node) {

		this.categories = new ArrayList<String>();

		NodeList childNodes = node.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++) {
			Node cNode = childNodes.item(j);
			if (cNode instanceof Element) {
				String content = Utility.getTextFromNode((Element) cNode);
				switch (cNode.getNodeName()) {
				case TITLE:
					setTitle(content);
					break;
				case LINK:
					setLink(content);
					break;
				case DESCRIPTION:
					setDescription(content);
					break;
				case CATEGORY:
					addCategory(content);
					break;
				case PUB_DATE:
					try {
						this.date = formatter.parse(content);
						setDate(externalFormat.format(this.date));
					} catch (ParseException e) {
						this.pubDate = "";
					}
					break;
				case GUID:
					setGUID(content);
					break;
				default:
					break;
				}
			}
		}
	}

	public Boolean isValid() {
		return this.hasDescription && this.hasTitle && this.hasLink;
	}

	private void setGUID(final String passedGuid) {
		this.guid = passedGuid;
		
	}

	private void setDate(final String passedDate) {
		this.pubDate = passedDate;
	}

	private void setLink(final String passedLink) {
		this.link = passedLink;
		this.hasLink = Boolean.TRUE;		
	}

	private void setDescription(final String passedDescription) {
		this.description = passedDescription;
		this.hasDescription = Boolean.TRUE;
	}

	private void addCategory(final String category) {
		this.categories.add(category);
	}

	private void setTitle(final String passedTitle) {
		this.title = passedTitle;
		this.hasTitle = Boolean.TRUE;		
	}

	public String getLink() {
		return this.link;
	}

	public String getDescription() {
		return this.description;
	}

	public String getPubDate() {
		return this.pubDate;
	}

	public String getGuid() {
		return this.guid;
	}

	public List<String> getCategories() {
		return this.categories;
	}
	
    public String getTitle() { 
    	return this.title;
    }
    
    public String getSubscription() {
    	return this.subscription;
    }

}
