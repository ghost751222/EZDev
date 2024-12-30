package com.consilium.domain;

import java.io.Serializable;
public class LineEmoji implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8010272871042155709L;
	private String emojiId;
	private String remark;
	private String text;
	/**
	 * @return the emojiId
	 */
	public String getEmojiId() {
		return emojiId;
	}
	/**
	 * @param emojiId the emojiId to set
	 */
	public void setEmojiId(String emojiId) {
		this.emojiId = emojiId;
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
