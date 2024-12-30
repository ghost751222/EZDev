package com.consilium.domain;

import java.io.Serializable;

public class CTICallback1996 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7291248629041630543L;
	private String callId;
	private String callClid;
	private String telephone;
	private String wavFile;
	private int status;
	private String callTime;
	private String createTime;
	private String lastUpdateTime;
	private String lastModifier;
	private String STTInfo;
	/**
	 * @return the callId
	 */
	public String getCallId() {
		return callId;
	}
	/**
	 * @param callId the callId to set
	 */
	public void setCallId(String callId) {
		this.callId = callId;
	}
	/**
	 * @return the callClid
	 */
	public String getCallClid() {
		return callClid;
	}
	/**
	 * @param callClid the callClid to set
	 */
	public void setCallClid(String callClid) {
		this.callClid = callClid;
	}
	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
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
	/**
	 * @return the sTTInfo
	 */
	public String getSTTInfo() {
		return STTInfo;
	}
	/**
	 * @param sTTInfo the sTTInfo to set
	 */
	public void setSTTInfo(String sTTInfo) {
		STTInfo = sTTInfo;
	}
}
