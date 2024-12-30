package com.consilium.domain;

import java.io.Serializable;

public class AccessLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8811372712570190099L;
	
	private int seqNo; 			//序號
	private String mainId;		//主分類編號
	private String subId;		//次分類編號
	private String userId;		//使用者編號
	private String unitType; 	//使用單位類別
	private String unitName;	//單位名稱
	private String accessType;	//存取類別
	private String logType;		//資料類別
	private String viewUrl;		//資料檢視URL
	private String remark;		//補充說明
	private String accessTime;	//存取時間
	private String hashCode;	//hash code 資料驗證用
	private String accessTimeStr;	//存取時間YYYYMMDDHH24MISS
	/**
	 * @return the seqNo
	 */
	public int getSeqNo() {
		return seqNo;
	}
	/**
	 * @param seqNo the seqNo to set
	 */
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	/**
	 * @return the mainId
	 */
	public String getMainId() {
		return mainId;
	}
	/**
	 * @param mainId the mainId to set
	 */
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	/**
	 * @return the subId
	 */
	public String getSubId() {
		return subId;
	}
	/**
	 * @param subId the subId to set
	 */
	public void setSubId(String subId) {
		this.subId = subId;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the unitType
	 */
	public String getUnitType() {
		return unitType;
	}
	/**
	 * @param unitType the unitType to set
	 */
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	/**
	 * @return the accessType
	 */
	public String getAccessType() {
		return accessType;
	}
	/**
	 * @param accessType the accessType to set
	 */
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	/**
	 * @return the logType
	 */
	public String getLogType() {
		return logType;
	}
	/**
	 * @param logType the logType to set
	 */
	public void setLogType(String logType) {
		this.logType = logType;
	}
	/**
	 * @return the viewUrl
	 */
	public String getViewUrl() {
		return viewUrl;
	}
	/**
	 * @param viewUrl the viewUrl to set
	 */
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the accessTime
	 */
	public String getAccessTime() {
		return accessTime;
	}
	/**
	 * @param accessTime the accessTime to set
	 */
	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}
	/**
	 * @return the hashCode
	 */
	public String getHashCode() {
		return hashCode;
	}
	/**
	 * @param hashCode the hashCode to set
	 */
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}
	/**
	 * @return the accessTimeStr
	 */
	public String getAccessTimeStr() {
		return accessTimeStr;
	}
	/**
	 * @param accessTimeStr the accessTimeStr to set
	 */
	public void setAccessTimeStr(String accessTimeStr) {
		this.accessTimeStr = accessTimeStr;
	}
}
