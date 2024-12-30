package com.consilium.domain;

import java.io.Serializable;

public class M7480CallsToExternal implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1826831500462384554L;
	private String m7480CallId;
	private String callCld;
	private int servicedTmf;
	private int transferResult;
	private String callTime;
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
	 * @return the callCld
	 */
	public String getCallCld() {
		return callCld;
	}
	/**
	 * @param callCld the callCld to set
	 */
	public void setCallCld(String callCld) {
		this.callCld = callCld;
	}
	/**
	 * @return the servicedTmf
	 */
	public int getServicedTmf() {
		return servicedTmf;
	}
	/**
	 * @param servicedTmf the servicedTmf to set
	 */
	public void setServicedTmf(int servicedTmf) {
		this.servicedTmf = servicedTmf;
	}
	/**
	 * @return the transferResult
	 */
	public int getTransferResult() {
		return transferResult;
	}
	/**
	 * @param transferResult the transferResult to set
	 */
	public void setTransferResult(int transferResult) {
		this.transferResult = transferResult;
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
