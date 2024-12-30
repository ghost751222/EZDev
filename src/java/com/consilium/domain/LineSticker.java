package com.consilium.domain;

import java.io.Serializable;

public class LineSticker implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1553717402944039196L;
	private String packageId;
	private String stickerId;
	private String path;
	private String remark;
	/**
	 * @return the packageId
	 */
	public String getPackageId() {
		return packageId;
	}
	/**
	 * @param packageId the packageId to set
	 */
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	/**
	 * @return the stickerId
	 */
	public String getStickerId() {
		return stickerId;
	}
	/**
	 * @param stickerId the stickerId to set
	 */
	public void setStickerId(String stickerId) {
		this.stickerId = stickerId;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
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
	
}
