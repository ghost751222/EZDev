package com.consilium.domain;

import java.io.Serializable;

public class LineOriFileContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5524984897551107146L;
	
	private int lineSeq;
	private int status;
	private String createTime;
	private int fileSeq;
	private String lastUpdateTime;
	private String resourceId;
	/**
	 * @return the lineSeq
	 */
	public int getLineSeq() {
		return lineSeq;
	}
	/**
	 * @param lineSeq the lineSeq to set
	 */
	public void setLineSeq(int lineSeq) {
		this.lineSeq = lineSeq;
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
	 * @return the fileSeq
	 */
	public int getFileSeq() {
		return fileSeq;
	}
	/**
	 * @param fileSeq the fileSeq to set
	 */
	public void setFileSeq(int fileSeq) {
		this.fileSeq = fileSeq;
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
	 * @return the resourceId
	 */
	public String getResourceId() {
		return resourceId;
	}
	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
}
