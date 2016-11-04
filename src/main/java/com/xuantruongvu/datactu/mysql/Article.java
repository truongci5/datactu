package com.xuantruongvu.datactu.mysql;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "datactu_tin")
public class Article implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "tieude")
	private String title;

	@Column(name = "tomtat")
	private String description;

	@Column(name = "url_image")
	private String image;

	@Column(name = "noidung", length = 20000)
	private String content;
	
	@Column(name = "html", length = 60000)
	private String html;

	@Column(name = "nguontin_id")
	private Integer sourceId;
	
	@Column(name = "duongdanbai")
	private String url;
	
	@Column(name = "tacgia")
	private String author = "chưa xác định";
	
	@Column(name = "ngaydang")
	@Temporal(TemporalType.TIMESTAMP)
	private Date publishedDate;
	
	@Column(name = "linhvuc_id")
	private Integer domainId;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name="datactu_tin2chude",
		joinColumns=@JoinColumn(name="tin_id", referencedColumnName="id"),
		inverseJoinColumns=@JoinColumn(name="chude_id", referencedColumnName="id"))
	private Set<Topic> topics;

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		try {
	        int size = getClass().getDeclaredField("content").getAnnotation(Column.class).length();
	        int inLength = content.length();
	        if (inLength>size)
	        {
	        	content = content.substring(0, size-1);
	        }
	    } catch (NoSuchFieldException ex) {
	    } catch (SecurityException ex) {
	    }
		
		this.content = content;
	}

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

	/**
	 * @param sourceId the sourceId to set
	 */
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param domainId the domainId to set
	 */
	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	/**
	 * @return the topics
	 */
	public Set<Topic> getTopics() {
		return topics;
	}

	/**
	 * @param topics the topics to set
	 */
	public void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}
	
	/**
	 * @param publishedDate the publishedDate to set
	 */
	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public void attachTopic(Topic topic) {
		if (topics == null) {
			topics = new HashSet<Topic>();
		}
		
		topics.add(topic);
	}
	
	
	
	public String getUrl() {
		return url;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getPublishedDate() {
		return publishedDate.toString();
	}
}
