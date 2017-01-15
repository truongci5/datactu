package com.xuantruongvu.datactu.crawler;

/**
 * @author xuantruongvu
 * This class stores the meta-data of the article
 */
public class MetaData {
	private String title;
	private String description;
	private String image;
	
	public String getTitle() {
		if (title.indexOf(" - ") > 0) title = title.substring(0, title.indexOf(" - "));
		if (title.indexOf(" | ") > 0) title = title.substring(0, title.indexOf(" | "));
		return title;
	}
	public String getDescription() {
		return description;
	}
	public String getImage() {
		return image;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
}
