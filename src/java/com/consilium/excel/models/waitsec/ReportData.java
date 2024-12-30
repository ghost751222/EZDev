package com.consilium.excel.models.waitsec;

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
 * OR LIABILITIES ARISING OUT OF THE USE, REPRODUCTION, OR DIStrIBUTION
 * OF YOUR PROGRAMS, INCLUDING ANY CLAIMS OR LIABILITIES ARISING OUT OF
 * OR RESULTING FROM THE USE, MODIFICATION, OR DIStrIBUTION OF PROGRAMS
 * OR FILES CREATED FROM, BASED ON, AND/OR DERIVED FROM THIS SOURCE
 * CODE FILE.
 *
 *
 *     File name:       ReportModel.java
 *
 *     History:
 *     Date             Author              Comments
 *     -----------------------------------------------------------------------
 *     Nov 12, 2013     Kevin              Initial Release
 ****************************************************************************/


public class ReportData
{
    /**
     *報表產生時間
     **/
    protected String reportTime = "";

    /**
     *報表起始日期
     **/
    protected String startDate = "";

    /**
     *報表結束日期
     **/
    protected String endDate = "";

    /**
     * 設定報表產生時間
     *
     * @param reportTime 報表產生時間
     **/
    public void setReportTime(String reportTime)
    {
        if (reportTime == null)
        {
            this.reportTime = "";
        }
        else
        {
            this.reportTime = reportTime.trim();
        }
    }

    /**
     * 設定報表起始日期
     *
     * @param startDate 報表起始日期
     **/
    public void setStartDate(String startDate)
    {
        if (startDate == null)
        {
            this.startDate = "";
        }
        else
        {
            this.startDate = startDate.trim();
        }
    }

    /**
     * 設定報表結束日期
     *
     * @param endDate 報表結束日期
     **/
    public void setEndDate(String endDate)
    {
        if (endDate == null)
        {
            this.endDate = "";
        }
        else
        {
            this.endDate = endDate.trim();
        }
    }

    /**
     * 取得報表產生時間
     *
     * @return reportTime 報表產生時間
     **/
    public String getReportTime()
    {
        return this.reportTime;
    }

    /**
     * 取得報表起始日期
     *
     * @return startDate 報表起始日期
     **/
    public String getStartDate()
    {
        return this.startDate;
    }

    /**
     * 取得報表結束日期
     *
     * @return endDate 報表結束日期
     **/
    public String getEndDate()
    {
        return this.endDate;
    }
}
