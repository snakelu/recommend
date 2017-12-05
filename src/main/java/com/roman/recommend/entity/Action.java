package com.roman.recommend.entity;

public enum Action {

	VIEW((short) 1, "浏览"), COMMENT((short) 2, "评论"), VOTE((short) 3, "点赞"), COLLECT((short) 4, "收藏"), PLAY((short) 5,
			"视频播放"), SHARE((short) 6, "分享"), ENROLL((short) 7, "报名");

	private Short actionId;

	private String actionName;

	private Action(Short actionId, String actionName) {
		this.actionId = actionId;
		this.actionName = actionName;
	}

	/**
	 * @return the actionId
	 */
	public Short getActionId() {
		return actionId;
	}

	/**
	 * @param actionId
	 *            the actionId to set
	 */
	public void setActionId(Short actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @param actionName
	 *            the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

}
