package com.consilium.domain;

import java.io.Serializable;

public class CaseReceiveLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2808647839550620365L;
	private String formType;
	private int formVer;
	private String actionId;
	private int sendType;
	private int hasForm;
	private String sendTime;
	private String senderId;
	private String sendUnitType;
	private String sendUnitCode;
	private String sendCityCode;
	private String receiveTime;
	private String receiverId;
	private String receiveUnitType;
	private String receiveUnitCode;
	private String receiveCityCode;
	
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
	public int getFormVer() {
		return formVer;
	}
	public void setFormVer(int formVer) {
		this.formVer = formVer;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public int getSendType() {
		return sendType;
	}
	public void setSendType(int sendType) {
		this.sendType = sendType;
	}
	public int getHasForm() {
		return hasForm;
	}
	public void setHasForm(int hasForm) {
		this.hasForm = hasForm;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSendUnitType() {
		return sendUnitType;
	}
	public void setSendUnitType(String sendUnitType) {
		this.sendUnitType = sendUnitType;
	}
	public String getSendUnitCode() {
		return sendUnitCode;
	}
	public void setSendUnitCode(String sendUnitCode) {
		this.sendUnitCode = sendUnitCode;
	}
	public String getSendCityCode() {
		return sendCityCode;
	}
	public void setSendCityCode(String sendCityCode) {
		this.sendCityCode = sendCityCode;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getReceiveUnitType() {
		return receiveUnitType;
	}
	public void setReceiveUnitType(String receiveUnitType) {
		this.receiveUnitType = receiveUnitType;
	}
	public String getReceiveUnitCode() {
		return receiveUnitCode;
	}
	public void setReceiveUnitCode(String receiveUnitCode) {
		this.receiveUnitCode = receiveUnitCode;
	}
	public String getReceiveCityCode() {
		return receiveCityCode;
	}
	public void setReceiveCityCode(String receiveCityCode) {
		this.receiveCityCode = receiveCityCode;
	}
}
