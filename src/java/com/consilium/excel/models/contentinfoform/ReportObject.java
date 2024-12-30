/****************************************************************************
 *
 * Copyright (c) 2014 ESound Tech. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by ESound as part
 * of a ESound product for use ONLY by licensed users of the product,
 * includes CONFIDENTIAL and PROPRIETARY information of ESound.
 *
 * USE OF THIS SOFTWARE IS GOVERNED BY THE TERMS AND CONDITIONS
 * OF THE LICENSE STATEMENT AND LIMITED WARRANTY FURNISHED WITH
 * THE PRODUCT.
 *
 * IN PARTICULAR, YOU WILL INDEMNIFY AND HOLD ESOUND, ITS RELATED
 * COMPANIES AND ITS SUPPLIERS, HARMLESS FROM AND AGAINST ANY CLAIMS
 * OR LIABILITIES ARISING OUT OF THE USE, REPRODUCTION, OR DISTRIBUTION
 * OF YOUR PROGRAMS, INCLUDING ANY CLAIMS OR LIABILITIES ARISING OUT OF
 * OR RESULTING FROM THE USE, MODIFICATION, OR DISTRIBUTION OF PROGRAMS
 * OR FILES CREATED FROM, BASED ON, AND/OR DERIVED FROM THIS SOURCE
 * CODE FILE.
 *
 *
 *     File name:       ReportObject.java
 *
 *     History:
 *     Date               Author            Comments
 *     -----------------------------------------------------------------------
 *     DEC 22, 2014       mars           Initial Release
 *****************************************************************************/
package com.consilium.excel.models.contentinfoform;

/**
 * <code>ReportObject</code>
 * 新增系統更正新增確認通知單物件
 **/
public class ReportObject
{
	/**
	 * �׸�
	 **/
	private String actionId = "";

	/**
	 * ���
	 **/
	private String unitName = "";

	/**
	 * ���
	 **/
	private String sectionName = "";

	/**
	 * �O�_�O��
	 **/
	private String isOverTime = "";

	/**
	 * ���ɤ��
	 **/
	private String createTime = "";
	
	/**
	 * ���z�ɶ�
	 **/
	private String receiveTime = "";

	/**
	 * �����ɶ�
	 **/
	private String lastReplyTime = "";

	/**
	 * ���׮ɶ�
	 **/
	private String confirmTime = "";

	private String caseType = "";

	private String suggestion = "";

	private String description = "";

	private String note = "";

	private String situation ="";

	private String otherSituation ="";

	private String confirmResultScore ="";

	private String confirmResult ="";

	/**
	 * �]�w�׸�
	 *
	 * @param actionId �׸�
	 **/
	public void setActionId(String actionId)
	{
	    if (actionId == null)
	    {
	        this.actionId = "";
	    }
	    else
	    {
	        this.actionId = actionId.trim();
	    }
	} //method setActionId

	/**
	 * ���o�׸�
	 *
	 * @return actionId �׸�
	 **/
	public String getActionId()
	{
	    return this.actionId;
	} //method getActionId

	/**
	 * �]�w���
	 *
	 * @param unitName ���
	 **/
	public void setUnitName(String unitName)
	{
	    if (unitName == null)
	    {
	        this.unitName = "";
	    }
	    else
	    {
	        this.unitName = unitName.trim();
	    }
	} //method setUnitName

	/**
	 * ���o���
	 *
	 * @return unitName ���
	 **/
	public String getUnitName()
	{
	    return this.unitName;
	} //method getUnitName

	/**
	 * �]�w���
	 *
	 * @param sectionName ���
	 **/
	public void setSectionName(String sectionName)
	{
	    if (sectionName == null)
	    {
	        this.sectionName = "";
	    }
	    else
	    {
	        this.sectionName = sectionName.trim();
	    }
	} //method setSectionName

	/**
	 * ���o���
	 *
	 * @return sectionName ���
	 **/
	public String getSectionName()
	{
	    return this.sectionName;
	} //method getSectionName

	/**
	 * �]�w�O�_�O��
	 *
	 * @param isOverTime �O�_�O��
	 **/
	public void setIsOverTime(String isOverTime)
	{
	    if (isOverTime == null)
	    {
	        this.isOverTime = "";
	    }
	    else
	    {
	        this.isOverTime = isOverTime.trim();
	    }
	} //method setIsOverTime

	/**
	 * ���o�O�_�O��
	 *
	 * @return isOverTime �O�_�O��
	 **/
	public String getIsOverTime()
	{
	    return this.isOverTime;
	} //method getIsOverTime

	/**
	 * �]�w���ɤ��
	 *
	 * @param createTime ���ɤ��
	 **/
	public void setCreateTime(String createTime)
	{
	    if (createTime == null)
	    {
	        this.createTime = "";
	    }
	    else
	    {
	        this.createTime = createTime.trim();
	    }
	} //method setCreateTime

	/**
	 * ���o���ɤ��
	 *
	 * @return createTime ���ɤ��
	 **/
	public String getCreateTime()
	{
	    return this.createTime;
	} //method getCreateTime
	
	/**
	 * �]�w���z�ɶ�
	 *
	 * @param receiveTime ���z�ɶ�
	 **/
	public void setReceiveTime(String receiveTime)
	{
	    if (receiveTime == null)
	    {
	        this.receiveTime = "";
	    }
	    else
	    {
	        this.receiveTime = receiveTime.trim();
	    }
	} //method setReceiveTime

	/**
	 * ���o���z�ɶ�
	 *
	 * @return receiveTime ���z�ɶ�
	 **/
	public String getReceiveTime()
	{
	    return this.receiveTime;
	} //method getReceiveTime

	
	/**
	 * �]�w�����ɶ�
	 *
	 * @param lastReplyTime �����ɶ�
	 **/
	public void setLastReplyTime(String lastReplyTime)
	{
	    if (lastReplyTime == null)
	    {
	        this.lastReplyTime = "";
	    }
	    else
	    {
	        this.lastReplyTime = lastReplyTime.trim();
	    }
	} //method setLastReplyTime

	/**
	 * ���o�����ɶ�
	 *
	 * @return lastReplyTime �����ɶ�
	 **/
	public String getLastReplyTime()
	{
	    return this.lastReplyTime;
	} //method getLastReplyTime

	/**
	 * �]�w���׮ɶ�
	 *
	 * @param confirmTime ���׮ɶ�
	 **/
	public void setConfirmTime(String confirmTime)
	{
	    if (confirmTime == null)
	    {
	        this.confirmTime = "";
	    }
	    else
	    {
	        this.confirmTime = confirmTime.trim();
	    }
	} //method setConfirmTime

	/**
	 * ���o���׮ɶ�
	 *
	 * @return confirmTime ���׮ɶ�
	 **/
	public String getConfirmTime()
	{
	    return this.confirmTime;
	} //method getConfirmTime

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}



	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}



	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	public String getOtherSituation() {
		return otherSituation;
	}

	public void setOtherSituation(String otherSituation) {
		this.otherSituation = otherSituation;
	}

	public String getConfirmResultScore() {
		return confirmResultScore;
	}

	public void setConfirmResultScore(String confirmResultScore) {
		this.confirmResultScore = confirmResultScore;
	}

	public String getConfirmResult() {
		return confirmResult;
	}

	public void setConfirmResult(String confirmResult) {
		this.confirmResult = confirmResult;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
