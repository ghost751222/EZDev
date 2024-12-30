package com.consilium.domain;

import java.io.Serializable;
public class ExceptionData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4257465485065595895L;
	private String time;
	private String text;
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
}
