package com.consilium.domain;

import java.io.Serializable;
public class Board  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5045460576278443632L;
	private String unitType;
	private String unitCode;
	private String userId;
	private String postText;
	private String postTime;
	private String textType;
	/**
	 * @return the unitType
	 */
	public String getUnitType() {
		return unitType;
	}
	/**
	 * @param unitType the unitType to set
	 */
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	/**
	 * @return the unitCode
	 */
	public String getUnitCode() {
		return unitCode;
	}
	/**
	 * @param unitCode the unitCode to set
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
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
	 * @return the postText
	 */
	public String getPostText() {
		return postText;
	}
	/**
	 * @param postText the postText to set
	 */
	public void setPostText(String postText) {
		this.postText = postText;
	}
	/**
	 * @return the postTime
	 */
	public String getPostTime() {
		return postTime;
	}
	/**
	 * @param postTime the postTime to set
	 */
	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}
	/**
	 * @return the textType
	 */
	public String getTextType() {
		return textType;
	}
	/**
	 * @param textType the textType to set
	 */
	public void setTextType(String textType) {
		this.textType = textType;
	}
	
}
