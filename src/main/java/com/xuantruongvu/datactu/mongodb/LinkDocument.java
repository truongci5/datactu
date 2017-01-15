package com.xuantruongvu.datactu.mongodb;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.xuantruongvu.datactu.util.HashUtil;

/**
 * @author xuantruongvu
 * This class represents the object corresponding to the Link document stored within MongoDB
 */
public class LinkDocument {
	private String url;
	
	private String hashedUrl;
	
	private String source;

	private Long createdAt; 
	
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
		
		try {
			this.hashedUrl = HashUtil.hashString(url);
		} catch (Exception e) {
			this.hashedUrl = url;
		}
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
	 * Converts to the MongoDB native object
	 * @return
	 */
	public DBObject createDBObject() {
		BasicDBObjectBuilder docBuilder = BasicDBObjectBuilder.start();
        
        docBuilder.append("url", url);
        docBuilder.append("hashed_url", hashedUrl);
        docBuilder.append("source", source);
        docBuilder.append("created_at", createdAt);
        return docBuilder.get();
	}
}
