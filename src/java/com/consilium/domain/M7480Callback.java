package com.consilium.domain;

import java.io.Serializable;

public class M7480Callback implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2980956897943009149L;
	private String m7480CallId;
	private String callClId;
	private String callbackClId;
	private String wavFile;
	private int status;
	private String callTime;
	private String createTime;
	private String lastUpdateTime;
	private String lastModifier;
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
	 * @return the callbackClId
	 */
	public String getCallbackClId() {
		return callbackClId;
	}
	/**
	 * @param callbackClId the callbackClId to set
	 */
	public void setCallbackClId(String callbackClId) {
		this.callbackClId = callbackClId;
	}
	/**
	 * @return the wavFile
	 */
	public String getWavFile() {
		return wavFile;
	}
	/**
	 * @param wavFile the wavFile to set
	 */
	public void setWavFile(String wavFile) {
		this.wavFile = wavFile;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
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
	
}
