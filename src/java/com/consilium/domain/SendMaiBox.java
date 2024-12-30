package com.consilium.domain;

import java.io.Serializable;

public class SendMaiBox implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7108823391517430482L;
	private int seqId;
	private String type;
	private String senderUnitType;
	private String senderUnitCode;
	private String senderId;
	private String title;
	private String body;
	private String sendTime;
	private String hasRead;
	private String register;
	private String canReply;
	/**
	 * @return the seqId
	 */
	public int getSeqId() {
		return seqId;
	}
	/**
	 * @param seqId the seqId to set
	 */
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the senderUnitType
	 */
	public String getSenderUnitType() {
		return senderUnitType;
	}
	/**
	 * @param senderUnitType the senderUnitType to set
	 */
	public void setSenderUnitType(String senderUnitType) {
		this.senderUnitType = senderUnitType;
	}
	/**
	 * @return the senderUnitCode
	 */
	public String getSenderUnitCode() {
		return senderUnitCode;
	}
	/**
	 * @param senderUnitCode the senderUnitCode to set
	 */
	public void setSenderUnitCode(String senderUnitCode) {
		this.senderUnitCode = senderUnitCode;
	}
	/**
	 * @return the senderId
	 */
	public String getSenderId() {
		return senderId;
	}
	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the sendTime
	 */
	public String getSendTime() {
		return sendTime;
	}
	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return the hasRead
	 */
	public String getHasRead() {
		return hasRead;
	}
	/**
	 * @param hasRead the hasRead to set
	 */
	public void setHasRead(String hasRead) {
		this.hasRead = hasRead;
	}
	/**
	 * @return the register
	 */
	public String getRegister() {
		return register;
	}
	/**
	 * @param register the register to set
	 */
	public void setRegister(String register) {
		this.register = register;
	}
	/**
	 * @return the canReply
	 */
	public String getCanReply() {
		return canReply;
	}
	/**
	 * @param canReply the canReply to set
	 */
	public void setCanReply(String canReply) {
		this.canReply = canReply;
	}
	
}
