package com.consilium.domain;

import java.io.Serializable;
public class ResourceSubcategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4607843621024180979L;
	private int catId;
	private int subCatId;
	private String display;
	private String createTime;
	private String creatorId;
	private String creatorUnitType;
	private String creatorUnitCode;
	private String creatorCityCode;
	private String lastModifier;
	private String lastUpdatTime;
	private int orderBy;
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
	 * @return the display
	 */
	public String getDisplay() {
		return display;
	}
	/**
	 * @param display the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
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
	 * @return the creatorId
	 */
	public String getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the creatorUnitType
	 */
	public String getCreatorUnitType() {
		return creatorUnitType;
	}
	/**
	 * @param creatorUnitType the creatorUnitType to set
	 */
	public void setCreatorUnitType(String creatorUnitType) {
		this.creatorUnitType = creatorUnitType;
	}
	/**
	 * @return the creatorUnitCode
	 */
	public String getCreatorUnitCode() {
		return creatorUnitCode;
	}
	/**
	 * @param creatorUnitCode the creatorUnitCode to set
	 */
	public void setCreatorUnitCode(String creatorUnitCode) {
		this.creatorUnitCode = creatorUnitCode;
	}
	/**
	 * @return the creatorCityCode
	 */
	public String getCreatorCityCode() {
		return creatorCityCode;
	}
	/**
	 * @param creatorCityCode the creatorCityCode to set
	 */
	public void setCreatorCityCode(String creatorCityCode) {
		this.creatorCityCode = creatorCityCode;
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
	 * @return the lastUpdatTime
	 */
	public String getLastUpdatTime() {
		return lastUpdatTime;
	}
	/**
	 * @param lastUpdatTime the lastUpdatTime to set
	 */
	public void setLastUpdatTime(String lastUpdatTime) {
		this.lastUpdatTime = lastUpdatTime;
	}
	/**
	 * @return the orderBy
	 */
	public int getOrderBy() {
		return orderBy;
	}
	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}
}
