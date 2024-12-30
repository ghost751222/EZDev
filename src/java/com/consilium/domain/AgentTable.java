package com.consilium.domain;

import java.io.Serializable;

public class AgentTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7016707805398157473L;
	private String AgentId;
	private String agentPassword;
	private String userId;
	/**
	 * @return the agentId
	 */
	public String getAgentId() {
		return AgentId;
	}
	/**
	 * @param agentId the agentId to set
	 */
	public void setAgentId(String agentId) {
		AgentId = agentId;
	}
	/**
	 * @return the agentPassword
	 */
	public String getAgentPassword() {
		return agentPassword;
	}
	/**
	 * @param agentPassword the agentPassword to set
	 */
	public void setAgentPassword(String agentPassword) {
		this.agentPassword = agentPassword;
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
}
