package com.consilium.domain;

import java.io.Serializable;

public class LineBlackList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1967780167045561046L;
	private int id;
	private String lineId;
	private String isActive;
	private int blockRule;
	private String blockMessage;
	private String expiration;
	private String forbitReason;
	private String remark;
	private String creator;
	private String createTime;
	private String lastModifier;
	private String lastUpdateTime;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
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
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return the blockRule
	 */
	public int getBlockRule() {
		return blockRule;
	}
	/**
	 * @param blockRule the blockRule to set
	 */
	public void setBlockRule(int blockRule) {
		this.blockRule = blockRule;
	}
	/**
	 * @return the blockMessage
	 */
	public String getBlockMessage() {
		return blockMessage;
	}
	/**
	 * @param blockMessage the blockMessage to set
	 */
	public void setBlockMessage(String blockMessage) {
		this.blockMessage = blockMessage;
	}
	/**
	 * @return the expiration
	 */
	public String getExpiration() {
		return expiration;
	}
	/**
	 * @param expiration the expiration to set
	 */
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	/**
	 * @return the forbitReason
	 */
	public String getForbitReason() {
		return forbitReason;
	}
	/**
	 * @param forbitReason the forbitReason to set
	 */
	public void setForbitReason(String forbitReason) {
		this.forbitReason = forbitReason;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
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
	 * @return the lastModifier
	 */
	public String getLastModifier() {
		return lastModifier;
	}
	/**
	 * @param lastModifier the lastModifier to set
	 */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
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
	
}
