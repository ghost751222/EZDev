package com.consilium.domain;

import java.io.Serializable;

public class MessageReceive implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 795643864101537214L;
	private int seqNo;
	private String mlFrom;
	private String mlSub;
	private String mlCont;
	private String mlAtt;
	private String mlType;
	private int processId;
	private String status;
	private String createTime;
	private int responseNo;
	private String mlUncont;
	/**
	 * @return the seqNo
	 */
	public int getSeqNo() {
		return seqNo;
	}
	/**
	 * @param seqNo the seqNo to set
	 */
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	/**
	 * @return the mlFrom
	 */
	public String getMlFrom() {
		return mlFrom;
	}
	/**
	 * @param mlFrom the mlFrom to set
	 */
	public void setMlFrom(String mlFrom) {
		this.mlFrom = mlFrom;
	}
	/**
	 * @return the mlSub
	 */
	public String getMlSub() {
		return mlSub;
	}
	/**
	 * @param mlSub the mlSub to set
	 */
	public void setMlSub(String mlSub) {
		this.mlSub = mlSub;
	}
	/**
	 * @return the mlCont
	 */
	public String getMlCont() {
		return mlCont;
	}
	/**
	 * @param mlCont the mlCont to set
	 */
	public void setMlCont(String mlCont) {
		this.mlCont = mlCont;
	}
	/**
	 * @return the mlAtt
	 */
	public String getMlAtt() {
		return mlAtt;
	}
	/**
	 * @param mlAtt the mlAtt to set
	 */
	public void setMlAtt(String mlAtt) {
		this.mlAtt = mlAtt;
	}
	/**
	 * @return the mlType
	 */
	public String getMlType() {
		return mlType;
	}
	/**
	 * @param mlType the mlType to set
	 */
	public void setMlType(String mlType) {
		this.mlType = mlType;
	}
	/**
	 * @return the processId
	 */
	public int getProcessId() {
		return processId;
	}
	/**
	 * @param processId the processId to set
	 */
	public void setProcessId(int processId) {
		this.processId = processId;
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
	 * @return the responseNo
	 */
	public int getResponseNo() {
		return responseNo;
	}
	/**
	 * @param responseNo the responseNo to set
	 */
	public void setResponseNo(int responseNo) {
		this.responseNo = responseNo;
	}
	/**
	 * @return the mlUncont
	 */
	public String getMlUncont() {
		return mlUncont;
	}
	/**
	 * @param mlUncont the mlUncont to set
	 */
	public void setMlUncont(String mlUncont) {
		this.mlUncont = mlUncont;
	}
	
	
}
