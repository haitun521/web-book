package com.haitun.bean;

import com.haitun.util.RespCode;

public class Message {

	private int resCode; // 结果码
	private String resInfo;// 结果信息
	private Object item;

	public Message() {
	}

	public Message(int resCode, String resInfo) {
		this.resCode = resCode;
		this.resInfo = resInfo;
	}

	public Message(RespCode respCode) {
		this.resCode = respCode.getResCode();
		this.resInfo = respCode.getResInfo();
	}

	public int getResCode() {
		return resCode;
	}

	public void setResCode(int resCode) {
		this.resCode = resCode;
	}

	public String getResInfo() {
		return resInfo;
	}

	public void setResInfo(String resInfo) {
		this.resInfo = resInfo;
	}

	public Object getItem() {
		return item;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "Message [resCode=" + resCode + ", resInfo=" + resInfo
				+ ", item=" + item + "]";
	}

}
