package com.consilium.domain;

import java.io.Serializable;

public class QASubCategory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6114796309831732526L;
	private int subCatId;
	private int catId;
	private String subCatDisplay;
	private String createTime;
	private String creator;
	private String lastModifier;
	private String lastUpdateTime;
	private int orderId;
	/**
	 * @return the subCatId
	 */
	public int getSubCatId() {
		return subCatId;
	}
	/**
	 * @param subCatId the subCatId to set
	 */
	public void setSubCatId(int subCatId) {
		this.subCatId = subCatId;
	}
	/**
	 * @return the catId
	 */
	public int getCatId() {
		return catId;
	}
	/**
	 * @param catId the catId to set
	 */
	public void setCatId(int catId) {
		this.catId = catId;
	}
	/**
	 * @return the subCatDisplay
	 */
	public String getSubCatDisplay() {
		return subCatDisplay;
	}
	/**
	 * @param subCatDisplay the subCatDisplay to set
	 */
	public void setSubCatDisplay(String subCatDisplay) {
		this.subCatDisplay = subCatDisplay;
	}
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the lastModifier
	 */
	public String getLastModifier() {
		return lastModifier;
	}
	/**
	 * @param lastModifier the lastModifier to set
	 */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	/**
	 * @return the lastUpdateTime
	 */
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	/**
	 * @param lastUpdateTime the lastUpdateTime to set
	 */
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	/**
	 * @return the orderId
	 */
	public int getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
}
