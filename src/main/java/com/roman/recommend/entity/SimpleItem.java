package com.roman.recommend.entity;

import java.io.Serializable;

/**
 * 活动简化entity，拆分活动标签保存
 * 
 * @author lyhcc
 * @version 0.0.1 2017/12/3
 */
public class SimpleItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 活动id：9499288992
	 */
	private String itemId;

	/**
	 * 活动标签：骑行、烧烤（id: 101, 121）
	 */
	private String itemTag;

	/**
	 * 活动发布时间
	 */
	private Long itemModifyTime;

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
	 * @return the itemTag
	 */
	public String getItemTag() {
		return itemTag;
	}

	/**
	 * @param itemTag
	 *            the itemTag to set
	 */
	public void setItemTag(String itemTag) {
		this.itemTag = itemTag;
	}

	/**
	 * @return the itemModifyTime
	 */
	public Long getItemModifyTime() {
		return itemModifyTime;
	}

	/**
	 * @param itemModifyTime
	 *            the itemModifyTime to set
	 */
	public void setItemModifyTime(Long itemModifyTime) {
		this.itemModifyTime = itemModifyTime;
	}

}
