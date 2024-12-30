package com.consilium.domain;

import java.io.Serializable;

public class QADataViewDiff implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9147690639078184919L;
	private int seqNo;
	private String sId;
	private String oldValue;
	private String newValue;
	private int mode;
	/**
	 * @return the seqNo
	 */
	public int getSeqNo() {
		return seqNo;
	}
	/**
	 * @param seqNo the seqNo to set
	 */
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	/**
	 * @return the sId
	 */
	public String getsId() {
		return sId;
	}
	/**
	 * @param sId the sId to set
	 */
	public void setsId(String sId) {
		this.sId = sId;
	}
	/**
	 * @return the oldValue
	 */
	public String getOldValue() {
		return oldValue;
	}
	/**
	 * @param oldValue the oldValue to set
	 */
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	/**
	 * @return the newValue
	 */
	public String getNewValue() {
		return newValue;
	}
	/**
	 * @param newValue the newValue to set
	 */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}
	
}
