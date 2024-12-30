package com.consilium.domain;

import java.io.Serializable;

public class M7480AgentCodeStatus implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6300414907465413303L;
	private String agentId;
	private String userId;
	private String status;
	private String agentName;
	private String beginTime;
	private int activity;
	private int code;
	private int totalDuration;
	private int nbrPeriods;
	private int maxDuration;
	private int updated;
	private String createTime;
	private String lastUpdateTime;
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
	 * @return the activity
	 */
	public int getActivity() {
		return activity;
	}
	/**
	 * @param activity the activity to set
	 */
	public void setActivity(int activity) {
		this.activity = activity;
	}
	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
	/**
	 * @return the totalDuration
	 */
	public int getTotalDuration() {
		return totalDuration;
	}
	/**
	 * @param totalDuration the totalDuration to set
	 */
	public void setTotalDuration(int totalDuration) {
		this.totalDuration = totalDuration;
	}
	/**
	 * @return the nbrPeriods
	 */
	public int getNbrPeriods() {
		return nbrPeriods;
	}
	/**
	 * @param nbrPeriods the nbrPeriods to set
	 */
	public void setNbrPeriods(int nbrPeriods) {
		this.nbrPeriods = nbrPeriods;
	}
	/**
	 * @return the maxDuration
	 */
	public int getMaxDuration() {
		return maxDuration;
	}
	/**
	 * @param maxDuration the maxDuration to set
	 */
	public void setMaxDuration(int maxDuration) {
		this.maxDuration = maxDuration;
	}
	/**
	 * @return the updated
	 */
	public int getUpdated() {
		return updated;
	}
	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(int updated) {
		this.updated = updated;
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
