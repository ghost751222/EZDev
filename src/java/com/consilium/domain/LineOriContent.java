package com.consilium.domain;

import java.io.Serializable;
public class LineOriContent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2602970849146191740L;
	private int lineSeq;
	private String lineMessageId;
	private String createTime;
	private String content;
	/**
	 * @return the lineSeq
	 */
	public int getLineSeq() {
		return lineSeq;
	}
	/**
	 * @param lineSeq the lineSeq to set
	 */
	public void setLineSeq(int lineSeq) {
		this.lineSeq = lineSeq;
	}
	/**
	 * @return the lineMessageId
	 */
	public String getLineMessageId() {
		return lineMessageId;
	}
	/**
	 * @param lineMessageId the lineMessageId to set
	 */
	public void setLineMessageId(String lineMessageId) {
		this.lineMessageId = lineMessageId;
	}
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
}
