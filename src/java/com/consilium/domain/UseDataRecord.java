package com.consilium.domain;

import java.io.Serializable;

public class UseDataRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6830835021741167041L;
	private int sId;
	private String dataSid;
	private String source;
	private int hit;
	/**
	 * @return the sId
	 */
	public int getsId() {
		return sId;
	}
	/**
	 * @param sId the sId to set
	 */
	public void setsId(int sId) {
		this.sId = sId;
	}
	/**
	 * @return the dataSid
	 */
	public String getDataSid() {
		return dataSid;
	}
	/**
	 * @param dataSid the dataSid to set
	 */
	public void setDataSid(String dataSid) {
		this.dataSid = dataSid;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the hit
	 */
	public int getHit() {
		return hit;
	}
	/**
	 * @param hit the hit to set
	 */
	public void setHit(int hit) {
		this.hit = hit;
	}
	
}
