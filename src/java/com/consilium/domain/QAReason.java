package com.consilium.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QAReason implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5640597968392667017L;
    private Integer sId;
    private int qsId;
    private String reason;
    private String createTime;
    private String creator;
    private String lastModifier;
    private String lastUpdateTime;
    private String type;

    /**
     * @return the sId
     */
    public Integer getsId() {
        return sId;
    }

    /**
     * @param sId the sId to set
     */
    public void setsId(Integer sId) {
        this.sId = sId;
    }

    /**
     * @return the qsId
     */
    public int getQsId() {
        return qsId;
    }

    /**
     * @param qsId the qsId to set
     */
    public void setQsId(int qsId) {
        this.qsId = qsId;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
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
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return the lastModifier
     */
    public String getLastModifier() {
        return lastModifier;
    }

    /**
     * @param lastModifier the lastModifier to set
     */
    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    /**
     * @return the lastUpateTime
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpateTime the lastUpateTime to set
     */
    public void setLastUpateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
