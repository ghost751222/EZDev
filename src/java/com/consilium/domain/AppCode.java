package com.consilium.domain;

import java.io.Serializable;

public class AppCode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2361055822582598645L;
	private String category;
	private String codeCode;
	private String codeDisp;
	private String remark;
	private String disabled;
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
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
	/**
	 * @return the disabled
	 */
	public String getDisabled() {
		return disabled;
	}
	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	

}
