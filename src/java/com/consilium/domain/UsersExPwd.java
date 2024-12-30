package com.consilium.domain;

import java.io.Serializable;

public class UsersExPwd implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4793695057151911921L;
	private String userId;
	private String password;
	private String changeTime;
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the changeTime
	 */
	public String getChangeTime() {
		return changeTime;
	}
	/**
	 * @param changeTime the changeTime to set
	 */
	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}
	
}
