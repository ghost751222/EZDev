package com.consilium.domain;

import java.io.Serializable;

public class BBForum implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3931380424516323296L;
	
	private int catId;
	private int forumId;
	private String forumTile;
	private String forumDesc;
	private int forumTopics;
	private int forumPosts;
	private String forumLastPostId;
	private String forumLastPostTime;
	private String disabled;
	private String authority;
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
	 * @return the forumTile
	 */
	public String getForumTile() {
		return forumTile;
	}
	/**
	 * @param forumTile the forumTile to set
	 */
	public void setForumTile(String forumTile) {
		this.forumTile = forumTile;
	}
	/**
	 * @return the forumDesc
	 */
	public String getForumDesc() {
		return forumDesc;
	}
	/**
	 * @param forumDesc the forumDesc to set
	 */
	public void setForumDesc(String forumDesc) {
		this.forumDesc = forumDesc;
	}
	/**
	 * @return the forumTopics
	 */
	public int getForumTopics() {
		return forumTopics;
	}
	/**
	 * @param forumTopics the forumTopics to set
	 */
	public void setForumTopics(int forumTopics) {
		this.forumTopics = forumTopics;
	}
	/**
	 * @return the forumPosts
	 */
	public int getForumPosts() {
		return forumPosts;
	}
	/**
	 * @param forumPosts the forumPosts to set
	 */
	public void setForumPosts(int forumPosts) {
		this.forumPosts = forumPosts;
	}
	/**
	 * @return the forumLastPostId
	 */
	public String getForumLastPostId() {
		return forumLastPostId;
	}
	/**
	 * @param forumLastPostId the forumLastPostId to set
	 */
	public void setForumLastPostId(String forumLastPostId) {
		this.forumLastPostId = forumLastPostId;
	}
	/**
	 * @return the forumLastPostTime
	 */
	public String getForumLastPostTime() {
		return forumLastPostTime;
	}
	/**
	 * @param forumLastPostTime the forumLastPostTime to set
	 */
	public void setForumLastPostTime(String forumLastPostTime) {
		this.forumLastPostTime = forumLastPostTime;
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
	 * @return the authority
	 */
	public String getAuthority() {
		return authority;
	}
	/**
	 * @param authority the authority to set
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	

}
