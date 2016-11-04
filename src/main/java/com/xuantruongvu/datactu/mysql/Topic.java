package com.xuantruongvu.datactu.mysql;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "datactu_chude")
public class Topic implements Serializable {
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

	public List<String> getKeywords() {
		String delimiter = "\\s*;\\s*";
		
		return Arrays.asList(keywords.trim().split(delimiter));
	}
}
