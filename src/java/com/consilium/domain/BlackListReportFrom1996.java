package com.consilium.domain;

import java.io.Serializable;

public class BlackListReportFrom1996 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4835794791932772526L;
	private String id;
	private String callClid;
	private int blockRule;
	private String createTime;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the callClid
	 */
	public String getCallClid() {
		return callClid;
	}
	/**
	 * @param callClid the callClid to set
	 */
	public void setCallClid(String callClid) {
		this.callClid = callClid;
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
	
}
