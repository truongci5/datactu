package com.xuantruongvu.datactu.mysql;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "datactu_html")
public class Html implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "html", length = 60000)
	private String html;
	
	@Column(name = "tin_id")
	private Integer articleId;
	
	/**
	 * @param html the html to set
	 */
	public void setHtml(String html) {
		try {
	        int size = getClass().getDeclaredField("html").getAnnotation(Column.class).length();
	        int inLength = html.length();
	        if (inLength>size)
	        {
	            html = html.substring(0, size-1);
	        }
	    } catch (NoSuchFieldException ex) {
	    } catch (SecurityException ex) {
	    }
		
		this.html = html;
	}
	
	
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}
}
