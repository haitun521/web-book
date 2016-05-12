package com.haitun.util;

public enum RespCode {

	SUCCESS(000, "成功"), FAIL(111, "失败");

	private int resCode;
	private String resInfo;

	RespCode(int resCode, String resInfo) {
		this.setResCode(resCode);
		this.setResInfo(resInfo);
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

}
