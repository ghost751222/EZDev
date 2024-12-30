package com.consilium.domain;

import java.io.Serializable;

public class CaseFlow implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4033442901339735432L;
	private String formType;
	private int formver;
	private String actionId;
	private int processId;
	private String inProcess;
	private int approvenGroupId;
	private String approvenStatus;
	private String approvenFastFlag;
	private String approvenDescription;
	private int approvenMsgId;
	private String approvenTime;
	private String approvenUserId;
	private String approvenUnitType;
	private String approvenUnitCode;
	private String approvenCityCode;
	private String receiveStatus;
	private String receiveFastFlag;
	private String receiveDescription;
	private int receiveMsgId;
	private String receiveTime;
	private String receiveUserId;
	private String receiveUnitType;
	private String receiveUnitCode;
	private int rejectMsgId;
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
	private String deadline;
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
	 * @return the processId
	 */
	public int getProcessId() {
		return processId;
	}
	/**
	 * @param processId the processId to set
	 */
	public void setProcessId(int processId) {
		this.processId = processId;
	}
	/**
	 * @return the inProcess
	 */
	public String getInProcess() {
		return inProcess;
	}
	/**
	 * @param inProcess the inProcess to set
	 */
	public void setInProcess(String inProcess) {
		this.inProcess = inProcess;
	}
	/**
	 * @return the approvenGroupId
	 */
	public int getApprovenGroupId() {
		return approvenGroupId;
	}
	/**
	 * @param approvenGroupId the approvenGroupId to set
	 */
	public void setApprovenGroupId(int approvenGroupId) {
		this.approvenGroupId = approvenGroupId;
	}
	/**
	 * @return the approvenStatus
	 */
	public String getApprovenStatus() {
		return approvenStatus;
	}
	/**
	 * @param approvenStatus the approvenStatus to set
	 */
	public void setApprovenStatus(String approvenStatus) {
		this.approvenStatus = approvenStatus;
	}
	/**
	 * @return the approvenFastFlag
	 */
	public String getApprovenFastFlag() {
		return approvenFastFlag;
	}
	/**
	 * @param approvenFastFlag the approvenFastFlag to set
	 */
	public void setApprovenFastFlag(String approvenFastFlag) {
		this.approvenFastFlag = approvenFastFlag;
	}
	/**
	 * @return the approvenDescription
	 */
	public String getApprovenDescription() {
		return approvenDescription;
	}
	/**
	 * @param approvenDescription the approvenDescription to set
	 */
	public void setApprovenDescription(String approvenDescription) {
		this.approvenDescription = approvenDescription;
	}
	/**
	 * @return the approvenMsgId
	 */
	public int getApprovenMsgId() {
		return approvenMsgId;
	}
	/**
	 * @param approvenMsgId the approvenMsgId to set
	 */
	public void setApprovenMsgId(int approvenMsgId) {
		this.approvenMsgId = approvenMsgId;
	}
	/**
	 * @return the approvenTime
	 */
	public String getApprovenTime() {
		return approvenTime;
	}
	/**
	 * @param approvenTime the approvenTime to set
	 */
	public void setApprovenTime(String approvenTime) {
		this.approvenTime = approvenTime;
	}
	/**
	 * @return the approvenUserId
	 */
	public String getApprovenUserId() {
		return approvenUserId;
	}
	/**
	 * @param approvenUserId the approvenUserId to set
	 */
	public void setApprovenUserId(String approvenUserId) {
		this.approvenUserId = approvenUserId;
	}
	/**
	 * @return the approvenUnitType
	 */
	public String getApprovenUnitType() {
		return approvenUnitType;
	}
	/**
	 * @param approvenUnitType the approvenUnitType to set
	 */
	public void setApprovenUnitType(String approvenUnitType) {
		this.approvenUnitType = approvenUnitType;
	}
	/**
	 * @return the approvenUnitCode
	 */
	public String getApprovenUnitCode() {
		return approvenUnitCode;
	}
	/**
	 * @param approvenUnitCode the approvenUnitCode to set
	 */
	public void setApprovenUnitCode(String approvenUnitCode) {
		this.approvenUnitCode = approvenUnitCode;
	}
	/**
	 * @return the approvenCityCode
	 */
	public String getApprovenCityCode() {
		return approvenCityCode;
	}
	/**
	 * @param approvenCityCode the approvenCityCode to set
	 */
	public void setApprovenCityCode(String approvenCityCode) {
		this.approvenCityCode = approvenCityCode;
	}
	/**
	 * @return the receiveStatus
	 */
	public String getReceiveStatus() {
		return receiveStatus;
	}
	/**
	 * @param receiveStatus the receiveStatus to set
	 */
	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	/**
	 * @return the receiveFastFlag
	 */
	public String getReceiveFastFlag() {
		return receiveFastFlag;
	}
	/**
	 * @param receiveFastFlag the receiveFastFlag to set
	 */
	public void setReceiveFastFlag(String receiveFastFlag) {
		this.receiveFastFlag = receiveFastFlag;
	}
	/**
	 * @return the receiveDescription
	 */
	public String getReceiveDescription() {
		return receiveDescription;
	}
	/**
	 * @param receiveDescription the receiveDescription to set
	 */
	public void setReceiveDescription(String receiveDescription) {
		this.receiveDescription = receiveDescription;
	}
	/**
	 * @return the receiveMsgId
	 */
	public int getReceiveMsgId() {
		return receiveMsgId;
	}
	/**
	 * @param receiveMsgId the receiveMsgId to set
	 */
	public void setReceiveMsgId(int receiveMsgId) {
		this.receiveMsgId = receiveMsgId;
	}
	/**
	 * @return the receiveTime
	 */
	public String getReceiveTime() {
		return receiveTime;
	}
	/**
	 * @param receiveTime the receiveTime to set
	 */
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	/**
	 * @return the receiveUserId
	 */
	public String getReceiveUserId() {
		return receiveUserId;
	}
	/**
	 * @param receiveUserId the receiveUserId to set
	 */
	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	/**
	 * @return the receiveUnitType
	 */
	public String getReceiveUnitType() {
		return receiveUnitType;
	}
	/**
	 * @param receiveUnitType the receiveUnitType to set
	 */
	public void setReceiveUnitType(String receiveUnitType) {
		this.receiveUnitType = receiveUnitType;
	}
	/**
	 * @return the receiveUnitCode
	 */
	public String getReceiveUnitCode() {
		return receiveUnitCode;
	}
	/**
	 * @param receiveUnitCode the receiveUnitCode to set
	 */
	public void setReceiveUnitCode(String receiveUnitCode) {
		this.receiveUnitCode = receiveUnitCode;
	}
	/**
	 * @return the rejectMsgId
	 */
	public int getRejectMsgId() {
		return rejectMsgId;
	}
	/**
	 * @param rejectMsgId the rejectMsgId to set
	 */
	public void setRejectMsgId(int rejectMsgId) {
		this.rejectMsgId = rejectMsgId;
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
	/**
	 * @return the deadline
	 */
	public String getDeadline() {
		return deadline;
	}
	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	
}
