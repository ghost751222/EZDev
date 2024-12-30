package com.consilium.domain;

import java.io.Serializable;

public class LineMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9162996891587837412L;
	private int lineSeq;
	private String lineMessageId;
	private String lineId;
	private int roomId;
	private int direction;
	private String contentType;
	private int status;
	private String actionId;
	private String owner;
	private String createTime;
	private String lastUpdateTime;
	private String display;
	private int attachment;
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
	 * @return the lineId
	 */
	public String getLineId() {
		return lineId;
	}
	/**
	 * @param lineId the lineId to set
	 */
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	/**
	 * @return the roomId
	 */
	public int getRoomId() {
		return roomId;
	}
	/**
	 * @param roomId the roomId to set
	 */
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
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
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
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
	 * @return the lastUpdateTime
	 */
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	/**
	 * @param lastUpdateTime the lastUpdateTime to set
	 */
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	/**
	 * @return the display
	 */
	public String getDisplay() {
		return display;
	}
	/**
	 * @param display the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
	}
	/**
	 * @return the attachment
	 */
	public int getAttachment() {
		return attachment;
	}
	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(int attachment) {
		this.attachment = attachment;
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
