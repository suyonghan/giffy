package com.safe.traveler.vo.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GiffyAPIVO {
	@Override
	public String toString() {
		return "GiffyAPIVO [countryName=" + countryName + ", isoCode=" + isoCode + ", safetyList=" + safetyList
				+ ", warningList=" + warningList + ", safetyTimeList=" + safetyTimeList + "]";
	}

	private String countryName;
	private String isoCode;
	List<SafetyVO> safetyList;
	List<WarningVO> warningList;

	public List<WarningVO> getWarningList() {
		return warningList;
	}

	public void setWarningList(List<WarningVO> warningList) {
		this.warningList = warningList;
	}

	List<SafetyVO> safetyTimeList;

	public GiffyAPIVO() {
		safetyList = new LinkedList<SafetyVO>();
		safetyTimeList = new LinkedList<SafetyVO>();
		warningList = new ArrayList<WarningVO>();
	}

	public List<SafetyVO> getSafetyList() {
		return safetyList;
	}

	public void setSafetyList(List<SafetyVO> safetyList) {
		this.safetyList = safetyList;
	}

	public List<SafetyVO> getSafetyTimeList() {
		return safetyTimeList;
	}

	public void setSafetyTimeList(List<SafetyVO> safetyTimeList) {
		this.safetyTimeList = safetyTimeList;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
}
