package com.consilium.domain;

import java.io.Serializable;

public class M7480SiteInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7365203528555491753L;
	private String serviceId;
	private String beginTime;
	private int beginTimeHour;
	private int nbrCalls;
	private int nbrRejectedCalls;
	private int nbrCallsAnswered;
	private int nbtCallsAbandoned;
	private int nbtCallsNotToTransfer;
	private int sumCallsDuration;
	private int maxCallDuration;
	private int sumCallsAnsweredWaitDuration;
	private int maxCallAnsweredWaitDuration;
	private int sumCallsAbandoneWaitDuration;
	private int maxCallAbandoneWaitDuration;
	private int sumCallsDurationBeforeAnswer;
	private int maxCallDurationBeforeAnswer;
	private int sumCallsDurationBeforeAbandon;
	private int maxCallDurationBeforeAbandon;
	private int sumCallsNotToTransferDuration;
	private int maxCallNotToTransferDuration;
	private int sumCallsAgentCommDuration;
	private int maxCallAgentCommDuration;
	private int sumPCPDuration;
	private int maxPCPDuration;
	private int updated;
	private String createTime;
	private String lastUpdateTime;
	private int sumRingDurAnswerCall;
	private int totalLoginReadyIdleDuration;
	private int totalLoginNotReadyIdleDuration;
	private int totalWebChatingDuration;
	private int maxWaitCallNBR;
	private int waitCallNBR;
	private int callCount;
	private int nbrCallsAbandonedWait;
	private int nbrInboundCallNotAnswer;
	private int reportCount;
	private int externalCallCount;
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
	 * @return the nbrCalls
	 */
	public int getNbrCalls() {
		return nbrCalls;
	}
	/**
	 * @param nbrCalls the nbrCalls to set
	 */
	public void setNbrCalls(int nbrCalls) {
		this.nbrCalls = nbrCalls;
	}
	/**
	 * @return the nbrRejectedCalls
	 */
	public int getNbrRejectedCalls() {
		return nbrRejectedCalls;
	}
	/**
	 * @param nbrRejectedCalls the nbrRejectedCalls to set
	 */
	public void setNbrRejectedCalls(int nbrRejectedCalls) {
		this.nbrRejectedCalls = nbrRejectedCalls;
	}
	/**
	 * @return the nbrCallsAnswered
	 */
	public int getNbrCallsAnswered() {
		return nbrCallsAnswered;
	}
	/**
	 * @param nbrCallsAnswered the nbrCallsAnswered to set
	 */
	public void setNbrCallsAnswered(int nbrCallsAnswered) {
		this.nbrCallsAnswered = nbrCallsAnswered;
	}
	/**
	 * @return the nbtCallsAbandoned
	 */
	public int getNbtCallsAbandoned() {
		return nbtCallsAbandoned;
	}
	/**
	 * @param nbtCallsAbandoned the nbtCallsAbandoned to set
	 */
	public void setNbtCallsAbandoned(int nbtCallsAbandoned) {
		this.nbtCallsAbandoned = nbtCallsAbandoned;
	}
	/**
	 * @return the nbtCallsNotToTransfer
	 */
	public int getNbtCallsNotToTransfer() {
		return nbtCallsNotToTransfer;
	}
	/**
	 * @param nbtCallsNotToTransfer the nbtCallsNotToTransfer to set
	 */
	public void setNbtCallsNotToTransfer(int nbtCallsNotToTransfer) {
		this.nbtCallsNotToTransfer = nbtCallsNotToTransfer;
	}
	/**
	 * @return the sumCallsDuration
	 */
	public int getSumCallsDuration() {
		return sumCallsDuration;
	}
	/**
	 * @param sumCallsDuration the sumCallsDuration to set
	 */
	public void setSumCallsDuration(int sumCallsDuration) {
		this.sumCallsDuration = sumCallsDuration;
	}
	/**
	 * @return the maxCallDuration
	 */
	public int getMaxCallDuration() {
		return maxCallDuration;
	}
	/**
	 * @param maxCallDuration the maxCallDuration to set
	 */
	public void setMaxCallDuration(int maxCallDuration) {
		this.maxCallDuration = maxCallDuration;
	}
	/**
	 * @return the sumCallsAnsweredWaitDuration
	 */
	public int getSumCallsAnsweredWaitDuration() {
		return sumCallsAnsweredWaitDuration;
	}
	/**
	 * @param sumCallsAnsweredWaitDuration the sumCallsAnsweredWaitDuration to set
	 */
	public void setSumCallsAnsweredWaitDuration(int sumCallsAnsweredWaitDuration) {
		this.sumCallsAnsweredWaitDuration = sumCallsAnsweredWaitDuration;
	}
	/**
	 * @return the maxCallAnsweredWaitDuration
	 */
	public int getMaxCallAnsweredWaitDuration() {
		return maxCallAnsweredWaitDuration;
	}
	/**
	 * @param maxCallAnsweredWaitDuration the maxCallAnsweredWaitDuration to set
	 */
	public void setMaxCallAnsweredWaitDuration(int maxCallAnsweredWaitDuration) {
		this.maxCallAnsweredWaitDuration = maxCallAnsweredWaitDuration;
	}
	/**
	 * @return the sumCallsAbandoneWaitDuration
	 */
	public int getSumCallsAbandoneWaitDuration() {
		return sumCallsAbandoneWaitDuration;
	}
	/**
	 * @param sumCallsAbandoneWaitDuration the sumCallsAbandoneWaitDuration to set
	 */
	public void setSumCallsAbandoneWaitDuration(int sumCallsAbandoneWaitDuration) {
		this.sumCallsAbandoneWaitDuration = sumCallsAbandoneWaitDuration;
	}
	/**
	 * @return the maxCallAbandoneWaitDuration
	 */
	public int getMaxCallAbandoneWaitDuration() {
		return maxCallAbandoneWaitDuration;
	}
	/**
	 * @param maxCallAbandoneWaitDuration the maxCallAbandoneWaitDuration to set
	 */
	public void setMaxCallAbandoneWaitDuration(int maxCallAbandoneWaitDuration) {
		this.maxCallAbandoneWaitDuration = maxCallAbandoneWaitDuration;
	}
	/**
	 * @return the sumCallsDurationBeforeAnswer
	 */
	public int getSumCallsDurationBeforeAnswer() {
		return sumCallsDurationBeforeAnswer;
	}
	/**
	 * @param sumCallsDurationBeforeAnswer the sumCallsDurationBeforeAnswer to set
	 */
	public void setSumCallsDurationBeforeAnswer(int sumCallsDurationBeforeAnswer) {
		this.sumCallsDurationBeforeAnswer = sumCallsDurationBeforeAnswer;
	}
	/**
	 * @return the maxCallDurationBeforeAnswer
	 */
	public int getMaxCallDurationBeforeAnswer() {
		return maxCallDurationBeforeAnswer;
	}
	/**
	 * @param maxCallDurationBeforeAnswer the maxCallDurationBeforeAnswer to set
	 */
	public void setMaxCallDurationBeforeAnswer(int maxCallDurationBeforeAnswer) {
		this.maxCallDurationBeforeAnswer = maxCallDurationBeforeAnswer;
	}
	/**
	 * @return the sumCallsDurationBeforeAbandon
	 */
	public int getSumCallsDurationBeforeAbandon() {
		return sumCallsDurationBeforeAbandon;
	}
	/**
	 * @param sumCallsDurationBeforeAbandon the sumCallsDurationBeforeAbandon to set
	 */
	public void setSumCallsDurationBeforeAbandon(int sumCallsDurationBeforeAbandon) {
		this.sumCallsDurationBeforeAbandon = sumCallsDurationBeforeAbandon;
	}
	/**
	 * @return the maxCallDurationBeforeAbandon
	 */
	public int getMaxCallDurationBeforeAbandon() {
		return maxCallDurationBeforeAbandon;
	}
	/**
	 * @param maxCallDurationBeforeAbandon the maxCallDurationBeforeAbandon to set
	 */
	public void setMaxCallDurationBeforeAbandon(int maxCallDurationBeforeAbandon) {
		this.maxCallDurationBeforeAbandon = maxCallDurationBeforeAbandon;
	}
	/**
	 * @return the sumCallsNotToTransferDuration
	 */
	public int getSumCallsNotToTransferDuration() {
		return sumCallsNotToTransferDuration;
	}
	/**
	 * @param sumCallsNotToTransferDuration the sumCallsNotToTransferDuration to set
	 */
	public void setSumCallsNotToTransferDuration(int sumCallsNotToTransferDuration) {
		this.sumCallsNotToTransferDuration = sumCallsNotToTransferDuration;
	}
	/**
	 * @return the maxCallNotToTransferDuration
	 */
	public int getMaxCallNotToTransferDuration() {
		return maxCallNotToTransferDuration;
	}
	/**
	 * @param maxCallNotToTransferDuration the maxCallNotToTransferDuration to set
	 */
	public void setMaxCallNotToTransferDuration(int maxCallNotToTransferDuration) {
		this.maxCallNotToTransferDuration = maxCallNotToTransferDuration;
	}
	/**
	 * @return the sumCallsAgentCommDuration
	 */
	public int getSumCallsAgentCommDuration() {
		return sumCallsAgentCommDuration;
	}
	/**
	 * @param sumCallsAgentCommDuration the sumCallsAgentCommDuration to set
	 */
	public void setSumCallsAgentCommDuration(int sumCallsAgentCommDuration) {
		this.sumCallsAgentCommDuration = sumCallsAgentCommDuration;
	}
	/**
	 * @return the maxCallAgentCommDuration
	 */
	public int getMaxCallAgentCommDuration() {
		return maxCallAgentCommDuration;
	}
	/**
	 * @param maxCallAgentCommDuration the maxCallAgentCommDuration to set
	 */
	public void setMaxCallAgentCommDuration(int maxCallAgentCommDuration) {
		this.maxCallAgentCommDuration = maxCallAgentCommDuration;
	}
	/**
	 * @return the sumPCPDuration
	 */
	public int getSumPCPDuration() {
		return sumPCPDuration;
	}
	/**
	 * @param sumPCPDuration the sumPCPDuration to set
	 */
	public void setSumPCPDuration(int sumPCPDuration) {
		this.sumPCPDuration = sumPCPDuration;
	}
	/**
	 * @return the maxPCPDuration
	 */
	public int getMaxPCPDuration() {
		return maxPCPDuration;
	}
	/**
	 * @param maxPCPDuration the maxPCPDuration to set
	 */
	public void setMaxPCPDuration(int maxPCPDuration) {
		this.maxPCPDuration = maxPCPDuration;
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
	/**
	 * @return the sumRingDurAnswerCall
	 */
	public int getSumRingDurAnswerCall() {
		return sumRingDurAnswerCall;
	}
	/**
	 * @param sumRingDurAnswerCall the sumRingDurAnswerCall to set
	 */
	public void setSumRingDurAnswerCall(int sumRingDurAnswerCall) {
		this.sumRingDurAnswerCall = sumRingDurAnswerCall;
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
	 * @return the totalLoginNotReadyIdleDuration
	 */
	public int getTotalLoginNotReadyIdleDuration() {
		return totalLoginNotReadyIdleDuration;
	}
	/**
	 * @param totalLoginNotReadyIdleDuration the totalLoginNotReadyIdleDuration to set
	 */
	public void setTotalLoginNotReadyIdleDuration(int totalLoginNotReadyIdleDuration) {
		this.totalLoginNotReadyIdleDuration = totalLoginNotReadyIdleDuration;
	}
	/**
	 * @return the totalWebChatingDuration
	 */
	public int getTotalWebChatingDuration() {
		return totalWebChatingDuration;
	}
	/**
	 * @param totalWebChatingDuration the totalWebChatingDuration to set
	 */
	public void setTotalWebChatingDuration(int totalWebChatingDuration) {
		this.totalWebChatingDuration = totalWebChatingDuration;
	}
	/**
	 * @return the maxWaitCallNBR
	 */
	public int getMaxWaitCallNBR() {
		return maxWaitCallNBR;
	}
	/**
	 * @param maxWaitCallNBR the maxWaitCallNBR to set
	 */
	public void setMaxWaitCallNBR(int maxWaitCallNBR) {
		this.maxWaitCallNBR = maxWaitCallNBR;
	}
	/**
	 * @return the waitCallNBR
	 */
	public int getWaitCallNBR() {
		return waitCallNBR;
	}
	/**
	 * @param waitCallNBR the waitCallNBR to set
	 */
	public void setWaitCallNBR(int waitCallNBR) {
		this.waitCallNBR = waitCallNBR;
	}
	/**
	 * @return the callCount
	 */
	public int getCallCount() {
		return callCount;
	}
	/**
	 * @param callCount the callCount to set
	 */
	public void setCallCount(int callCount) {
		this.callCount = callCount;
	}
	/**
	 * @return the nbrCallsAbandonedWait
	 */
	public int getNbrCallsAbandonedWait() {
		return nbrCallsAbandonedWait;
	}
	/**
	 * @param nbrCallsAbandonedWait the nbrCallsAbandonedWait to set
	 */
	public void setNbrCallsAbandonedWait(int nbrCallsAbandonedWait) {
		this.nbrCallsAbandonedWait = nbrCallsAbandonedWait;
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
	 * @return the reportCount
	 */
	public int getReportCount() {
		return reportCount;
	}
	/**
	 * @param reportCount the reportCount to set
	 */
	public void setReportCount(int reportCount) {
		this.reportCount = reportCount;
	}
	/**
	 * @return the externalCallCount
	 */
	public int getExternalCallCount() {
		return externalCallCount;
	}
	/**
	 * @param externalCallCount the externalCallCount to set
	 */
	public void setExternalCallCount(int externalCallCount) {
		this.externalCallCount = externalCallCount;
	}
	
	
	
}
