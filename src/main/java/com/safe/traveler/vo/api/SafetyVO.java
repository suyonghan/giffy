package com.safe.traveler.vo.api;

public class SafetyVO {
	String sid = null;
	String isoCode = null;
	String title = null;
	String content = null;
	String writeDate = null;
	String remarks = null;
	String riskType = null;

	public SafetyVO() {

	}

	public SafetyVO(String sid, String isoCode, String title, String content, String writeDate, String remarks,
			String riskType) {
		super();
		this.sid = sid;
		this.isoCode = isoCode;
		this.title = title;
		this.content = content;
		this.writeDate = writeDate;
		this.remarks = remarks;
		this.riskType = riskType;
	}

	public String getRiskType() {
		return riskType;
	}

	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "SafetyVO [sid=" + sid + ", isoCode=" + isoCode + ", title=" + title + ", content=" + content
				+ ", writeDate=" + writeDate + ", remarks=" + remarks + ", riskType=" + riskType + "]";
	}

}
