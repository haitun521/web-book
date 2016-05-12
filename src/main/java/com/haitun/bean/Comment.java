package com.haitun.bean;

// Generated 2016-2-3 11:57:06 by Hibernate Tools 4.0.0

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Comment generated by hbm2java
 */
public class Comment implements java.io.Serializable {

	private static final long serialVersionUID = 7659315418072160092L;
	private Integer id;
	private Book book;
	private String content;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date date;

	public Comment() {
	}

	public Comment(Book book, String content, Date date) {
		this.book = book;
		this.content = content;
		this.date = date;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}