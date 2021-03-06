package com.haitun.bean;

// Generated 2016-2-3 11:57:06 by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;


/**
 * Category generated by hbm2java
 */
public class Category implements java.io.Serializable {

	private static final long serialVersionUID = -5428866584195631553L;
	private Integer id;
	private String name;
	private Set booktagses = new HashSet(0);

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	public Category(String name, Set booktagses) {
		this.name = name;
		this.booktagses = booktagses;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set getBooktagses() {
		return this.booktagses;
	}

	public void setBooktagses(Set booktagses) {
		this.booktagses = booktagses;
	}

}
