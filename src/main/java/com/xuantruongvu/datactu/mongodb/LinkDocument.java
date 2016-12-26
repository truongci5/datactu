package com.xuantruongvu.datactu.mongodb;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.xuantruongvu.datactu.util.HashUtil;

public class LinkDocument {
	private String url;
	
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
		try {
			this.url = HashUtil.hashString(url);
		} catch (Exception e) {
			this.url = url;
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
	
	public DBObject createDBObject() {
		BasicDBObjectBuilder docBuilder = BasicDBObjectBuilder.start();
        
        docBuilder.append("url", url);
        docBuilder.append("source", source);
        docBuilder.append("created_at", createdAt);
        return docBuilder.get();
	}
}
