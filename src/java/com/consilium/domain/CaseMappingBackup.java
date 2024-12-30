package com.consilium.domain;

import java.io.Serializable;

public class CaseMappingBackup implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5205652555645630888L;
	private String formType;
	private int formver;
	private String actionId;
	private String mappingFormType;
	private int mappingFormVer;
	private String mappingActionId;
	private int mappingType;
	private String createTime;
	private String creatorId;
	private String creatorUnitType;
	private String creatorUnitCode;
	private String creatorCityCode;
	/**
	 * @return the formType
	 */
	public String getFormType() {
		return formType;
	}
	/**
	 * @param formType the formType to set
	 */
	public void setFormType(String formType) {
		this.formType = formType;
	}
	/**
	 * @return the formver
	 */
	public int getFormver() {
		return formver;
	}
	/**
	 * @param formver the formver to set
	 */
	public void setFormver(int formver) {
		this.formver = formver;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public String getMappingFormType() {
		return mappingFormType;
	}
	public void setMappingFormType(String mappingFormType) {
		this.mappingFormType = mappingFormType;
	}
	public int getMappingFormVer() {
		return mappingFormVer;
	}
	public void setMappingFormVer(int mappingFormVer) {
		this.mappingFormVer = mappingFormVer;
	}
	public String getMappingActionId() {
		return mappingActionId;
	}
	public void setMappingActionId(String mappingActionId) {
		this.mappingActionId = mappingActionId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getMappingType() {
		return mappingType;
	}
	public void setMappingType(int mappingType) {
		this.mappingType = mappingType;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreatorUnitType() {
		return creatorUnitType;
	}
	public void setCreatorUnitType(String creatorUnitType) {
		this.creatorUnitType = creatorUnitType;
	}
	public String getCreatorUnitCode() {
		return creatorUnitCode;
	}
	public void setCreatorUnitCode(String creatorUnitCode) {
		this.creatorUnitCode = creatorUnitCode;
	}
	public String getCreatorCityCode() {
		return creatorCityCode;
	}
	public void setCreatorCityCode(String creatorCityCode) {
		this.creatorCityCode = creatorCityCode;
	}
}
