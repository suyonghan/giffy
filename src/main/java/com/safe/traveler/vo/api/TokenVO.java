package com.safe.traveler.vo.api;

public class TokenVO {
	private int tokenId;
	private String userName;
	private String startDate;
	private String expiryDate;
	private String serviceKey;
	private String state;

	public String getState() {
		return state;
	}

	public TokenVO(String userName, String startDate, String expiryDate, String serviceKey, String state) {
		this.userName = userName;
		this.startDate = startDate;
		this.expiryDate = expiryDate;
		this.serviceKey = serviceKey;
		this.state = state;
	}

	public TokenVO(int tokenId, String userName, String startDate, String expiryDate, String serviceKey, String state) {
		this.tokenId = tokenId;
		this.userName = userName;
		this.startDate = startDate;
		this.expiryDate = expiryDate;
		this.serviceKey = serviceKey;
		this.state = state;
	}

	public String getServiceKey() {
		return serviceKey;
	}

	public int getTokenId() {
		return tokenId;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public String toString() {
		return "TokenDTO [tokenId=" + tokenId + ", userName=" + userName + ", startDate=" + startDate + ", expiryDate="
				+ expiryDate + ", serviceKey=" + serviceKey + ", state=" + state + "]";
	}

	public String getStartDate() {
		return startDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}
}
