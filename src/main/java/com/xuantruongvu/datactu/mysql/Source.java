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
@Table(name = "datactu_nguontin")
public class Source implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "ten")
	private String name;

	@Column(name = "ma")
	private String code;

	@Column(name = "url_nguontin")
	private String url;

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
	 * @return the url
	 */
	public String getUrl() {
		return url;
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

	public String getDomain() {
		String domain = url.replace("https://", "");
		domain = domain.replace("http://", "");
		domain = domain.replace("www.", "");
		if (domain.indexOf("/") > 0) {
			domain = domain.substring(0, domain.indexOf("/"));
		}

		return domain;
	}
}
