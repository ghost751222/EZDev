package com.consilium.domain;

import java.io.Serializable;

public class ExcelUnits implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5474728557355513139L;
	private String unitCode;
	private String unitLevel;
	private int orderNum;
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
	 * @return the unitLevel
	 */
	public String getUnitLevel() {
		return unitLevel;
	}
	/**
	 * @param unitLevel the unitLevel to set
	 */
	public void setUnitLevel(String unitLevel) {
		this.unitLevel = unitLevel;
	}
	/**
	 * @return the orderNum
	 */
	public int getOrderNum() {
		return orderNum;
	}
	/**
	 * @param orderNum the orderNum to set
	 */
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	
}
