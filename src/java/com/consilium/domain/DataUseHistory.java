package com.consilium.domain;

import java.io.Serializable;
public class DataUseHistory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4968884314319028363L;
	private int sId;
	private String dataSId;
	private String source;
	private String caseId;
	private String createor;
	private String createTime;
	/**
	 * @return the sId
	 */
	public int getsId() {
		return sId;
	}
	/**
	 * @param sId the sId to set
	 */
	public void setsId(int sId) {
		this.sId = sId;
	}
	/**
	 * @return the dataSId
	 */
	public String getDataSId() {
		return dataSId;
	}
	/**
	 * @param dataSId the dataSId to set
	 */
	public void setDataSId(String dataSId) {
		this.dataSId = dataSId;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
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
	 * @return the createor
	 */
	public String getCreateor() {
		return createor;
	}
	/**
	 * @param createor the createor to set
	 */
	public void setCreateor(String createor) {
		this.createor = createor;
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
	
}
