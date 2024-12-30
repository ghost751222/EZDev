package com.consilium.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionAnswerRelation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2439530545561309527L;
	private int sId;
	private int pId;
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
	private String isPublic;
	private  String use;
	private String closeData;
	private String closeDataTime;
	private String isEdit;

	private String tel;
	private String connector;
	private String extension;


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
	 * @return the pId
	 */
	public int getpId() {
		return pId;
	}
	/**
	 * @param pId the pId to set
	 */
	public void setpId(int pId) {
		this.pId = pId;
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
	 * @return the staus
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param staus the staus to set
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
	/**
	 * @return the use
	 */
	public String getUse() {
		return use;
	}
	/**
	 * @param use the use to set
	 */
	public void setUse(String use) {
		this.use = use;
	}
	/**
	 * @return the closedData
	 */
	public String getCloseData() {
		return closeData;
	}
	/**
	 * @param closeData the closedData to set
	 */
	public void setCloseData(String closeData) {
		this.closeData = closeData;
	}
	/**
	 * @return the closedDataTime
	 */
	public String getCloseDataTime() {
		return closeDataTime;
	}
	/**
	 * @param closeDataTime the closedDataTime to set
	 */
	public void setCloseDataTime(String closeDataTime) {
		this.closeDataTime = closeDataTime;
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getConnector() {
		return connector;
	}

	public void setConnector(String connector) {
		this.connector = connector;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}
