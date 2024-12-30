package com.consilium.domain;

import java.io.Serializable;

public class LineBusyMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5879021505316524263L;
	private int roomId;
	private String lastTime;
	private int warnNo;
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
	 * @return the lastTime
	 */
	public String getLastTime() {
		return lastTime;
	}
	/**
	 * @param lastTime the lastTime to set
	 */
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	/**
	 * @return the warnNo
	 */
	public int getWarnNo() {
		return warnNo;
	}
	/**
	 * @param warnNo the warnNo to set
	 */
	public void setWarnNo(int warnNo) {
		this.warnNo = warnNo;
	}
	
}
