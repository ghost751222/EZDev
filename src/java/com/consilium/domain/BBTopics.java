package com.consilium.domain;

import java.io.Serializable;

public class BBTopics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9138946595045290956L;
	
	private int forumId;
	private int topicsId;
	private int postId;
	private String topicsTitle;
	private String topicsPoster;
	private String topicsTime;
	private int topicsViews;
	private int topicsReplies;
	private String topicsLastPostId;
	private String topicsLastPostTime;
	private String disabled;
	private String isTop;
	private String marrow;
	private String misTop;
	/**
	 * @return the forumId
	 */
	public int getForumId() {
		return forumId;
	}
	/**
	 * @param forumId the forumId to set
	 */
	public void setForumId(int forumId) {
		this.forumId = forumId;
	}
	/**
	 * @return the topicsId
	 */
	public int getTopicsId() {
		return topicsId;
	}
	/**
	 * @param topicsId the topicsId to set
	 */
	public void setTopicsId(int topicsId) {
		this.topicsId = topicsId;
	}
	/**
	 * @return the postId
	 */
	public int getPostId() {
		return postId;
	}
	/**
	 * @param postId the postId to set
	 */
	public void setPostId(int postId) {
		this.postId = postId;
	}
	/**
	 * @return the topicsTitle
	 */
	public String getTopicsTitle() {
		return topicsTitle;
	}
	/**
	 * @param topicsTitle the topicsTitle to set
	 */
	public void setTopicsTitle(String topicsTitle) {
		this.topicsTitle = topicsTitle;
	}
	/**
	 * @return the topicsPoster
	 */
	public String getTopicsPoster() {
		return topicsPoster;
	}
	/**
	 * @param topicsPoster the topicsPoster to set
	 */
	public void setTopicsPoster(String topicsPoster) {
		this.topicsPoster = topicsPoster;
	}
	/**
	 * @return the topicsTime
	 */
	public String getTopicsTime() {
		return topicsTime;
	}
	/**
	 * @param topicsTime the topicsTime to set
	 */
	public void setTopicsTime(String topicsTime) {
		this.topicsTime = topicsTime;
	}
	/**
	 * @return the topicsViews
	 */
	public int getTopicsViews() {
		return topicsViews;
	}
	/**
	 * @param topicsViews the topicsViews to set
	 */
	public void setTopicsViews(int topicsViews) {
		this.topicsViews = topicsViews;
	}
	/**
	 * @return the topicsReplies
	 */
	public int getTopicsReplies() {
		return topicsReplies;
	}
	/**
	 * @param topicsReplies the topicsReplies to set
	 */
	public void setTopicsReplies(int topicsReplies) {
		this.topicsReplies = topicsReplies;
	}
	/**
	 * @return the topicsLastPostId
	 */
	public String getTopicsLastPostId() {
		return topicsLastPostId;
	}
	/**
	 * @param topicsLastPostId the topicsLastPostId to set
	 */
	public void setTopicsLastPostId(String topicsLastPostId) {
		this.topicsLastPostId = topicsLastPostId;
	}
	/**
	 * @return the topicsLastPostTime
	 */
	public String getTopicsLastPostTime() {
		return topicsLastPostTime;
	}
	/**
	 * @param topicsLastPostTime the topicsLastPostTime to set
	 */
	public void setTopicsLastPostTime(String topicsLastPostTime) {
		this.topicsLastPostTime = topicsLastPostTime;
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
	/**
	 * @return the isTop
	 */
	public String getIsTop() {
		return isTop;
	}
	/**
	 * @param isTop the isTop to set
	 */
	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}
	/**
	 * @return the marrow
	 */
	public String getMarrow() {
		return marrow;
	}
	/**
	 * @param marrow the marrow to set
	 */
	public void setMarrow(String marrow) {
		this.marrow = marrow;
	}
	/**
	 * @return the misTop
	 */
	public String getMisTop() {
		return misTop;
	}
	/**
	 * @param misTop the misTop to set
	 */
	public void setMisTop(String misTop) {
		this.misTop = misTop;
	}
	
}
