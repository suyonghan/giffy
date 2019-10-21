package com.safe.traveler.vo;

public class AdminVO {
	private String url1;
	private String url2;
	private String url3;
	private String url4;
	private String serviceKey;

	public AdminVO(String url1, String url2, String url3, String url4, String serviceKey) {
		this.url1 = url1;
		this.url2 = url2;
		this.url3 = url3;
		this.url4 = url4;
		this.serviceKey = serviceKey;
	}

	public String getUrl1() {
		return url1;
	}

	public String getUrl2() {
		return url2;
	}

	public String getUrl3() {
		return url3;
	}

	public String getUrl4() {
		return url4;
	}

	public String getServiceKey() {
		return serviceKey;
	}

	@Override
	public String toString() {
		return "AdminVO [url1=" + url1 + ", url2=" + url2 + ", url3=" + url3 + ", url4=" + url4 + ", serviceKey1="
				+ serviceKey + "]";
	}
}
