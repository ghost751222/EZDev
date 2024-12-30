package com.consilium.domain;

import java.io.Serializable;

public class ContentConfirmHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3011220674986019540L;
	private int formVer;
	private String actionId;
	private String confirmType;
	private String confirmDate;
	private int confirmDays;
	private String confirmNote;
	private String confirmId;
	private String confirmTime;
	private String confirmUnitType;
	private String confirmUnitCode;
	private String lastConfirmTime;
	/**
	 * @return the formVer
	 */
	public int getFormVer() {
		return formVer;
	}
	/**
	 * @param formVer the formVer to set
	 */
	public void setFormVer(int formVer) {
		this.formVer = formVer;
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
	 * @return the confirmType
	 */
	public String getConfirmType() {
		return confirmType;
	}
	/**
	 * @param confirmType the confirmType to set
	 */
	public void setConfirmType(String confirmType) {
		this.confirmType = confirmType;
	}
	/**
	 * @return the confirmDate
	 */
	public String getConfirmDate() {
		return confirmDate;
	}
	/**
	 * @param confirmDate the confirmDate to set
	 */
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	/**
	 * @return the confirmDays
	 */
	public int getConfirmDays() {
		return confirmDays;
	}
	/**
	 * @param confirmDays the confirmDays to set
	 */
	public void setConfirmDays(int confirmDays) {
		this.confirmDays = confirmDays;
	}
	/**
	 * @return the confirmNote
	 */
	public String getConfirmNote() {
		return confirmNote;
	}
	/**
	 * @param confirmNote the confirmNote to set
	 */
	public void setConfirmNote(String confirmNote) {
		this.confirmNote = confirmNote;
	}
	/**
	 * @return the confirmId
	 */
	public String getConfirmId() {
		return confirmId;
	}
	/**
	 * @param confirmId the confirmId to set
	 */
	public void setConfirmId(String confirmId) {
		this.confirmId = confirmId;
	}
	/**
	 * @return the confirmTime
	 */
	public String getConfirmTime() {
		return confirmTime;
	}
	/**
	 * @param confirmTime the confirmTime to set
	 */
	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}
	/**
	 * @return the confirmUnitType
	 */
	public String getConfirmUnitType() {
		return confirmUnitType;
	}
	/**
	 * @param confirmUnitType the confirmUnitType to set
	 */
	public void setConfirmUnitType(String confirmUnitType) {
		this.confirmUnitType = confirmUnitType;
	}
	/**
	 * @return the confirmUnitCode
	 */
	public String getConfirmUnitCode() {
		return confirmUnitCode;
	}
	/**
	 * @param confirmUnitCode the confirmUnitCode to set
	 */
	public void setConfirmUnitCode(String confirmUnitCode) {
		this.confirmUnitCode = confirmUnitCode;
	}
	/**
	 * @return the lastConfirmTime
	 */
	public String getLastConfirmTime() {
		return lastConfirmTime;
	}
	/**
	 * @param lastConfirmTime the lastConfirmTime to set
	 */
	public void setLastConfirmTime(String lastConfirmTime) {
		this.lastConfirmTime = lastConfirmTime;
	}
	
}
