package com.consilium.domain;

import java.io.Serializable;

public class BBPost implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1935991974258363753L;

    private int forumId;
    private int topicsId;
    private int postId;
    private String posterId;
    private String postTime;
    private String subject;
    private String postText;

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
     * @return the posterId
     */
    public String getPosterId() {
        return posterId;
    }

    /**
     * @param posterId the posterId to set
     */
    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    /**
     * @return the postTime
     */
    public String getPostTime() {
        return postTime;
    }

    /**
     * @param postTime the postTime to set
     */
    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the postText
     */
    public String getPostText() {
        return postText;
    }

    /**
     * @param postText the postText to set
     */
    public void setPostText(String postText) {
        this.postText = postText;
    }
}
