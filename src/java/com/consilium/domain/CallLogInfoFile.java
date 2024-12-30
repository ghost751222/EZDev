package com.consilium.domain;

import java.io.Serializable;

public class CallLogInfoFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5985132962328310732L;
	private int fileNo;
	private String actionId;
	private String fileName;
	private int fileSize;
	private String fileContent;
	private String createTime;
	private String createId;
	private String creatorUnitType;
	private String creatorUnitcode;
	private String lastModifier;
	private String lastModifierUnitType;
	private String lstUpdateTime;
	private int isActive;
	/**
	 * @return the fileNo
	 */
	public int getFileNo() {
		return fileNo;
	}
	/**
	 * @param fileNo the fileNo to set
	 */
	public void setFileNo(int fileNo) {
		this.fileNo = fileNo;
	}
	/**
	 * @return the actionId
	 */
	public String getActionId() {
		return actionId;
	}
	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the fileSize
	 */
	public int getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	/**
	 * @return the fileContent
	 */
	public String getFileContent() {
		return fileContent;
	}
	/**
	 * @param fileContent the fileContent to set
	 */
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
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
	 * @return the createId
	 */
	public String getCreateId() {
		return createId;
	}
	/**
	 * @param createId the createId to set
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
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
	 * @return the creatorUnitcode
	 */
	public String getCreatorUnitcode() {
		return creatorUnitcode;
	}
	/**
	 * @param creatorUnitcode the creatorUnitcode to set
	 */
	public void setCreatorUnitcode(String creatorUnitcode) {
		this.creatorUnitcode = creatorUnitcode;
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
	 * @return the lstUpdateTime
	 */
	public String getLstUpdateTime() {
		return lstUpdateTime;
	}
	/**
	 * @param lstUpdateTime the lstUpdateTime to set
	 */
	public void setLstUpdateTime(String lstUpdateTime) {
		this.lstUpdateTime = lstUpdateTime;
	}
	/**
	 * @return the isActive
	 */
	public int getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
}
