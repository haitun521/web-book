package com.haitun.bean;

import java.util.List;

public class HPULibBook implements java.io.Serializable {

	private static final long serialVersionUID = -34980392745682239L;

	private List<String> address;

	private String author;
	private String borrowInfo;
	private String callNumber;
	private String dateTime;
	private String ISBNPrice;
	private String link;
	private String name;
	private String publisher;
	private String summary;
	public HPULibBook() {

	}

	public List<String> getAddress() {
		return address;
	}

	public String getAuthor() {
		return author;
	}

	public String getBorrowInfo() {
		return borrowInfo;
	}

	public String getCallNumber() {
		return callNumber;
	}

	public String getDateTime() {
		return dateTime;
	}

	public String getISBNPrice() {
		return ISBNPrice;
	}

	public String getLink() {
		return link;
	}

	public String getName() {
		return name;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getSummary() {
		return summary;
	}

	public void setAddress(List<String> address) {
		this.address = address;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setBorrowInfo(String borrowInfo) {
		this.borrowInfo = borrowInfo;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public void setISBNPrice(String iSBNPrice) {
		ISBNPrice = iSBNPrice;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public String toString() {
		return "HPULibBook [name=" + name + ", link=" + link + ", author="
				+ author + ", publisher=" + publisher + ", ISBNPrice="
				+ ISBNPrice + ", callNumber=" + callNumber + ", dateTime="
				+ dateTime + ", address=" + address + ", borrowInfo="
				+ borrowInfo + ", summary=" + summary + "]";
	}

}
