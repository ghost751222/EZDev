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


public class ReportObj
{
    /**
     * 時間
     **/
    protected String calltime = "";

    /**
     * 號碼
     **/
    protected String callclid = "";

    /**
     * 案號
     **/
    protected String actionid = "";

    /**
     * 秒數
     **/
    protected String callwaitingduration = "";

    /**
     * 設定時間
     *
     * @param calltime 時間
     **/
    public void setCalltime(String calltime)
    {
        if (calltime == null)
        {
            this.calltime = "";
        }
        else
        {
            this.calltime = calltime.trim();
        }
    } //method setcalltime

    /**
     * 取得時間
     *
     * @return calltime 時間
     **/
    public String getCalltime()
    {
        return this.calltime;
    } //method getcalltime

    /**
     * 設定號碼
     *
     * @param callclid 號碼
     **/
    public void setCallclid(String callclid)
    {
        if (callclid == null)
        {
            this.callclid = "";
        }
        else
        {
            this.callclid = callclid.trim();
        }
    } //method setcallclid

    /**
     * 取得號碼
     *
     * @return callclid 號碼
     **/
    public String getCallclid()
    {
        return this.callclid;
    } //method getcallclid

    /**
     * 設定案號
     *
     * @param actionid 案號
     **/
    public void setActionid(String actionid)
    {
        if (actionid == null)
        {
            this.actionid = "";
        }
        else
        {
            this.actionid = actionid.trim();
        }
    } //method setactionid

    /**
     * 取得案號
     *
     * @return actionid 案號
     **/
    public String getActionid()
    {
        return this.actionid;
    } //method getactionid

    /**
     * 設定秒數
     *
     * @param callwaitingduration 秒數
     **/
    public void setCallwaitingduration(String callwaitingduration)
    {
        if (callwaitingduration == null)
        {
            this.callwaitingduration = "";
        }
        else
        {
            this.callwaitingduration = callwaitingduration.trim();
        }
    } //method setcallwaitingduration

    /**
     * 取得秒數
     *
     * @return callwaitingduration 秒數
     **/
    public String getCallwaitingduration()
    {
        return this.callwaitingduration;
    } //method getcallwaitingduration
}
