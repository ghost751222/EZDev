
package com.consilium.excel.models.talk;

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
 *     File name:       DefaultObject.java
 *
 *     History:
 *     Date                        Comments
 *     -----------------------------------------------------------------------
 *     DEC 4, 2009     Initial Release
 *****************************************************************************/

/**
 * <code>DefaultObject</code>
 * 服務水準統計表 基礎物件
 * @author      SHENG-HAN WU
 * @version     1.0
 **/
public class DefaultObject
{
    /**
     *資料日期
     **/
    protected String dataTime = "";

    /**
     *平均通話(秒)
     **/
    protected int averageCommunication;

    /**
     *平均鈴響(秒)
     **/
    protected int averageRang;

    /**
     *平均等待(秒)
     **/
    protected int averageWait;

    /**
     *最長等待時間(秒)
     **/
    protected int maxWait;

    /**
     *最大等待通數(通)
     **/
    protected int maxCommunication;

    /**
     *總接通數(通)
     **/
    protected int totalConnected;

    /**
     *座席總等待時間
     **/
    protected int totalLoginReady;

    /**
     *座席總離席時間
     **/
    protected int totalLoginNotReady;

    /**
     *座席總響鈴未接通數
     **/
    protected int callNotAnswer;

    /**
     *等待通數(通)
     **/
    protected int waitCallnbr;

    /**
     *REPORTCOUNT
     **/
    protected int reportCount;

    /**
     *總放棄等待(通)
     **/
    protected int totalWait;

    /**
     *總來話數(通)
     **/
    protected int totalIncoming;

    /**
     * 設定資料日期
     *
     * @param dataTime 資料日期
     **/
    public void setDataTime(String dataTime)
    {
        if (dataTime == null)
        {
            this.dataTime = "";
        }
        else
        {
            this.dataTime = dataTime.trim();
        }
    }

    /**
     * 設定平均通話(秒)
     *
     * @param averageCommunication 平均通話(秒)
     **/
    public void setAverageCommunication(int averageCommunication)
    {
        this.averageCommunication = averageCommunication;
    }

    /**
     * 設定平均鈴響(秒)
     *
     * @param averageRang 平均鈴響(秒)
     **/
    public void setAverageRang(int averageRang)
    {
        this.averageRang = averageRang;
    }

    /**
     * 設定平均等待(秒)
     *
     * @param averageWait 平均等待(秒)
     **/
    public void setAverageWait(int averageWait)
    {
        this.averageWait = averageWait;
    }

    /**
     * 設定最長等待時間(秒)
     *
     * @param maxWait 最長等待時間(秒)
     **/
    public void setMaxWait(int maxWait)
    {
        this.maxWait = maxWait;
    }

    /**
     * 設定最大等待通數(通)
     *
     * @param maxCommunication 最大等待通數(通)
     **/
    public void setMaxCommunication(int maxCommunication)
    {
        this.maxCommunication = maxCommunication;
    }

    /**
     * 設定總接通數(通)
     *
     * @param totalConnected 總接通數(通)
     **/
    public void setTotalConnected(int totalConnected)
    {
        this.totalConnected = totalConnected;
    }

    /**
     * 設定座席總等待時間
     *
     * @param totalLoginReady 座席總等待時間
     **/
    public void setTotalLoginReady(int totalLoginReady)
    {
        this.totalLoginReady = totalLoginReady;
    }

    /**
     * 設定座席總離席時間
     *
     * @param totalLoginNotReady 座席總離席時間
     **/
    public void setTotalLoginNotReady(int totalLoginNotReady)
    {
        this.totalLoginNotReady = totalLoginNotReady;
    }

    /**
     * 設定座席總響鈴未接通數
     *
     * @param callNotAnswer 座席總響鈴未接通數
     **/
    public void setCallNotAnswer(int callNotAnswer)
    {
        this.callNotAnswer = callNotAnswer;
    }

    /**
     * 設定等待通數(通)
     *
     * @param waitCallnbr 等待通數(通)
     **/
    public void setWaitCallnbr(int waitCallnbr)
    {
        this.waitCallnbr = waitCallnbr;
    }

    /**
     * 設定REPORTCOUNT
     *
     * @param reportCount REPORTCOUNT
     **/
    public void setReportCount(int reportCount)
    {
        this.reportCount = reportCount;
    }

    /**
     * 設定總放棄等待(通)
     *
     * @param totalWait 總放棄等待(通)
     **/
    public void setTotalWait(int totalWait)
    {
        this.totalWait = totalWait;
    }

    /**
     * 設定總來話數(通)
     *
     * @param totalIncoming 總來話數(通)
     **/
    public void setTotalIncoming(int totalIncoming)
    {
        this.totalIncoming = totalIncoming;
    }

    /**
     * 取得資料日期
     *
     * @return dataTime 資料日期
     **/
    public String getDataTime()
    {
        return this.dataTime;
    }

    /**
     * 取得平均通話(秒)
     *
     * @return averageCommunication 平均通話(秒)
     **/
    public int getAverageCommunication()
    {
        return this.averageCommunication;
    }

    /**
     * 取得平均鈴響(秒)
     *
     * @return averageRang 平均鈴響(秒)
     **/
    public int getAverageRang()
    {
        return this.averageRang;
    }

    /**
     * 取得平均等待(秒)
     *
     * @return averageWait 平均等待(秒)
     **/
    public int getAverageWait()
    {
        return this.averageWait;
    }

    /**
     * 取得最長等待時間(秒)
     *
     * @return maxWait 最長等待時間(秒)
     **/
    public int getMaxWait()
    {
        return this.maxWait;
    }

    /**
     * 取得最大等待通數(通)
     *
     * @return maxCommunication 最大等待通數(通)
     **/
    public int getMaxCommunication()
    {
        return this.maxCommunication;
    }

    /**
     * 取得總接通數(通)
     *
     * @return totalConnected 總接通數(通)
     **/
    public int getTotalConnected()
    {
        return this.totalConnected;
    }

    /**
     * 取得座席總等待時間
     *
     * @return totalLoginReady 座席總等待時間
     **/
    public int getTotalLoginReady()
    {
        return this.totalLoginReady;
    }

    /**
     * 取得座席總離席時間
     *
     * @return totalLoginNotReady 座席總離席時間
     **/
    public int getTotalLoginNotReady()
    {
        return this.totalLoginNotReady;
    }

    /**
     * 取得座席總響鈴未接通數
     *
     * @return callNotAnswer 座席總響鈴未接通數
     **/
    public int getCallNotAnswer()
    {
        return this.callNotAnswer;
    }

    /**
     * 取得等待通數(通)
     *
     * @return waitCallnbr 等待通數(通)
     **/
    public int getWaitCallnbr()
    {
        return this.waitCallnbr;
    }

    /**
     * 取得REPORTCOUNT
     *
     * @return reportCount REPORTCOUNT
     **/
    public int getReportCount()
    {
        return this.reportCount;
    }

    /**
     * 取得總放棄等待(通)
     *
     * @return totalWait 總放棄等待(通)
     **/
    public int getTotalWait()
    {
        return this.totalWait;
    }

    /**
     * 取得總來話數(通)
     *
     * @return totalIncoming 總來話數(通)
     **/
    public int getTotalIncoming()
    {
        return this.totalIncoming;
    }

}