package com.consilium.domain;

import java.io.Serializable;

public class CaseReponseRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -243687731093638648L;
	private int responseNo;
	private String formType;
	private int formVer;
	private String actionId;
	private String responseTime;
	private String reportType;
	private String description;
	private String isOpenCase;
	private String workerName;
	private String caseId;
	private String responseFlag;
	private String responseProcessId;
	private String responseUnitType;
	private String responseUnitCode;
	private String responseCityCode;
	private String createTime;
	private String creatorId;
	private String creatorUnitType;
	private String creatorUnitCode;
	private String creatorCityCode;
	private String lastModifier;
	private String lastModifierUnitType;
	private String lastModifierUnitCode;
	private String lastModifierCityCode;
	private String lastUpdateTime;
	
	public int getResponseNo() {
		return responseNo;
	}
	public void setResponseNo(int responseNo) {
		this.responseNo = responseNo;
	}
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
	public int getFormVer() {
		return formVer;
	}
	public void setFormVer(int formVer) {
		this.formVer = formVer;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIsOpenCase() {
		return isOpenCase;
	}
	public void setIsOpenCase(String isOpenCase) {
		this.isOpenCase = isOpenCase;
	}
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getResponseFlag() {
		return responseFlag;
	}
	public void setResponseFlag(String responseFlag) {
		this.responseFlag = responseFlag;
	}
	public String getResponseProcessId() {
		return responseProcessId;
	}
	public void setResponseProcessId(String responseProcessId) {
		this.responseProcessId = responseProcessId;
	}
	/**
	 * @return the responseUnitType
	 */
	public String getResponseUnitType() {
		return responseUnitType;
	}
	/**
	 * @param responseUnitType the responseUnitType to set
	 */
	public void setResponseUnitType(String responseUnitType) {
		this.responseUnitType = responseUnitType;
	}
	/**
	 * @return the responseUnitCode
	 */
	public String getResponseUnitCode() {
		return responseUnitCode;
	}
	/**
	 * @param responseUnitCode the responseUnitCode to set
	 */
	public void setResponseUnitCode(String responseUnitCode) {
		this.responseUnitCode = responseUnitCode;
	}
	/**
	 * @return the responseCityCode
	 */
	public String getResponseCityCode() {
		return responseCityCode;
	}
	/**
	 * @param responseCityCode the responseCityCode to set
	 */
	public void setResponseCityCode(String responseCityCode) {
		this.responseCityCode = responseCityCode;
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
	 * @return the lastModifierUnitType
	 */
	public String getLastModifierUnitType() {
		return lastModifierUnitType;
	}
	/**
	 * @param lastModifierUnitType the lastModifierUnitType to set
	 */
	public void setLastModifierUnitType(String lastModifierUnitType) {
		this.lastModifierUnitType = lastModifierUnitType;
	}
	/**
	 * @return the lastModifierUnitCode
	 */
	public String getLastModifierUnitCode() {
		return lastModifierUnitCode;
	}
	/**
	 * @param lastModifierUnitCode the lastModifierUnitCode to set
	 */
	public void setLastModifierUnitCode(String lastModifierUnitCode) {
		this.lastModifierUnitCode = lastModifierUnitCode;
	}
	/**
	 * @return the lastModifierCityCode
	 */
	public String getLastModifierCityCode() {
		return lastModifierCityCode;
	}
	/**
	 * @param lastModifierCityCode the lastModifierCityCode to set
	 */
	public void setLastModifierCityCode(String lastModifierCityCode) {
		this.lastModifierCityCode = lastModifierCityCode;
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
	
	
	
}
