package com.consilium.domain;

import java.io.Serializable;

public class SendMailboxReceiver implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4009042013055713900L;
	private int sId;
	private int pId;
	private String receiverUnitType;
	private String receiverUnitCode;
	private String receiverId;
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
	 * @return the pId
	 */
	public int getpId() {
		return pId;
	}
	/**
	 * @param pId the pId to set
	 */
	public void setpId(int pId) {
		this.pId = pId;
	}
	/**
	 * @return the receiverUnitType
	 */
	public String getReceiverUnitType() {
		return receiverUnitType;
	}
	/**
	 * @param receiverUnitType the receiverUnitType to set
	 */
	public void setReceiverUnitType(String receiverUnitType) {
		this.receiverUnitType = receiverUnitType;
	}
	/**
	 * @return the receiverUnitCode
	 */
	public String getReceiverUnitCode() {
		return receiverUnitCode;
	}
	/**
	 * @param receiverUnitCode the receiverUnitCode to set
	 */
	public void setReceiverUnitCode(String receiverUnitCode) {
		this.receiverUnitCode = receiverUnitCode;
	}
	/**
	 * @return the receiverId
	 */
	public String getReceiverId() {
		return receiverId;
	}
	/**
	 * @param receiverId the receiverId to set
	 */
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	
}
