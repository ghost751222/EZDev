package com.consilium.domain;

import java.io.Serializable;

public class M7480IVRCallLogs implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4439561850808363797L;
	private int sNo;
	private String ivrTime;
	private String agentId;
	private String agentName;
	private String agentText;
	private String callerId;
	private String caseId;
	private String question1;
	private String question2;
	private String question3;
	private String question4;
	private String status;
	/**
	 * @return the sNo
	 */
	public int getsNo() {
		return sNo;
	}
	/**
	 * @param sNo the sNo to set
	 */
	public void setsNo(int sNo) {
		this.sNo = sNo;
	}
	/**
	 * @return the ivrTime
	 */
	public String getIvrTime() {
		return ivrTime;
	}
	/**
	 * @param ivrTime the ivrTime to set
	 */
	public void setIvrTime(String ivrTime) {
		this.ivrTime = ivrTime;
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
	 * @return the agentText
	 */
	public String getAgentText() {
		return agentText;
	}
	/**
	 * @param agentText the agentText to set
	 */
	public void setAgentText(String agentText) {
		this.agentText = agentText;
	}
	/**
	 * @return the callerId
	 */
	public String getCallerId() {
		return callerId;
	}
	/**
	 * @param callerId the callerId to set
	 */
	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}
	/**
	 * @return the caseId
	 */
	public String getCaseId() {
		return caseId;
	}
	/**
	 * @param caseId the caseId to set
	 */
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	/**
	 * @return the question1
	 */
	public String getQuestion1() {
		return question1;
	}
	/**
	 * @param question1 the question1 to set
	 */
	public void setQuestion1(String question1) {
		this.question1 = question1;
	}
	/**
	 * @return the question2
	 */
	public String getQuestion2() {
		return question2;
	}
	/**
	 * @param question2 the question2 to set
	 */
	public void setQuestion2(String question2) {
		this.question2 = question2;
	}
	/**
	 * @return the question3
	 */
	public String getQuestion3() {
		return question3;
	}
	/**
	 * @param question3 the question3 to set
	 */
	public void setQuestion3(String question3) {
		this.question3 = question3;
	}
	/**
	 * @return the question4
	 */
	public String getQuestion4() {
		return question4;
	}
	/**
	 * @param question4 the question4 to set
	 */
	public void setQuestion4(String question4) {
		this.question4 = question4;
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
	
}
