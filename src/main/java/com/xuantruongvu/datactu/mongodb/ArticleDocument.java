package com.xuantruongvu.datactu.mongodb;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

/**
 * @author xuantruongvu
 * This class represents the object corresponding to the Article document stored within MongoDB
 */
public class ArticleDocument {
	private String title;
	
	private String description;
	
	private String image;
	
	private String content;
	
	private String html;
	
	private String url;
	
	private String source;
	
	private Long createdAt;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the html
	 */
	public String getHtml() {
		return html;
	}

	/**
	 * @param html the html to set
	 */
	public void setHtml(String html) {
		this.html = html;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	
	/**
	 * @return the created_at
	 */
	public Long getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param created_at the created_at to set
	 */
	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Convert to MongoDB native object
	 * @return
	 */
	public DBObject createDBObject() {
		BasicDBObjectBuilder docBuilder = BasicDBObjectBuilder.start();
        
        docBuilder.append("title", title);
        docBuilder.append("description", description);
        docBuilder.append("image", image);
        docBuilder.append("content", content);
        docBuilder.append("html", html);
        docBuilder.append("url", url);
        docBuilder.append("source", source);
        docBuilder.append("created_at", createdAt);
        return docBuilder.get();
	}
}
