package com.consilium.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionAnswer implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4154540758383609765L;
    private int sId;
    private String unitId;
    private String sectionId;
    private String question;
    private String answer;
    private String keyword;
    private String creator;
    private String createTime;
    private String lastModifier;
    private String lastUpdateTime;
    private String status;
    private String efficientTime;
    private Integer score;
    private Integer hit;
    private String isEdit;
    private String applier;
    private String applyTime;
    private String closeData;
    private String closeDataTime;
    private String oldQuestion;
    private String oldAnswer;
    private String disableReason;
    private String disableTime;
    private String orderTime;
    private String isPublic;
    private String type;
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
     * @return the unitId
     */
    public String getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    /**
     * @return the sectionId
     */
    public String getSectionId() {
        return sectionId;
    }

    /**
     * @param sectionId the sectionId to set
     */
    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    /**
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return the answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * @param answer the answer to set
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the efficientTime
     */
    public String getEfficientTime() {
        return efficientTime;
    }

    /**
     * @param efficientTime the efficientTime to set
     */
    public void setEfficientTime(String efficientTime) {
        this.efficientTime = efficientTime;
    }

    /**
     * @return the score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * @return the hit
     */
    public Integer getHit() {
        return hit;
    }

    /**
     * @param hit the hit to set
     */
    public void setHit(Integer hit) {
        this.hit = hit;
    }

    /**
     * @return the isEdit
     */
    public String getIsEdit() {
        return isEdit;
    }

    /**
     * @param isEdit the isEdit to set
     */
    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    /**
     * @return the applier
     */
    public String getApplier() {
        return applier;
    }

    /**
     * @param applier the applier to set
     */
    public void setApplier(String applier) {
        this.applier = applier;
    }

    /**
     * @return the applyTime
     */
    public String getApplyTime() {
        return applyTime;
    }

    /**
     * @param applyTime the applyTime to set
     */
    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * @return the closeData
     */
    public String getCloseData() {
        return closeData;
    }

    /**
     * @param closeData the closeData to set
     */
    public void setCloseData(String closeData) {
        this.closeData = closeData;
    }

    /**
     * @return the closeDataTime
     */
    public String getCloseDataTime() {
        return closeDataTime;
    }

    /**
     * @param closeDataTime the closeDataTime to set
     */
    public void setCloseDataTime(String closeDataTime) {
        this.closeDataTime = closeDataTime;
    }

    /**
     * @return the oldQuestion
     */
    public String getOldQuestion() {
        return oldQuestion;
    }

    /**
     * @param oldQuestion the oldQuestion to set
     */
    public void setOldQuestion(String oldQuestion) {
        this.oldQuestion = oldQuestion;
    }

    /**
     * @return the oldAnswer
     */
    public String getOldAnswer() {
        return oldAnswer;
    }

    /**
     * @param oldAnswer the oldAnswer to set
     */
    public void setOldAnswer(String oldAnswer) {
        this.oldAnswer = oldAnswer;
    }

    /**
     * @return the disableReason
     */
    public String getDisableReason() {
        return disableReason;
    }

    /**
     * @param disableReason the disableReason to set
     */
    public void setDisableReason(String disableReason) {
        this.disableReason = disableReason;
    }

    /**
     * @return the disableTime
     */
    public String getDisableTime() {
        return disableTime;
    }

    /**
     * @param disableTime the disableTime to set
     */
    public void setDisableTime(String disableTime) {
        this.disableTime = disableTime;
    }

    /**
     * @return the orderTime
     */
    public String getOrderTime() {
        return orderTime;
    }

    /**
     * @param orderTime the orderTime to set
     */
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * @return the isPublic
     */
    public String getIsPublic() {
        return isPublic;
    }

    /**
     * @param isPublic the isPublic to set
     */
    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}