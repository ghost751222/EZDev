package com.consilium.domain;

import java.io.Serializable;

public class M7480Realtime implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3668557835389809341L;
	private int onlineWaiting;
	private int onlineLongstWaiting;
	private int totalyAgent;
	private int agentInReady;
	private int agentNotReady;
	private int agentInTalk;
	private int agentInPCP;
	private int reportTime;
	/**
	 * @return the onlineWaiting
	 */
	public int getOnlineWaiting() {
		return onlineWaiting;
	}
	/**
	 * @param onlineWaiting the onlineWaiting to set
	 */
	public void setOnlineWaiting(int onlineWaiting) {
		this.onlineWaiting = onlineWaiting;
	}
	/**
	 * @return the onlineLongstWaiting
	 */
	public int getOnlineLongstWaiting() {
		return onlineLongstWaiting;
	}
	/**
	 * @param onlineLongstWaiting the onlineLongstWaiting to set
	 */
	public void setOnlineLongstWaiting(int onlineLongstWaiting) {
		this.onlineLongstWaiting = onlineLongstWaiting;
	}
	/**
	 * @return the totalyAgent
	 */
	public int getTotalyAgent() {
		return totalyAgent;
	}
	/**
	 * @param totalyAgent the totalyAgent to set
	 */
	public void setTotalyAgent(int totalyAgent) {
		this.totalyAgent = totalyAgent;
	}
	/**
	 * @return the agentInReady
	 */
	public int getAgentInReady() {
		return agentInReady;
	}
	/**
	 * @param agentInReady the agentInReady to set
	 */
	public void setAgentInReady(int agentInReady) {
		this.agentInReady = agentInReady;
	}
	/**
	 * @return the agentNotReady
	 */
	public int getAgentNotReady() {
		return agentNotReady;
	}
	/**
	 * @param agentNotReady the agentNotReady to set
	 */
	public void setAgentNotReady(int agentNotReady) {
		this.agentNotReady = agentNotReady;
	}
	/**
	 * @return the agentInTalk
	 */
	public int getAgentInTalk() {
		return agentInTalk;
	}
	/**
	 * @param agentInTalk the agentInTalk to set
	 */
	public void setAgentInTalk(int agentInTalk) {
		this.agentInTalk = agentInTalk;
	}
	/**
	 * @return the agentInPCP
	 */
	public int getAgentInPCP() {
		return agentInPCP;
	}
	/**
	 * @param agentInPCP the agentInPCP to set
	 */
	public void setAgentInPCP(int agentInPCP) {
		this.agentInPCP = agentInPCP;
	}
	/**
	 * @return the reportTime
	 */
	public int getReportTime() {
		return reportTime;
	}
	/**
	 * @param reportTime the reportTime to set
	 */
	public void setReportTime(int reportTime) {
		this.reportTime = reportTime;
	}
}
