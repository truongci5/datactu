package com.xuantruongvu.datactu.mysql;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
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
@Table(name = "datactu_linhvuc")
public class Domain implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "ten")
	private String name;
	
	@Column(name = "keywords")
	private String keywords;

	@Column(name = "ma")
	private String code;

	@Column(name = "trangthai")
	private Integer state;

	@Column(name = "daxoa")
	private Integer isDeleted;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "datactu_linhvuc2nguontin", joinColumns = @JoinColumn(name = "linhvuc_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "nguontin_id", referencedColumnName = "id"))
	private List<Source> sources;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "datactu_linhvuc2chude", joinColumns = @JoinColumn(name = "linhvuc_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "chude_id", referencedColumnName = "id"))
	private List<Topic> topics;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the state
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * @return the isDeleted
	 */
	public Integer getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @return the sources
	 */
	public List<Source> getSources() {
		return sources;
	}

	/**
	 * @return the topics
	 */
	public List<Topic> getTopics() {
		return topics;
	}

	/**
	 * @return the keywords
	 */
	public List<String> getKeywords() {
		String delimiter = "\\s*;\\s*";
		
		return Arrays.asList(keywords.trim().split(delimiter));
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

}
