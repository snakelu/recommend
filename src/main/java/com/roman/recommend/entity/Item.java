package com.roman.recommend.entity;

import java.io.Serializable;

/**
 * 活动entity
 * 
 * @author lyhcc
 * @version 0.0.1 2017/12/3
 */
public class Item implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 活动id：9499288992
	 */
	private String itemId;

	/**
	 * 活动名称：大夫山烧烤骑行一日游
	 */
	private String title;

	/**
	 * 活动分类Id：户外 > 短途游（id: 102_2）-->关联活动分类表
	 */
	private String cateId;

	/**
	 * 活动标签：骑行、烧烤（id: 101, 121） -->关联活动标签表
	 */
	private String itemTags;

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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the cateId
	 */
	public String getCateId() {
		return cateId;
	}

	/**
	 * @param cateId
	 *            the cateId to set
	 */
	public void setCateId(String cateId) {
		this.cateId = cateId;
	}

	/**
	 * @return the itemTags
	 */
	public String getItemTags() {
		return itemTags;
	}

	/**
	 * @param itemTags
	 *            the itemTags to set
	 */
	public void setItemTags(String itemTags) {
		this.itemTags = itemTags;
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
