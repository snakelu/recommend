package com.roman.recommend.entity;

public class UserAction {

	/**
	 * 设备id：1992992
	 */
	private String imei;

	/**
	 * 用户id：c270e012a8fd722205b5e5e049f4d532
	 */
	private String userId;

	/**
	 * 活动id：9499288992 --对应{@link}Item
	 */
	private String itemId;

	/**
	 * 用户行为：对应Action枚举
	 */
	private Short actionType;

	/**
	 * 推荐id
	 */
	private String recRequestId;

	/**
	 * 用户行为发生时间戳
	 */
	private Long actionTime;

	/**
	 * 额外携带参数：比如视频播放时长，json格式
	 */
	private String ext;

	/**
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * @param imei
	 *            the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the actionType
	 */
	public Short getActionType() {
		return actionType;
	}

	/**
	 * @param actionType
	 *            the actionType to set
	 */
	public void setActionType(Short actionType) {
		this.actionType = actionType;
	}

	/**
	 * @return the recRequestId
	 */
	public String getRecRequestId() {
		return recRequestId;
	}

	/**
	 * @param recRequestId
	 *            the recRequestId to set
	 */
	public void setRecRequestId(String recRequestId) {
		this.recRequestId = recRequestId;
	}

	/**
	 * @return the actionTime
	 */
	public Long getActionTime() {
		return actionTime;
	}

	/**
	 * @param actionTime
	 *            the actionTime to set
	 */
	public void setActionTime(Long actionTime) {
		this.actionTime = actionTime;
	}

	/**
	 * @return the ext
	 */
	public String getExt() {
		return ext;
	}

	/**
	 * @param ext
	 *            the ext to set
	 */
	public void setExt(String ext) {
		this.ext = ext;
	}

}
