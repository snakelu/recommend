package com.roman.recommend.entity;

/**
 * imei和id(必须是数字)的映射
 * 
 * @author lyhcc
 * @version 0.0.1 2017/12/4
 */
public class Imei {

	private Long imeiId;

	private String imei;

	/**
	 * @return the imeiId
	 */
	public Long getImeiId() {
		return imeiId;
	}

	/**
	 * @param imeiId
	 *            the imeiId to set
	 */
	public void setImeiId(Long imeiId) {
		this.imeiId = imeiId;
	}

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

}
