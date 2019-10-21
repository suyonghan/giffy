package com.safe.traveler.vo.api;

public class WarningVO {
	String isoCode = null;
	String wid = null;
	String countryName = null;
	String continent = null;
	String warning = null;
	String warningArea = null;
	String writeDate = null;
	String limitStatus = null;
	String limitArea = null;

	public WarningVO() {
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getWid() {
		return wid;
	}

	public void setWid(String wid) {
		this.wid = wid;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public String getWarningArea() {
		return warningArea;
	}

	public void setWarningArea(String warningArea) {
		this.warningArea = warningArea;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}

	public String getLimitStatus() {
		return limitStatus;
	}

	public void setLimitStatus(String limitStatus) {
		this.limitStatus = limitStatus;
	}

	public String getLimitArea() {
		return limitArea;
	}

	public void setLimitArea(String limitArea) {
		this.limitArea = limitArea;
	}

	@Override
	public String toString() {
		return "WarningVO [isoCode=" + isoCode + ", wid=" + wid + ", countryName=" + countryName + ", continent="
				+ continent + ", warning=" + warning + ", warningArea=" + warningArea + ", writeDate=" + writeDate
				+ ", limitStatus=" + limitStatus + ", limitArea=" + limitArea + "]";
	}

}
