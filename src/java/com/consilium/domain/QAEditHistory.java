package com.consilium.domain;

import java.io.Serializable;
public class QAEditHistory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4317258870188915620L;
	private int sId;
	private int qsId;
	private String question;
	private String answer;
	private String keyword;
	private String lastUpdateTime;
	private String lastModifier;
	private String editDate;
	private String source;
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
	 * @return the answser
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * @param answer the answser to set
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
	 * @return the lastModifer
	 */
	public String getLastModifier() {
		return lastModifier;
	}
	/**
	 * @param lastModifier the lastModifer to set
	 */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	/**
	 * @return the editDate
	 */
	public String getEditDate() {
		return editDate;
	}
	/**
	 * @param editDate the editDate to set
	 */
	public void setEditDate(String editDate) {
		this.editDate = editDate;
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
	
}
