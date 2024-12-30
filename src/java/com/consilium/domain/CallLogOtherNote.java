package com.consilium.domain;

import java.io.Serializable;

public class CallLogOtherNote implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5668993731500946899L;
	private String actionId;
	private String itemId;
	/**
	 * @return the actionId
	 */
	public String getActionId() {
		return actionId;
	}
	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
}
