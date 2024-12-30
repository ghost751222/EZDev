package com.consilium.domain;

import java.io.Serializable;

public class TownCode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -268881419097355269L;
	private String cityCode;
	private String codeCode;
	private String codeDisp;
	private String remark;
	/**
	 * @return the cityCode
	 */
	public String getCityCode() {
		return cityCode;
	}
	/**
	 * @param cityCode the cityCode to set
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	/**
	 * @return the codeCode
	 */
	public String getCodeCode() {
		return codeCode;
	}
	/**
	 * @param codeCode the codeCode to set
	 */
	public void setCodeCode(String codeCode) {
		this.codeCode = codeCode;
	}
	/**
	 * @return the codeDisp
	 */
	public String getCodeDisp() {
		return codeDisp;
	}
	/**
	 * @param codeDisp the codeDisp to set
	 */
	public void setCodeDisp(String codeDisp) {
		this.codeDisp = codeDisp;
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
