/****************************************************************************
 *
 * Copyright (c) 2013 ESound Tech. All Rights Reserved.
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
 *     File name:       SatisfactionListObject.java
 *
 *     History:
 *     Date               Author                  Comments
 *     -----------------------------------------------------------------------
 *     Mar 18, 2013       Berton                  Initial Release
 *****************************************************************************/
package com.consilium.excel.models;

/**
 * <code>SatisfactionListObject</code> 滿意度調查清單資料物件
 **/

public class SatisfactionListObject
{
    /**
     * IVR進線時間
     **/
    private String ivrTime = null;

    /**
     * 值機員姓名
     **/
    private String agentName = null;

    /**
     * 案件編號
     **/
    private String caseId = null;

    /**
     * 問題一答案
     **/
    private String question1 = null;

    /**
     * 問題二答案
     **/
    private String question2 = null;

    /**
     * 問題三答案
     **/
    private String question3 = null;

    /**
     * 問題四答案
     **/
    private String question4 = null;

    /**
     * IVR進線時間
     * @param ivrTime IVR進線時間
     */
    public void setIvrTime(String ivrTime)
    {
        if (ivrTime != null)
        {
            this.ivrTime = ivrTime.trim();
        }
        else
        {
            this.ivrTime = "";
        }
    }

    /**
     * @return IVR進線時間
     */
    public String getIvrTime()
    {
        return ivrTime;
    }

    /**
     * 值機員姓名
     * @param agentName 值機員姓名
     */
    public void setAgentName(String agentName)
    {
        if (agentName != null)
        {
            this.agentName = agentName.trim();
        }
        else
        {
            this.agentName = "";
        }
    }

    /**
     * @return 值機員姓名
     */
    public String getAgentName()
    {
        return agentName;
    }

    /**
     * 案件編號
     * @param caseId 案件編號
     */
    public void setCaseId(String caseId)
    {
        if (caseId != null)
        {
            this.caseId = caseId.trim();
        }
        else
        {
            this.caseId = "";
        }
    }

    /**
     * @return 案件編號
     */
    public String getCaseId()
    {
        return caseId;
    }

    /**
     * 問題一答案, -1代表未回答, 在此用""顯示
     * @param question1 問題一答案
     */
    public void setQuestion1(String question1)
    {
        if (question1 != null)
        {
            this.question1 = question1.trim();
        }
        else
        {
            this.question1 = "";
        }
    }

    /**
     * @return 問題一答案
     */
    public String getQuestion1()
    {
        return question1;
    }

    /**
     * 問題二答案, -1代表未回答, 在此用""顯示
     * @param question2 問題二答案
     */
    public void setQuestion2(String question2)
    {
        if (question2 != null)
        {
            this.question2 = question2.trim();
        }
        else
        {
            this.question2 = "";
        }
    }

    /**
     * @return 問題二答案
     */
    public String getQuestion2()
    {
        return question2;
    }

    /**
     * 問題三答案, -1代表未回答, 在此用""顯示
     * @param question3 問題三答案
     */
    public void setQuestion3(String question3)
    {
        if (question3 != null)
        {
            this.question3 = question3.trim();
        }
        else
        {
            this.question3 = "";
        }
    }

    /**
     * @return 問題三答案
     */
    public String getQuestion3()
    {
        return question3;
    }

    /**
     * 問題四答案, -1代表未回答, 在此用""顯示
     * @param question4 問題四答案
     */
    public void setQuestion4(String question4)
    {
        if (question4 != null)
        {
            this.question4 = question4.trim();
        }
        else
        {
            this.question4 = "";
        }
    }

    /**
     * @return 問題四答案
     */
    public String getQuestion4()
    {
        return question4;
    }
}
