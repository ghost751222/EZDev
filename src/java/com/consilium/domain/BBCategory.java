package com.consilium.domain;

import java.io.Serializable;

public class BBCategory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1136261614014269842L;
	
	private int catId;
	private String catTitle;
	private String disabled;
	/**
	 * @return the catId
	 */
	public int getCatId() {
		return catId;
	}
	/**
	 * @param catId the catId to set
	 */
	public void setCatId(int catId) {
		this.catId = catId;
	}
	/**
	 * @return the catTitle
	 */
	public String getCatTitle() {
		return catTitle;
	}
	/**
	 * @param catTitle the catTitle to set
	 */
	public void setCatTitle(String catTitle) {
		this.catTitle = catTitle;
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
