package com.consilium.domain;

import java.io.Serializable;

public class M7480AgengStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8690442044773000099L;
	private String agentId;
	private String userId;
	private String station;
	private String beginTime;
	private int beginTimeHour;
	private String agentName;
	private int totalLoginReadyIdleDuration;
	private int totalLoginPCPIdleDuration;
	private int nbrLoginReadyIdlePeriods;
	private int nbrLoginNotReadyIdlePeriods;
	private int nbrLoginPCPIdlePeriods;
	private int nbrLoginBreakIdlePeriods;
	private int maxLoginReadyIdleDuration;
	private int maxLoginNotReadyIdleDuration;
	private int maxLoginPCPIdleDuration;
	private int maxLoginBreakIdleDuration;
	private int update;
	private int nbrForcedReadyPeriods;
	private int nbrForcedLogoutPeriods;
	private int totalDCPInboundDuration;
	private int nbrDCPInboundPeriods;
	private int maxDCPInboundDuration;
	private int totalDCPOutboundDuration;
	private int nbrDCPOutboundPeriods;
	private int maxDCPOutboundDuration;
	private int totalPRivateInDuration;
	private int nbrPrivateInPeriods;
	private int maxPrivateInDuration;
	private int totalPRivateOutDuration;
	private int nbrPrivateOutPeriods;
	private int maxPrivateOutDuration;
	private int totalReservedInboundDuration;
	private int nbrReservedInboundPeriods;
	private int maxReservedInboundDuration;
	private int totalReservedOutboundDuration;
	private int nbrReservedOutboundPeriods;
	private int maxReservedOutboundDuration;
	private int totalHoldInboundDurations;
	private int nbrHoldInboundPeriods;
	private int maxHoldInboundDuration;
	private int totalHoldOutboundDuration;
	private int nbrHoldOutboundPeriods;
	private int maxHoldOutboundDuration;
	private String createTime;
	private String lastUpdateTime;
	private int totalWebChattingDuration;
	private int maxWebChattingDuration;
	private int nbrDCPInboundCallsAnswered;
	private int totalDCPInboundCallsDuration;
	private int offSetValue;
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
	 * @return the beginTimeHour
	 */
	public int getBeginTimeHour() {
		return beginTimeHour;
	}
	/**
	 * @param beginTimeHour the beginTimeHour to set
	 */
	public void setBeginTimeHour(int beginTimeHour) {
		this.beginTimeHour = beginTimeHour;
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
	 * @return the totalLoginReadyIdleDuration
	 */
	public int getTotalLoginReadyIdleDuration() {
		return totalLoginReadyIdleDuration;
	}
	/**
	 * @param totalLoginReadyIdleDuration the totalLoginReadyIdleDuration to set
	 */
	public void setTotalLoginReadyIdleDuration(int totalLoginReadyIdleDuration) {
		this.totalLoginReadyIdleDuration = totalLoginReadyIdleDuration;
	}
	/**
	 * @return the totalLoginPCPIdleDuration
	 */
	public int getTotalLoginPCPIdleDuration() {
		return totalLoginPCPIdleDuration;
	}
	/**
	 * @param totalLoginPCPIdleDuration the totalLoginPCPIdleDuration to set
	 */
	public void setTotalLoginPCPIdleDuration(int totalLoginPCPIdleDuration) {
		this.totalLoginPCPIdleDuration = totalLoginPCPIdleDuration;
	}
	/**
	 * @return the nbrLoginReadyIdlePeriods
	 */
	public int getNbrLoginReadyIdlePeriods() {
		return nbrLoginReadyIdlePeriods;
	}
	/**
	 * @param nbrLoginReadyIdlePeriods the nbrLoginReadyIdlePeriods to set
	 */
	public void setNbrLoginReadyIdlePeriods(int nbrLoginReadyIdlePeriods) {
		this.nbrLoginReadyIdlePeriods = nbrLoginReadyIdlePeriods;
	}
	/**
	 * @return the nbrLoginNotReadyIdlePeriods
	 */
	public int getNbrLoginNotReadyIdlePeriods() {
		return nbrLoginNotReadyIdlePeriods;
	}
	/**
	 * @param nbrLoginNotReadyIdlePeriods the nbrLoginNotReadyIdlePeriods to set
	 */
	public void setNbrLoginNotReadyIdlePeriods(int nbrLoginNotReadyIdlePeriods) {
		this.nbrLoginNotReadyIdlePeriods = nbrLoginNotReadyIdlePeriods;
	}
	/**
	 * @return the nbrLoginPCPIdlePeriods
	 */
	public int getNbrLoginPCPIdlePeriods() {
		return nbrLoginPCPIdlePeriods;
	}
	/**
	 * @param nbrLoginPCPIdlePeriods the nbrLoginPCPIdlePeriods to set
	 */
	public void setNbrLoginPCPIdlePeriods(int nbrLoginPCPIdlePeriods) {
		this.nbrLoginPCPIdlePeriods = nbrLoginPCPIdlePeriods;
	}
	/**
	 * @return the nbrLoginBreakIdlePeriods
	 */
	public int getNbrLoginBreakIdlePeriods() {
		return nbrLoginBreakIdlePeriods;
	}
	/**
	 * @param nbrLoginBreakIdlePeriods the nbrLoginBreakIdlePeriods to set
	 */
	public void setNbrLoginBreakIdlePeriods(int nbrLoginBreakIdlePeriods) {
		this.nbrLoginBreakIdlePeriods = nbrLoginBreakIdlePeriods;
	}
	/**
	 * @return the maxLoginReadyIdleDuration
	 */
	public int getMaxLoginReadyIdleDuration() {
		return maxLoginReadyIdleDuration;
	}
	/**
	 * @param maxLoginReadyIdleDuration the maxLoginReadyIdleDuration to set
	 */
	public void setMaxLoginReadyIdleDuration(int maxLoginReadyIdleDuration) {
		this.maxLoginReadyIdleDuration = maxLoginReadyIdleDuration;
	}
	/**
	 * @return the maxLoginNotReadyIdleDuration
	 */
	public int getMaxLoginNotReadyIdleDuration() {
		return maxLoginNotReadyIdleDuration;
	}
	/**
	 * @param maxLoginNotReadyIdleDuration the maxLoginNotReadyIdleDuration to set
	 */
	public void setMaxLoginNotReadyIdleDuration(int maxLoginNotReadyIdleDuration) {
		this.maxLoginNotReadyIdleDuration = maxLoginNotReadyIdleDuration;
	}
	/**
	 * @return the maxLoginPCPIdleDuration
	 */
	public int getMaxLoginPCPIdleDuration() {
		return maxLoginPCPIdleDuration;
	}
	/**
	 * @param maxLoginPCPIdleDuration the maxLoginPCPIdleDuration to set
	 */
	public void setMaxLoginPCPIdleDuration(int maxLoginPCPIdleDuration) {
		this.maxLoginPCPIdleDuration = maxLoginPCPIdleDuration;
	}
	/**
	 * @return the maxLoginBreakIdleDuration
	 */
	public int getMaxLoginBreakIdleDuration() {
		return maxLoginBreakIdleDuration;
	}
	/**
	 * @param maxLoginBreakIdleDuration the maxLoginBreakIdleDuration to set
	 */
	public void setMaxLoginBreakIdleDuration(int maxLoginBreakIdleDuration) {
		this.maxLoginBreakIdleDuration = maxLoginBreakIdleDuration;
	}
	/**
	 * @return the update
	 */
	public int getUpdate() {
		return update;
	}
	/**
	 * @param update the update to set
	 */
	public void setUpdate(int update) {
		this.update = update;
	}
	/**
	 * @return the nbrForcedReadyPeriods
	 */
	public int getNbrForcedReadyPeriods() {
		return nbrForcedReadyPeriods;
	}
	/**
	 * @param nbrForcedReadyPeriods the nbrForcedReadyPeriods to set
	 */
	public void setNbrForcedReadyPeriods(int nbrForcedReadyPeriods) {
		this.nbrForcedReadyPeriods = nbrForcedReadyPeriods;
	}
	/**
	 * @return the nbrForcedLogoutPeriods
	 */
	public int getNbrForcedLogoutPeriods() {
		return nbrForcedLogoutPeriods;
	}
	/**
	 * @param nbrForcedLogoutPeriods the nbrForcedLogoutPeriods to set
	 */
	public void setNbrForcedLogoutPeriods(int nbrForcedLogoutPeriods) {
		this.nbrForcedLogoutPeriods = nbrForcedLogoutPeriods;
	}
	/**
	 * @return the totalDCPInboundDuration
	 */
	public int getTotalDCPInboundDuration() {
		return totalDCPInboundDuration;
	}
	/**
	 * @param totalDCPInboundDuration the totalDCPInboundDuration to set
	 */
	public void setTotalDCPInboundDuration(int totalDCPInboundDuration) {
		this.totalDCPInboundDuration = totalDCPInboundDuration;
	}
	/**
	 * @return the nbrDCPInboundPeriods
	 */
	public int getNbrDCPInboundPeriods() {
		return nbrDCPInboundPeriods;
	}
	/**
	 * @param nbrDCPInboundPeriods the nbrDCPInboundPeriods to set
	 */
	public void setNbrDCPInboundPeriods(int nbrDCPInboundPeriods) {
		this.nbrDCPInboundPeriods = nbrDCPInboundPeriods;
	}
	/**
	 * @return the maxDCPInboundDuration
	 */
	public int getMaxDCPInboundDuration() {
		return maxDCPInboundDuration;
	}
	/**
	 * @param maxDCPInboundDuration the maxDCPInboundDuration to set
	 */
	public void setMaxDCPInboundDuration(int maxDCPInboundDuration) {
		this.maxDCPInboundDuration = maxDCPInboundDuration;
	}
	/**
	 * @return the totalDCPOutboundDuration
	 */
	public int getTotalDCPOutboundDuration() {
		return totalDCPOutboundDuration;
	}
	/**
	 * @param totalDCPOutboundDuration the totalDCPOutboundDuration to set
	 */
	public void setTotalDCPOutboundDuration(int totalDCPOutboundDuration) {
		this.totalDCPOutboundDuration = totalDCPOutboundDuration;
	}
	/**
	 * @return the nbrDCPOutboundPeriods
	 */
	public int getNbrDCPOutboundPeriods() {
		return nbrDCPOutboundPeriods;
	}
	/**
	 * @param nbrDCPOutboundPeriods the nbrDCPOutboundPeriods to set
	 */
	public void setNbrDCPOutboundPeriods(int nbrDCPOutboundPeriods) {
		this.nbrDCPOutboundPeriods = nbrDCPOutboundPeriods;
	}
	/**
	 * @return the maxDCPOutboundDuration
	 */
	public int getMaxDCPOutboundDuration() {
		return maxDCPOutboundDuration;
	}
	/**
	 * @param maxDCPOutboundDuration the maxDCPOutboundDuration to set
	 */
	public void setMaxDCPOutboundDuration(int maxDCPOutboundDuration) {
		this.maxDCPOutboundDuration = maxDCPOutboundDuration;
	}
	/**
	 * @return the totalPRivateInDuration
	 */
	public int getTotalPRivateInDuration() {
		return totalPRivateInDuration;
	}
	/**
	 * @param totalPRivateInDuration the totalPRivateInDuration to set
	 */
	public void setTotalPRivateInDuration(int totalPRivateInDuration) {
		this.totalPRivateInDuration = totalPRivateInDuration;
	}
	/**
	 * @return the nbrPrivateInPeriods
	 */
	public int getNbrPrivateInPeriods() {
		return nbrPrivateInPeriods;
	}
	/**
	 * @param nbrPrivateInPeriods the nbrPrivateInPeriods to set
	 */
	public void setNbrPrivateInPeriods(int nbrPrivateInPeriods) {
		this.nbrPrivateInPeriods = nbrPrivateInPeriods;
	}
	/**
	 * @return the maxPrivateInDuration
	 */
	public int getMaxPrivateInDuration() {
		return maxPrivateInDuration;
	}
	/**
	 * @param maxPrivateInDuration the maxPrivateInDuration to set
	 */
	public void setMaxPrivateInDuration(int maxPrivateInDuration) {
		this.maxPrivateInDuration = maxPrivateInDuration;
	}
	/**
	 * @return the totalPRivateOutDuration
	 */
	public int getTotalPRivateOutDuration() {
		return totalPRivateOutDuration;
	}
	/**
	 * @param totalPRivateOutDuration the totalPRivateOutDuration to set
	 */
	public void setTotalPRivateOutDuration(int totalPRivateOutDuration) {
		this.totalPRivateOutDuration = totalPRivateOutDuration;
	}
	/**
	 * @return the nbrPrivateOutPeriods
	 */
	public int getNbrPrivateOutPeriods() {
		return nbrPrivateOutPeriods;
	}
	/**
	 * @param nbrPrivateOutPeriods the nbrPrivateOutPeriods to set
	 */
	public void setNbrPrivateOutPeriods(int nbrPrivateOutPeriods) {
		this.nbrPrivateOutPeriods = nbrPrivateOutPeriods;
	}
	/**
	 * @return the maxPrivateOutDuration
	 */
	public int getMaxPrivateOutDuration() {
		return maxPrivateOutDuration;
	}
	/**
	 * @param maxPrivateOutDuration the maxPrivateOutDuration to set
	 */
	public void setMaxPrivateOutDuration(int maxPrivateOutDuration) {
		this.maxPrivateOutDuration = maxPrivateOutDuration;
	}
	/**
	 * @return the totalReservedInboundDuration
	 */
	public int getTotalReservedInboundDuration() {
		return totalReservedInboundDuration;
	}
	/**
	 * @param totalReservedInboundDuration the totalReservedInboundDuration to set
	 */
	public void setTotalReservedInboundDuration(int totalReservedInboundDuration) {
		this.totalReservedInboundDuration = totalReservedInboundDuration;
	}
	/**
	 * @return the nbrReservedInboundPeriods
	 */
	public int getNbrReservedInboundPeriods() {
		return nbrReservedInboundPeriods;
	}
	/**
	 * @param nbrReservedInboundPeriods the nbrReservedInboundPeriods to set
	 */
	public void setNbrReservedInboundPeriods(int nbrReservedInboundPeriods) {
		this.nbrReservedInboundPeriods = nbrReservedInboundPeriods;
	}
	/**
	 * @return the maxReservedInboundDuration
	 */
	public int getMaxReservedInboundDuration() {
		return maxReservedInboundDuration;
	}
	/**
	 * @param maxReservedInboundDuration the maxReservedInboundDuration to set
	 */
	public void setMaxReservedInboundDuration(int maxReservedInboundDuration) {
		this.maxReservedInboundDuration = maxReservedInboundDuration;
	}
	/**
	 * @return the totalReservedOutboundDuration
	 */
	public int getTotalReservedOutboundDuration() {
		return totalReservedOutboundDuration;
	}
	/**
	 * @param totalReservedOutboundDuration the totalReservedOutboundDuration to set
	 */
	public void setTotalReservedOutboundDuration(int totalReservedOutboundDuration) {
		this.totalReservedOutboundDuration = totalReservedOutboundDuration;
	}
	/**
	 * @return the nbrReservedOutboundPeriods
	 */
	public int getNbrReservedOutboundPeriods() {
		return nbrReservedOutboundPeriods;
	}
	/**
	 * @param nbrReservedOutboundPeriods the nbrReservedOutboundPeriods to set
	 */
	public void setNbrReservedOutboundPeriods(int nbrReservedOutboundPeriods) {
		this.nbrReservedOutboundPeriods = nbrReservedOutboundPeriods;
	}
	/**
	 * @return the maxReservedOutboundDuration
	 */
	public int getMaxReservedOutboundDuration() {
		return maxReservedOutboundDuration;
	}
	/**
	 * @param maxReservedOutboundDuration the maxReservedOutboundDuration to set
	 */
	public void setMaxReservedOutboundDuration(int maxReservedOutboundDuration) {
		this.maxReservedOutboundDuration = maxReservedOutboundDuration;
	}
	/**
	 * @return the totalHoldInboundDurations
	 */
	public int getTotalHoldInboundDurations() {
		return totalHoldInboundDurations;
	}
	/**
	 * @param totalHoldInboundDurations the totalHoldInboundDurations to set
	 */
	public void setTotalHoldInboundDurations(int totalHoldInboundDurations) {
		this.totalHoldInboundDurations = totalHoldInboundDurations;
	}
	/**
	 * @return the nbrHoldInboundPeriods
	 */
	public int getNbrHoldInboundPeriods() {
		return nbrHoldInboundPeriods;
	}
	/**
	 * @param nbrHoldInboundPeriods the nbrHoldInboundPeriods to set
	 */
	public void setNbrHoldInboundPeriods(int nbrHoldInboundPeriods) {
		this.nbrHoldInboundPeriods = nbrHoldInboundPeriods;
	}
	/**
	 * @return the maxHoldInboundDuration
	 */
	public int getMaxHoldInboundDuration() {
		return maxHoldInboundDuration;
	}
	/**
	 * @param maxHoldInboundDuration the maxHoldInboundDuration to set
	 */
	public void setMaxHoldInboundDuration(int maxHoldInboundDuration) {
		this.maxHoldInboundDuration = maxHoldInboundDuration;
	}
	/**
	 * @return the totalHoldOutboundDuration
	 */
	public int getTotalHoldOutboundDuration() {
		return totalHoldOutboundDuration;
	}
	/**
	 * @param totalHoldOutboundDuration the totalHoldOutboundDuration to set
	 */
	public void setTotalHoldOutboundDuration(int totalHoldOutboundDuration) {
		this.totalHoldOutboundDuration = totalHoldOutboundDuration;
	}
	/**
	 * @return the nbrHoldOutboundPeriods
	 */
	public int getNbrHoldOutboundPeriods() {
		return nbrHoldOutboundPeriods;
	}
	/**
	 * @param nbrHoldOutboundPeriods the nbrHoldOutboundPeriods to set
	 */
	public void setNbrHoldOutboundPeriods(int nbrHoldOutboundPeriods) {
		this.nbrHoldOutboundPeriods = nbrHoldOutboundPeriods;
	}
	/**
	 * @return the maxHoldOutboundDuration
	 */
	public int getMaxHoldOutboundDuration() {
		return maxHoldOutboundDuration;
	}
	/**
	 * @param maxHoldOutboundDuration the maxHoldOutboundDuration to set
	 */
	public void setMaxHoldOutboundDuration(int maxHoldOutboundDuration) {
		this.maxHoldOutboundDuration = maxHoldOutboundDuration;
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
	/**
	 * @return the totalWebChattingDuration
	 */
	public int getTotalWebChattingDuration() {
		return totalWebChattingDuration;
	}
	/**
	 * @param totalWebChattingDuration the totalWebChattingDuration to set
	 */
	public void setTotalWebChattingDuration(int totalWebChattingDuration) {
		this.totalWebChattingDuration = totalWebChattingDuration;
	}
	/**
	 * @return the maxWebChattingDuration
	 */
	public int getMaxWebChattingDuration() {
		return maxWebChattingDuration;
	}
	/**
	 * @param maxWebChattingDuration the maxWebChattingDuration to set
	 */
	public void setMaxWebChattingDuration(int maxWebChattingDuration) {
		this.maxWebChattingDuration = maxWebChattingDuration;
	}
	/**
	 * @return the nbrDCPInboundCallsAnswered
	 */
	public int getNbrDCPInboundCallsAnswered() {
		return nbrDCPInboundCallsAnswered;
	}
	/**
	 * @param nbrDCPInboundCallsAnswered the nbrDCPInboundCallsAnswered to set
	 */
	public void setNbrDCPInboundCallsAnswered(int nbrDCPInboundCallsAnswered) {
		this.nbrDCPInboundCallsAnswered = nbrDCPInboundCallsAnswered;
	}
	/**
	 * @return the totalDCPInboundCallsDuration
	 */
	public int getTotalDCPInboundCallsDuration() {
		return totalDCPInboundCallsDuration;
	}
	/**
	 * @param totalDCPInboundCallsDuration the totalDCPInboundCallsDuration to set
	 */
	public void setTotalDCPInboundCallsDuration(int totalDCPInboundCallsDuration) {
		this.totalDCPInboundCallsDuration = totalDCPInboundCallsDuration;
	}
	/**
	 * @return the offSetValue
	 */
	public int getOffSetValue() {
		return offSetValue;
	}
	/**
	 * @param offSetValue the offSetValue to set
	 */
	public void setOffSetValue(int offSetValue) {
		this.offSetValue = offSetValue;
	}
	
	
	
	
}
