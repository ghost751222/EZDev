package com.consilium.domain;

import java.io.Serializable;

public class M7480CallRinging implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5661521081905930527L;
	private String agentId;
	private String userId;
	private String serviceId;
	private String beginTime;
	private int sumRingDurAnwserCall;
	private String createTime;
	private int maxRingDurAnswerCall;
	private int nbrInboundCallAnswer;
	private int nbrInboundCallNotAnswer;
	private int sumRingDurNotAnswerCall;
	private int maxRingDurNotAnswerCall;
	private int sumTalkDurInboundCall;
	private int maxTalkDurInboundCall;
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
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * @return the beginTime
	 */
	public String getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * @return the sumRingDurAnwserCall
	 */
	public int getSumRingDurAnwserCall() {
		return sumRingDurAnwserCall;
	}
	/**
	 * @param sumRingDurAnwserCall the sumRingDurAnwserCall to set
	 */
	public void setSumRingDurAnwserCall(int sumRingDurAnwserCall) {
		this.sumRingDurAnwserCall = sumRingDurAnwserCall;
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
	 * @return the maxRingDurAnswerCall
	 */
	public int getMaxRingDurAnswerCall() {
		return maxRingDurAnswerCall;
	}
	/**
	 * @param maxRingDurAnswerCall the maxRingDurAnswerCall to set
	 */
	public void setMaxRingDurAnswerCall(int maxRingDurAnswerCall) {
		this.maxRingDurAnswerCall = maxRingDurAnswerCall;
	}
	/**
	 * @return the nbrInboundCallAnswer
	 */
	public int getNbrInboundCallAnswer() {
		return nbrInboundCallAnswer;
	}
	/**
	 * @param nbrInboundCallAnswer the nbrInboundCallAnswer to set
	 */
	public void setNbrInboundCallAnswer(int nbrInboundCallAnswer) {
		this.nbrInboundCallAnswer = nbrInboundCallAnswer;
	}
	/**
	 * @return the nbrInboundCallNotAnswer
	 */
	public int getNbrInboundCallNotAnswer() {
		return nbrInboundCallNotAnswer;
	}
	/**
	 * @param nbrInboundCallNotAnswer the nbrInboundCallNotAnswer to set
	 */
	public void setNbrInboundCallNotAnswer(int nbrInboundCallNotAnswer) {
		this.nbrInboundCallNotAnswer = nbrInboundCallNotAnswer;
	}
	/**
	 * @return the sumRingDurNotAnswerCall
	 */
	public int getSumRingDurNotAnswerCall() {
		return sumRingDurNotAnswerCall;
	}
	/**
	 * @param sumRingDurNotAnswerCall the sumRingDurNotAnswerCall to set
	 */
	public void setSumRingDurNotAnswerCall(int sumRingDurNotAnswerCall) {
		this.sumRingDurNotAnswerCall = sumRingDurNotAnswerCall;
	}
	/**
	 * @return the maxRingDurNotAnswerCall
	 */
	public int getMaxRingDurNotAnswerCall() {
		return maxRingDurNotAnswerCall;
	}
	/**
	 * @param maxRingDurNotAnswerCall the maxRingDurNotAnswerCall to set
	 */
	public void setMaxRingDurNotAnswerCall(int maxRingDurNotAnswerCall) {
		this.maxRingDurNotAnswerCall = maxRingDurNotAnswerCall;
	}
	/**
	 * @return the sumTalkDurInboundCall
	 */
	public int getSumTalkDurInboundCall() {
		return sumTalkDurInboundCall;
	}
	/**
	 * @param sumTalkDurInboundCall the sumTalkDurInboundCall to set
	 */
	public void setSumTalkDurInboundCall(int sumTalkDurInboundCall) {
		this.sumTalkDurInboundCall = sumTalkDurInboundCall;
	}
	/**
	 * @return the maxTalkDurInboundCall
	 */
	public int getMaxTalkDurInboundCall() {
		return maxTalkDurInboundCall;
	}
	/**
	 * @param maxTalkDurInboundCall the maxTalkDurInboundCall to set
	 */
	public void setMaxTalkDurInboundCall(int maxTalkDurInboundCall) {
		this.maxTalkDurInboundCall = maxTalkDurInboundCall;
	}
	
}
