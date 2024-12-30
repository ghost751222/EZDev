package com.consilium.domain;

import java.io.Serializable;
public class Holiday implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8223646598010230031L;
	private String dateOff;
	private String remark;
	/**
	 * @return the dateOff
	 */
	public String getDateOff() {
		return dateOff;
	}
	/**
	 * @param dateOff the dateOff to set
	 */
	public void setDateOff(String dateOff) {
		this.dateOff = dateOff;
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
