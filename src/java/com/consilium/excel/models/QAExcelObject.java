/****************************************************************************
 *
 * Copyright (c) 2006 ESound Tech. All Rights Reserved.
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
 *     File name:       QAExcelObject.java
 *
 *     History:
 *     Date                        Comments
 *     -----------------------------------------------------------------------
 *     JUN 24, 2011     Initial Release
 *****************************************************************************/
package com.consilium.excel.models;
/**
 * <code>QAExcelObject</code>
 * 問答集資料物件
 * @author      Mars
 * @version     1.0
 **/
public class QAExcelObject
{
	/**
	 *流水號
	 **/
	protected String sId = "";

	/**
	 *問題
	 **/
	protected String question = "";

	/**
	 *回答
	 **/
	protected String answer = "";

	/**
	 *相關查詢關鍵字
	 **/
	protected String keyword = "";

	/**
	 *意見
	 **/
	protected String option = "";

	/**
	 *資料有效日期
	 **/
	protected String efficientTime = "";

	/**
	 *聯絡人姓名1
	 **/
	protected String connectName1 = "";

	/**
	 *聯絡人電話1
	 **/
	protected String tel1 = "";

	/**
	 *分機1
	 **/
	protected String exten1 = "";

	/**
	 *聯絡人姓名2
	 **/
	protected String connectName2 = "";

	/**
	 *聯絡人電話2
	 **/
	protected String tel2 = "";

	/**
	 *分機2
	 **/
	protected String exten2 = "";


	/**
	 * 流水號
	 **/
	protected String rSId = "";

	/**
	 * 對應號
	 **/
	protected String rPId = "";

	/**
	 * 問題
	 **/
	protected String rQuestion = "";

	/**
	 * 回答
	 **/
	protected String rAnswer = "";

	/**
	 * 相關查詢關鍵字
	 **/
	protected String rKeyWord = "";

	/**
	 * 意見
	 **/
	protected String rOption = "";

	/**
	 * 資料有效日期
	 **/
	protected String rEfficientTime = "";

	/**
	 * 聯絡人姓名1
	 **/
	protected String rConnectName1 = "";

	/**
	 * 聯絡人電話1
	 **/
	protected String rTel1 = "";

	/**
	 * 分機1
	 **/
	protected String rExten1 = "";

	/**
	 * 聯絡人姓名2
	 **/
	protected String rConnectName2 = "";

	/**
	 * 聯絡人電話2
	 **/
	protected String rTel2 = "";

	/**
	 * 分機2
	 **/
	protected String rExten2 = "";


	protected String status = "";

	protected String rStatus = "";

	protected String pId = "";

	/**
	 *單位ID
	 **/
	protected String unitId = "";

	/**
	 *科室ID
	 **/
	protected String sectionId = "";

	/**
	 * 建立時間
	 **/
	private String createTime = "";

	/**
	 * 最後修改時間
	 **/
	private String lastUpdateTime = "";

	/**
	 * 設定建立時間
	 *
	 * @param createTime 建立時間
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
	 * 取得建立時間
	 *
	 * @return createTime 建立時間
	 **/
	public String getCreateTime()
	{
		return this.createTime;
	} //method getCreateTime

	/**
	 * 設定最後修改時間
	 *
	 * @param lastUpdateTime 最後修改時間
	 **/
	public void setLastUpdateTime(String lastUpdateTime)
	{
		if (lastUpdateTime == null)
		{
			this.lastUpdateTime = "";
		}
		else
		{
			this.lastUpdateTime = lastUpdateTime.trim();
		}
	} //method setLastUpdateTime

	/**
	 * 取得最後修改時間
	 *
	 * @return lastUpdateTime 最後修改時間
	 **/
	public String getLastUpdateTime()
	{
		return this.lastUpdateTime;
	} //method getLastUpdateTime


	/**
	 * 設定流水號
	 *
	 * @param sId 流水號
	 **/
	public void setSId(String sId)
	{
		if (sId == null)
		{
			this.sId = "";
		}
		else
		{
			this.sId = sId.trim();
		}
	}

	/**
	 * 設定問題
	 *
	 * @param question 問題
	 **/
	public void setQuestion(String question)
	{
		if (question == null)
		{
			this.question = "";
		}
		else
		{
			this.question = question.trim();
		}
	}

	/**
	 * 設定回答
	 *
	 * @param answer 回答
	 **/
	public void setAnswer(String answer)
	{
		if (answer == null)
		{
			this.answer = "";
		}
		else
		{
			this.answer = answer.trim();
		}
	}

	/**
	 * 設定相關查詢關鍵字
	 *
	 * @param keyword 相關查詢關鍵字
	 **/
	public void setKeyword(String keyword)
	{
		if (keyword == null)
		{
			this.keyword = "";
		}
		else
		{
			this.keyword = keyword.trim();
		}
	}

	/**
	 * 設定資料有效日期
	 *
	 * @param efficientTime 資料有效日期
	 **/
	public void setEfficientTime(String efficientTime)
	{
		if (efficientTime == null)
		{
			this.efficientTime = "";
		}
		else
		{
			this.efficientTime = efficientTime.trim();
		}
	}

	/**
	 * 設定聯絡人姓名1
	 *
	 * @param connectName1 聯絡人姓名1
	 **/
	public void setConnectName1(String connectName1)
	{
		if (connectName1 == null)
		{
			this.connectName1 = "";
		}
		else
		{
			this.connectName1 = connectName1.trim();
		}
	}

	/**
	 * 設定聯絡人電話1
	 *
	 * @param tel1 聯絡人電話1
	 **/
	public void setTel1(String tel1)
	{
		if (tel1 == null)
		{
			this.tel1 = "";
		}
		else
		{
			this.tel1 = tel1.trim();
		}
	}

	/**
	 * 設定分機1
	 *
	 * @param exten1 分機1
	 **/
	public void setExten1(String exten1)
	{
		if (exten1 == null)
		{
			this.exten1 = "";
		}
		else
		{
			this.exten1 = exten1.trim();
		}
	}

	/**
	 * 設定聯絡人姓名2
	 *
	 * @param connectName2 聯絡人姓名2
	 **/
	public void setConnectName2(String connectName2)
	{
		if (connectName2 == null)
		{
			this.connectName2 = "";
		}
		else
		{
			this.connectName2 = connectName2.trim();
		}
	}

	/**
	 * 設定聯絡人電話2
	 *
	 * @param tel2 聯絡人電話2
	 **/
	public void setTel2(String tel2)
	{
		if (tel2 == null)
		{
			this.tel2 = "";
		}
		else
		{
			this.tel2 = tel2.trim();
		}
	}

	/**
	 * 設定分機2
	 *
	 * @param exten2 分機2
	 **/
	public void setExten2(String exten2)
	{
		if (exten2 == null)
		{
			this.exten2 = "";
		}
		else
		{
			this.exten2 = exten2.trim();
		}
	}

	/**
	 * 取得流水號
	 *
	 * @return sId 流水號
	 **/
	public String getSId()
	{
		return this.sId;
	}

	/**
	 * 取得問題
	 *
	 * @return question 問題
	 **/
	public String getQuestion()
	{
		return this.question;
	}

	/**
	 * 取得回答
	 *
	 * @return answer 回答
	 **/
	public String getAnswer()
	{
		return this.answer;
	}

	/**
	 * 取得相關查詢關鍵字
	 *
	 * @return keyword 相關查詢關鍵字
	 **/
	public String getKeyword()
	{
		return this.keyword;
	}

	/**
	 * 取得資料有效日期
	 *
	 * @return efficientTime 資料有效日期
	 **/
	public String getEfficientTime()
	{
		return this.efficientTime;
	}

	/**
	 * 取得聯絡人姓名1
	 *
	 * @return connectName1 聯絡人姓名1
	 **/
	public String getConnectName1()
	{
		return this.connectName1;
	}

	/**
	 * 取得聯絡人電話1
	 *
	 * @return tel1 聯絡人電話1
	 **/
	public String getTel1()
	{
		return this.tel1;
	}

	/**
	 * 取得分機1
	 *
	 * @return exten1 分機1
	 **/
	public String getExten1()
	{
		return this.exten1;
	}

	/**
	 * 取得聯絡人姓名2
	 *
	 * @return connectName2 聯絡人姓名2
	 **/
	public String getConnectName2()
	{
		return this.connectName2;
	}

	/**
	 * 取得聯絡人電話2
	 *
	 * @return tel2 聯絡人電話2
	 **/
	public String getTel2()
	{
		return this.tel2;
	}

	/**
	 * 取得分機2
	 *
	 * @return exten2 分機2
	 **/
	public String getExten2()
	{
		return this.exten2;
	}


	/**
	 * 意見
	 *
	 * @param option 意見
	 **/
	public void setOption(String option)
	{
		if(option==null)
		{
			this.option="";
		}
		else
		{
			this.option=option;
		}
	}

	/**
	 * 意見
	 *
	 * @return option 意見
	 **/
	public String getOption()
	{
		return this.option;
	}

	/**
	 * 設定流水號
	 *
	 * @param rSId 流水號
	 **/
	public void setRSId(String rSId)
	{
		if (rSId == null)
		{
			this.rSId = "";
		}
		else
		{
			this.rSId = rSId.trim();
		}
	} //method setRSId

	/**
	 * 取得流水號
	 *
	 * @return rSId 流水號
	 **/
	public String getRSId()
	{
		return this.rSId;
	} //method getRSId



	public void setRPId(String rPId)
	{
		if (rPId == null)
		{
			this.rPId = "";
		}
		else
		{
			this.rPId = rPId.trim();
		}
	} //method setRPId


	public String getRPId()
	{
		return this.rPId;
	} //method getRPId


	/**
	 * 設定問題
	 *
	 * @param rQuestion 問題
	 **/
	public void setRQuestion(String rQuestion)
	{
		if (rQuestion == null)
		{
			this.rQuestion = "";
		}
		else
		{
			this.rQuestion = rQuestion.trim();
		}
	} //method setRQuestion

	/**
	 * 取得問題
	 *
	 * @return rQuestion 問題
	 **/
	public String getRQuestion()
	{
		return this.rQuestion;
	} //method getRQuestion

	/**
	 * 設定回答
	 *
	 * @param rAnswer 回答
	 **/
	public void setRAnswer(String rAnswer)
	{
		if (rAnswer == null)
		{
			this.rAnswer = "";
		}
		else
		{
			this.rAnswer = rAnswer.trim();
		}
	} //method setRAnswer

	/**
	 * 取得回答
	 *
	 * @return rAnswer 回答
	 **/
	public String getRAnswer()
	{
		return this.rAnswer;
	} //method getRAnswer

	/**
	 * 設定相關查詢關鍵字
	 *
	 * @param rKeyWord 相關查詢關鍵字
	 **/
	public void setRKeyWord(String rKeyWord)
	{
		if (rKeyWord == null)
		{
			this.rKeyWord = "";
		}
		else
		{
			this.rKeyWord = rKeyWord.trim();
		}
	} //method setRKeyWord

	/**
	 * 取得相關查詢關鍵字
	 *
	 * @return rKeyWord 相關查詢關鍵字
	 **/
	public String getRKeyWord()
	{
		return this.rKeyWord;
	} //method getRKeyWord

	/**
	 * 設定意見
	 *
	 * @param rOption 意見
	 **/
	public void setROption(String rOption)
	{
		if (rOption == null)
		{
			this.rOption = "";
		}
		else
		{
			this.rOption = rOption.trim();
		}
	} //method setROption

	/**
	 * 取得意見
	 *
	 * @return rOption 意見
	 **/
	public String getROption()
	{
		return this.rOption;
	} //method getROption

	/**
	 * 設定資料有效日期
	 *
	 * @param rEfficientTime 資料有效日期
	 **/
	public void setREfficientTime(String rEfficientTime)
	{
		if (rEfficientTime == null)
		{
			this.rEfficientTime = "";
		}
		else
		{
			this.rEfficientTime = rEfficientTime.trim();
		}
	} //method setREfficientTime

	/**
	 * 取得資料有效日期
	 *
	 * @return rEfficientTime 資料有效日期
	 **/
	public String getREfficientTime()
	{
		return this.rEfficientTime;
	} //method getREfficientTime

	/**
	 * 設定聯絡人姓名1
	 *
	 * @param rConnectName1 聯絡人姓名1
	 **/
	public void setRConnectName1(String rConnectName1)
	{
		if (rConnectName1 == null)
		{
			this.rConnectName1 = "";
		}
		else
		{
			this.rConnectName1 = rConnectName1.trim();
		}
	} //method setRConnectName1

	/**
	 * 取得聯絡人姓名1
	 *
	 * @return rConnectName1 聯絡人姓名1
	 **/
	public String getRConnectName1()
	{
		return this.rConnectName1;
	} //method getRConnectName1

	/**
	 * 設定聯絡人電話1
	 *
	 * @param rTel1 聯絡人電話1
	 **/
	public void setRTel1(String rTel1)
	{
		if (rTel1 == null)
		{
			this.rTel1 = "";
		}
		else
		{
			this.rTel1 = rTel1.trim();
		}
	} //method setRTel1

	/**
	 * 取得聯絡人電話1
	 *
	 * @return rTel1 聯絡人電話1
	 **/
	public String getRTel1()
	{
		return this.rTel1;
	} //method getRTel1

	/**
	 * 設定分機1
	 *
	 * @param rExten1 分機1
	 **/
	public void setRExten1(String rExten1)
	{
		if (rExten1 == null)
		{
			this.rExten1 = "";
		}
		else
		{
			this.rExten1 = rExten1.trim();
		}
	} //method setRExten1

	/**
	 * 取得分機1
	 *
	 * @return rExten1 分機1
	 **/
	public String getRExten1()
	{
		return this.rExten1;
	} //method getRExten1

	/**
	 * 設定聯絡人姓名2
	 *
	 * @param rConnectName2 聯絡人姓名2
	 **/
	public void setRConnectName2(String rConnectName2)
	{
		if (rConnectName2 == null)
		{
			this.rConnectName2 = "";
		}
		else
		{
			this.rConnectName2 = rConnectName2.trim();
		}
	} //method setRConnectName2

	/**
	 * 取得聯絡人姓名2
	 *
	 * @return rConnectName2 聯絡人姓名2
	 **/
	public String getRConnectName2()
	{
		return this.rConnectName2;
	} //method getRConnectName2

	/**
	 * 設定聯絡人電話2
	 *
	 * @param rTel2 聯絡人電話2
	 **/
	public void setRTel2(String rTel2)
	{
		if (rTel2 == null)
		{
			this.rTel2 = "";
		}
		else
		{
			this.rTel2 = rTel2.trim();
		}
	} //method setRTel2

	/**
	 * 取得聯絡人電話2
	 *
	 * @return rTel2 聯絡人電話2
	 **/
	public String getRTel2()
	{
		return this.rTel2;
	} //method getRTel2

	/**
	 * 設定分機2
	 *
	 * @param rExten2 分機2
	 **/
	public void setRExten2(String rExten2)
	{
		if (rExten2 == null)
		{
			this.rExten2 = "";
		}
		else
		{
			this.rExten2 = rExten2.trim();
		}
	} //method setRExten2

	/**
	 * 取得分機2
	 *
	 * @return rExten2 分機2
	 **/
	public String getRExten2()
	{
		return this.rExten2;
	} //method getRExten2


	public void setStatus(String status)
	{
		if (status == null)
		{
			this.status = "";
		}
		else
		{
			this.status = status.trim();
		}
	} //method setStatus


	public String getStatus()
	{
		return this.status;
	} //method getStatus


	public void setRStatus(String rStatus)
	{
		if (rStatus == null)
		{
			this.rStatus = "";
		}
		else
		{
			this.rStatus = rStatus.trim();
		}
	} //method setRStatus


	public String getRStatus()
	{
		return this.rStatus;
	}


	public void setPId(String pId)
	{
		if (pId == null)
		{
			this.pId = "";
		}
		else
		{
			this.pId = pId.trim();
		}
	} //method setPId


	public String getPId()
	{
		return this.pId;
	}

	/**
	 * 設定單位ID
	 *
	 * @param unitId 單位ID
	 **/
	public void setUnitId(String unitId)
	{
		if (unitId == null)
		{
			this.unitId = "";
		}
		else
		{
			this.unitId = unitId.trim();
		}
	}

	/**
	 * 設定科室ID
	 *
	 * @param sectionId 科室ID
	 **/
	public void setSectionId(String sectionId)
	{
		if (sectionId == null)
		{
			this.sectionId = "";
		}
		else
		{
			this.sectionId = sectionId.trim();
		}
	}

	/**
	 * 取得單位ID
	 *
	 * @return unitId 單位ID
	 **/
	public String getUnitId()
	{
		return this.unitId;
	}

	/**
	 * 取得科室ID
	 *
	 * @return sectionId 科室ID
	 **/
	public String getSectionId()
	{
		return this.sectionId;
	}

}
