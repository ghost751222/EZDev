package com.consilium.domain;

import java.io.Serializable;

public class M7480Call implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4689506495457272888L;
	private String m7480CallId;
	private String callClId;
	private String callDNIS;
	private String agentId;
	private String userId;
	private String station;
	private String agentName;
	private String agentExtension;
	private String callTime;
	private String answerTime;
	private String callDisconnectionTime;
	private String endPCPTime;
	private int callIVRDuration;
	private int callWaitingDuration;
	private int callAgentDuration;
	private int callTalkingDuration;
	private int callPCPDuration;
	private int callToTransfer;
	private int callDisconnectionStatus;
	private int callDisconnectionParty;
	private int acpCallType;
	private int serviceIniCallNumber;
	private int serviceWaitCallNumber;
	private int serviceRsvCallNumber;
	private int serviceACDCallNumber;
	private int serviceHoldCallNumber;
	private int servicePCPCallNumber;
	private String createTime;
	private String lastUpdateTime;
	/**
	 * @return the m7480CallId
	 */
	public String getM7480CallId() {
		return m7480CallId;
	}
	/**
	 * @param m7480CallId the m7480CallId to set
	 */
	public void setM7480CallId(String m7480CallId) {
		this.m7480CallId = m7480CallId;
	}
	/**
	 * @return the callClId
	 */
	public String getCallClId() {
		return callClId;
	}
	/**
	 * @param callClId the callClId to set
	 */
	public void setCallClId(String callClId) {
		this.callClId = callClId;
	}
	/**
	 * @return the callDNIS
	 */
	public String getCallDNIS() {
		return callDNIS;
	}
	/**
	 * @param callDNIS the callDNIS to set
	 */
	public void setCallDNIS(String callDNIS) {
		this.callDNIS = callDNIS;
	}
	/**
	 * @return the agentId
	 */
	public String getAgentId() {
		return agentId;
	}
	/**
	 * @param agentId the agentId to set
	 */
	public void setAgentId(String agentId) {
		this.agentId = agentId;
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
	 * @return the station
	 */
	public String getStation() {
		return station;
	}
	/**
	 * @param station the station to set
	 */
	public void setStation(String station) {
		this.station = station;
	}
	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}
	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	/**
	 * @return the agentExtension
	 */
	public String getAgentExtension() {
		return agentExtension;
	}
	/**
	 * @param agentExtension the agentExtension to set
	 */
	public void setAgentExtension(String agentExtension) {
		this.agentExtension = agentExtension;
	}
	/**
	 * @return the callTime
	 */
	public String getCallTime() {
		return callTime;
	}
	/**
	 * @param callTime the callTime to set
	 */
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	/**
	 * @return the answerTime
	 */
	public String getAnswerTime() {
		return answerTime;
	}
	/**
	 * @param answerTime the answerTime to set
	 */
	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
	}
	/**
	 * @return the callDisconnectionTime
	 */
	public String getCallDisconnectionTime() {
		return callDisconnectionTime;
	}
	/**
	 * @param callDisconnectionTime the callDisconnectionTime to set
	 */
	public void setCallDisconnectionTime(String callDisconnectionTime) {
		this.callDisconnectionTime = callDisconnectionTime;
	}
	/**
	 * @return the endPCPTime
	 */
	public String getEndPCPTime() {
		return endPCPTime;
	}
	/**
	 * @param endPCPTime the endPCPTime to set
	 */
	public void setEndPCPTime(String endPCPTime) {
		this.endPCPTime = endPCPTime;
	}
	/**
	 * @return the callIVRDuration
	 */
	public int getCallIVRDuration() {
		return callIVRDuration;
	}
	/**
	 * @param callIVRDuration the callIVRDuration to set
	 */
	public void setCallIVRDuration(int callIVRDuration) {
		this.callIVRDuration = callIVRDuration;
	}
	/**
	 * @return the callWaitingDuration
	 */
	public int getCallWaitingDuration() {
		return callWaitingDuration;
	}
	/**
	 * @param callWaitingDuration the callWaitingDuration to set
	 */
	public void setCallWaitingDuration(int callWaitingDuration) {
		this.callWaitingDuration = callWaitingDuration;
	}
	/**
	 * @return the callAgentDuration
	 */
	public int getCallAgentDuration() {
		return callAgentDuration;
	}
	/**
	 * @param callAgentDuration the callAgentDuration to set
	 */
	public void setCallAgentDuration(int callAgentDuration) {
		this.callAgentDuration = callAgentDuration;
	}
	/**
	 * @return the callTalkingDuration
	 */
	public int getCallTalkingDuration() {
		return callTalkingDuration;
	}
	/**
	 * @param callTalkingDuration the callTalkingDuration to set
	 */
	public void setCallTalkingDuration(int callTalkingDuration) {
		this.callTalkingDuration = callTalkingDuration;
	}
	/**
	 * @return the callPCPDuration
	 */
	public int getCallPCPDuration() {
		return callPCPDuration;
	}
	/**
	 * @param callPCPDuration the callPCPDuration to set
	 */
	public void setCallPCPDuration(int callPCPDuration) {
		this.callPCPDuration = callPCPDuration;
	}
	/**
	 * @return the callToTransfer
	 */
	public int getCallToTransfer() {
		return callToTransfer;
	}
	/**
	 * @param callToTransfer the callToTransfer to set
	 */
	public void setCallToTransfer(int callToTransfer) {
		this.callToTransfer = callToTransfer;
	}
	/**
	 * @return the callDisconnectionStatus
	 */
	public int getCallDisconnectionStatus() {
		return callDisconnectionStatus;
	}
	/**
	 * @param callDisconnectionStatus the callDisconnectionStatus to set
	 */
	public void setCallDisconnectionStatus(int callDisconnectionStatus) {
		this.callDisconnectionStatus = callDisconnectionStatus;
	}
	/**
	 * @return the callDisconnectionParty
	 */
	public int getCallDisconnectionParty() {
		return callDisconnectionParty;
	}
	/**
	 * @param callDisconnectionParty the callDisconnectionParty to set
	 */
	public void setCallDisconnectionParty(int callDisconnectionParty) {
		this.callDisconnectionParty = callDisconnectionParty;
	}
	/**
	 * @return the acpCallType
	 */
	public int getAcpCallType() {
		return acpCallType;
	}
	/**
	 * @param acpCallType the acpCallType to set
	 */
	public void setAcpCallType(int acpCallType) {
		this.acpCallType = acpCallType;
	}
	/**
	 * @return the serviceIniCallNumber
	 */
	public int getServiceIniCallNumber() {
		return serviceIniCallNumber;
	}
	/**
	 * @param serviceIniCallNumber the serviceIniCallNumber to set
	 */
	public void setServiceIniCallNumber(int serviceIniCallNumber) {
		this.serviceIniCallNumber = serviceIniCallNumber;
	}
	/**
	 * @return the serviceWaitCallNumber
	 */
	public int getServiceWaitCallNumber() {
		return serviceWaitCallNumber;
	}
	/**
	 * @param serviceWaitCallNumber the serviceWaitCallNumber to set
	 */
	public void setServiceWaitCallNumber(int serviceWaitCallNumber) {
		this.serviceWaitCallNumber = serviceWaitCallNumber;
	}
	/**
	 * @return the serviceRsvCallNumber
	 */
	public int getServiceRsvCallNumber() {
		return serviceRsvCallNumber;
	}
	/**
	 * @param serviceRsvCallNumber the serviceRsvCallNumber to set
	 */
	public void setServiceRsvCallNumber(int serviceRsvCallNumber) {
		this.serviceRsvCallNumber = serviceRsvCallNumber;
	}
	/**
	 * @return the serviceACDCallNumber
	 */
	public int getServiceACDCallNumber() {
		return serviceACDCallNumber;
	}
	/**
	 * @param serviceACDCallNumber the serviceACDCallNumber to set
	 */
	public void setServiceACDCallNumber(int serviceACDCallNumber) {
		this.serviceACDCallNumber = serviceACDCallNumber;
	}
	/**
	 * @return the serviceHoldCallNumber
	 */
	public int getServiceHoldCallNumber() {
		return serviceHoldCallNumber;
	}
	/**
	 * @param serviceHoldCallNumber the serviceHoldCallNumber to set
	 */
	public void setServiceHoldCallNumber(int serviceHoldCallNumber) {
		this.serviceHoldCallNumber = serviceHoldCallNumber;
	}
	/**
	 * @return the servicePCPCallNumber
	 */
	public int getServicePCPCallNumber() {
		return servicePCPCallNumber;
	}
	/**
	 * @param servicePCPCallNumber the servicePCPCallNumber to set
	 */
	public void setServicePCPCallNumber(int servicePCPCallNumber) {
		this.servicePCPCallNumber = servicePCPCallNumber;
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
