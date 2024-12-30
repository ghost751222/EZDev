package com.consilium.domain;

import java.io.Serializable;

public class ContentReplyHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5729627863042690597L;
	private int formVer;
	private String actionId;
	private String processType;
	private String processTime;
	private String processOther;
	private String processNote;
	private String replyId;
	private String replyTime;
	private String replyUnitType;
	private String replyUnitCode;
	private String lastReplyTime;
	private String status;
	private String actionStep;
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
	 * @return the processType
	 */
	public String getProcessType() {
		return processType;
	}
	/**
	 * @param processType the processType to set
	 */
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	/**
	 * @return the processTime
	 */
	public String getProcessTime() {
		return processTime;
	}
	/**
	 * @param processTime the processTime to set
	 */
	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}
	/**
	 * @return the processOther
	 */
	public String getProcessOther() {
		return processOther;
	}
	/**
	 * @param processOther the processOther to set
	 */
	public void setProcessOther(String processOther) {
		this.processOther = processOther;
	}
	/**
	 * @return the processNote
	 */
	public String getProcessNote() {
		return processNote;
	}
	/**
	 * @param processNote the processNote to set
	 */
	public void setProcessNote(String processNote) {
		this.processNote = processNote;
	}
	/**
	 * @return the replyId
	 */
	public String getReplyId() {
		return replyId;
	}
	/**
	 * @param replyId the replyId to set
	 */
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	/**
	 * @return the replyTime
	 */
	public String getReplyTime() {
		return replyTime;
	}
	/**
	 * @param replyTime the replyTime to set
	 */
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	/**
	 * @return the replyUnitType
	 */
	public String getReplyUnitType() {
		return replyUnitType;
	}
	/**
	 * @param replyUnitType the replyUnitType to set
	 */
	public void setReplyUnitType(String replyUnitType) {
		this.replyUnitType = replyUnitType;
	}
	/**
	 * @return the replyUnitCode
	 */
	public String getReplyUnitCode() {
		return replyUnitCode;
	}
	/**
	 * @param replyUnitCode the replyUnitCode to set
	 */
	public void setReplyUnitCode(String replyUnitCode) {
		this.replyUnitCode = replyUnitCode;
	}
	/**
	 * @return the lastReplyTime
	 */
	public String getLastReplyTime() {
		return lastReplyTime;
	}
	/**
	 * @param lastReplyTime the lastReplyTime to set
	 */
	public void setLastReplyTime(String lastReplyTime) {
		this.lastReplyTime = lastReplyTime;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the actionStep
	 */
	public String getActionStep() {
		return actionStep;
	}
	/**
	 * @param actionStep the actionStep to set
	 */
	public void setActionStep(String actionStep) {
		this.actionStep = actionStep;
	}
	
}
