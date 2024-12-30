package com.consilium.domain;

import java.io.Serializable;

public class AccessPriorityCtiAgent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4236721888052490108L;
	
	private String userId;
	private String priorityType;
	private String priorityLevel;
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
	 * @return the priorityType
	 */
	public String getPriorityType() {
		return priorityType;
	}
	/**
	 * @param priorityType the priorityType to set
	 */
	public void setPriorityType(String priorityType) {
		this.priorityType = priorityType;
	}
	/**
	 * @return the priorityLevel
	 */
	public String getPriorityLevel() {
		return priorityLevel;
	}
	/**
	 * @param priorityLevel the priorityLevel to set
	 */
	public void setPriorityLevel(String priorityLevel) {
		this.priorityLevel = priorityLevel;
	}
	
}
