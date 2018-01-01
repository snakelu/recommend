package com.roman.recommend.entity;

public class ItemScore {

	private Long imeiId;

	private String imei;

	private String userId;

	private String itemId;

	private double score;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getImeiId() {
		return imeiId;
	}

	public void setImeiId(Long imeiId) {
		this.imeiId = imeiId;
	}
}
