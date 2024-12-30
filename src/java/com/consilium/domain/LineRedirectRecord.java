package com.consilium.domain;

import java.io.Serializable;

public class LineRedirectRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -793614037616228272L;
	private String lineId;
	private int roomId;
	private int status;
	private String receiveIP;
	private String successTime;
	private String createTime;
	private String lastUpdateTime;
	private String endTime;
	private String actionId;
	private String owner;
	private int roomSeq;
	/**
	 * @return the lineId
	 */
	public String getLineId() {
		return lineId;
	}
	/**
	 * @param lineId the lineId to set
	 */
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	/**
	 * @return the roomId
	 */
	public int getRoomId() {
		return roomId;
	}
	/**
	 * @param roomId the roomId to set
	 */
	public void setRoomId(int roomId) {
		this.roomId = roomId;
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
	 * @return the receiveIP
	 */
	public String getReceiveIP() {
		return receiveIP;
	}
	/**
	 * @param receiveIP the receiveIP to set
	 */
	public void setReceiveIP(String receiveIP) {
		this.receiveIP = receiveIP;
	}
	/**
	 * @return the successTime
	 */
	public String getSuccessTime() {
		return successTime;
	}
	/**
	 * @param successTime the successTime to set
	 */
	public void setSuccessTime(String successTime) {
		this.successTime = successTime;
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
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the actionId
	 */
	public String getActionId() {
		return actionId;
	}
	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @return the roomSeq
	 */
	public int getRoomSeq() {
		return roomSeq;
	}
	/**
	 * @param roomSeq the roomSeq to set
	 */
	public void setRoomSeq(int roomSeq) {
		this.roomSeq = roomSeq;
	}
	
}
