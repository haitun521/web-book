package com.haitun.bean;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class Review implements java.io.Serializable {

	private static final long serialVersionUID = 1083440270554578278L;

	private Book book;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date deletedTime;
	private int id;
	private User user;

	public Review() {

	}

	public Review(User user, Book book, Date createdTime,
			Date deletedTime) {
		this.user = user;
		this.book = book;
		this.createdTime = createdTime;
		this.deletedTime = deletedTime;
	}

	public Book getBook() {
		return book;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public Date getDeletedTime() {
		return deletedTime;
	}

	public int getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public void setDeletedTime(Date deletedTime) {
		this.deletedTime = deletedTime;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
