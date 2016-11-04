package com.xuantruongvu.datactu.mysql;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "datactu_thoidiemlaytin")
public class CheckPoint {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "thoidiem")
	private int checkpoint;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the checkpoint
	 */
	public int getCheckpoint() {
		return checkpoint;
	}

	/**
	 * @param checkpoint the checkpoint to set
	 */
	public void setCheckpoint(int checkpoint) {
		this.checkpoint = checkpoint;
	}
}
