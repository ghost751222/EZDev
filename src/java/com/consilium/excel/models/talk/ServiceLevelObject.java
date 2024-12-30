
package com.consilium.excel.models.talk;
/****************************************************************************
 *
 * Copyright (c) 2009 ESound Tech. All Rights Reserved.
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
 *     File name:       ServiceLevelObject.java
 *
 *     History:
 *     Date             Author          Comments
 *     ----------------------------------------------------------------------
 *     DEC 03, 2009     SHENG-HAN WU    Initial Release
 *****************************************************************************/



/**
 * <code>ServiceLevelObject</code>
 * 服務水準統計表 物件
 * @version     1.0
 **/
public class ServiceLevelObject
{
    /**
     *資料日期
     **/
    protected String dataTime = "" ;

    /**
     *平均通話(秒)
     **/
    protected String averageCommunication ;

    /**
     *平均鈴響(秒)
     **/
    protected String averageRang ;

    /**
     *平均等待(秒)
     **/
    protected String averageWait ;

    /**
     *平均等待通數(通)
     **/
    protected long averageWaitCount ;

    /**
     *最長等待時間(秒)
     **/
    protected String maxWait ;

    /**
     *最大等待通數(通)
     **/
    protected long maxCommunication ;

    /**
     *總接通數(通)
     **/
    protected long totalConnected ;

    /**
     *總放棄等待(通)
     **/
    protected long totalWait ;

    /**
     *總來話數(通)
     **/
    protected long totalIncoming ;

    /**
     *座席平均等待時間
     **/
    protected String averageLoginReady;

    /**
     *座席平均離席時間
     **/
    protected String averageLoginNotReady;

    /**
     *座席平均響鈴未接通數
     **/
    protected int averageCallNotAnswer;

    /**
     * 設定資料日期
     *
     * @param dataTime 資料日期
     **/
    public void setDataTime ( String dataTime )
    {
        if ( dataTime == null )
        {
            this.dataTime = "" ;
        }
        else
        {
            this.dataTime = dataTime.trim () ;
        }
    }

    /**
     * 設定平均通話(秒)
     *
     * @param averageCommunication 平均通話(秒)
     **/
    public void setAverageCommunication ( String averageCommunication )
    {
        this.averageCommunication = averageCommunication ;
    }

    /**
     * 設定平均鈴響(秒)
     *
     * @param averageRang 平均鈴響(秒)
     **/
    public void setAverageRang ( String averageRang )
    {
        this.averageRang = averageRang ;
    }

    /**
     * 設定平均等待(秒)
     *
     * @param averageWait 平均等待(秒)
     **/
    public void setAverageWait ( String averageWait )
    {
        this.averageWait = averageWait ;
    }

    /**
     * 設定平均等待通數(通)
     *
     * @param averageWaitCount 平均等待通數(通)
     **/
    public void setAverageWaitCount ( long averageWaitCount )
    {
        this.averageWaitCount = averageWaitCount ;
    }

    /**
     * 設定最長等待時間(秒)
     *
     * @param maxWait 最長等待時間(秒)
     **/
    public void setMaxWait ( String maxWait )
    {
        this.maxWait = maxWait ;
    }

    /**
     * 設定最大等待通數(通)
     *
     * @param maxCommunication 最大等待通數(通)
     **/
    public void setMaxCommunication ( long maxCommunication )
    {
        this.maxCommunication = maxCommunication ;
    }

    /**
     * 設定總接通數(通)
     *
     * @param totalConnected 總接通數(通)
     **/
    public void setTotalConnected ( long totalConnected )
    {
        this.totalConnected = totalConnected ;
    }

    /**
     * 設定總放棄等待(通)
     *
     * @param totalWait 總放棄等待(通)
     **/
    public void setTotalWait ( long totalWait )
    {
        this.totalWait = totalWait ;
    }

    /**
     * 設定總來話數(通)
     *
     * @param totalIncoming 總來話數(通)
     **/
    public void setTotalIncoming ( long totalIncoming )
    {
        this.totalIncoming = totalIncoming ;
    }

    /**
     * 設定座席平均等待時間
     *
     * @param averageLoginReady 座席平均等待時間
     **/
    public void setAverageLoginReady(String averageLoginReady)
    {
        this.averageLoginReady = averageLoginReady;
    }

    /**
     * 設定座席平均離席時間
     *
     * @param averageLoginNotReady 座席平均離席時間
     **/
    public void setAverageLoginNotReady(String averageLoginNotReady)
    {
        this.averageLoginNotReady = averageLoginNotReady;
    }

    /**
     * 設定座席平均響鈴未接通數
     *
     * @param averageCallNotAnswer 座席平均響鈴未接通數
     **/
    public void setAverageCallNotAnswer(int averageCallNotAnswer)
    {
        this.averageCallNotAnswer = averageCallNotAnswer;
    }

    /**
     * 取得資料日期
     *
     * @return dataTime 資料日期
     **/
    public String getDataTime ()
    {
        return this.dataTime ;
    }

    /**
     * 取得平均通話(秒)
     *
     * @return averageCommunication 平均通話(秒)
     **/
    public String getAverageCommunication ()
    {
        return this.averageCommunication ;
    }

    /**
     * 取得平均鈴響(秒)
     *
     * @return averageRang 平均鈴響(秒)
     **/
    public String getAverageRang ()
    {
        return this.averageRang ;
    }

    /**
     * 取得平均等待(秒)
     *
     * @return averageWait 平均等待(秒)
     **/
    public String getAverageWait ()
    {
        return this.averageWait ;
    }

    /**
     * 平均等待通數(通)
     *
     * @return averageWaitCount 平均等待通數(通)
     **/
    public long getAverageWaitCount ()
    {
        return this.averageWaitCount ;
    }

    /**
     * 取得最長等待時間(秒)
     *
     * @return maxWait 最長等待時間(秒)
     **/
    public String getMaxWait ()
    {
        return this.maxWait ;
    }

    /**
     * 取得最大等待通數(通)
     *
     * @return maxCommunication 最大等待通數(通)
     **/
    public long getMaxCommunication ()
    {
        return this.maxCommunication ;
    }

    /**
     * 取得總接通數(通)
     *
     * @return totalConnected 總接通數(通)
     **/
    public long getTotalConnected ()
    {
        return this.totalConnected ;
    }

    /**
     * 取得總放棄等待(通)
     *
     * @return totalWait 總放棄等待(通)
     **/
    public long getTotalWait ()
    {
        return this.totalWait ;
    }

    /**
     * 取得總來話數(通)
     *
     * @return totalIncoming 總來話數(通)
     **/
    public long getTotalIncoming ()
    {
        return this.totalIncoming ;
    }

    /**
     * 取得座席平均等待時間
     *
     * @return averageLoginReady 座席平均等待時間
     **/
    public String getAverageLoginReady()
    {
        return this.averageLoginReady;
    }

    /**
     * 取得座席平均離席時間
     *
     * @return averageLoginNotReady 座席平均離席時間
     **/
    public String getAverageLoginNotReady()
    {
        return this.averageLoginNotReady;
    }

    /**
     * 取得座席平均響鈴未接通數
     *
     * @return averageCallNotAnswer 座席平均響鈴未接通數
     **/
    public int getAverageCallNotAnswer()
    {
        return this.averageCallNotAnswer;
    }



    /**
     * 設定進線數
     *
     * @param dataAllNbr 進線數
     **/
    protected int dataAllNbr=0;
    public void setDataAllNbr(int dataAllNbr)
    {
        this.dataAllNbr = dataAllNbr;
    }


    /**
     * 設定接通數
     *
     * @param dataNbr 接通數
     **/
    protected int dataNbr=0;
    public void setDataNbr(int dataNbr)
    {
        this.dataNbr = dataNbr;
    }




    /**
     * 設定未等候即放棄數
     *
     * @param dataNoNbr 未等候即放棄數
     **/
    protected int dataNoNbr=0;
    public void setDataNoNbr(int dataNoNbr)
    {
        this.dataNoNbr = dataNoNbr;
    }


    /**
     * 設定等候放棄數
     *
     * @param dataNoNbrWait 等候放棄數
     **/
    protected int dataNoNbrWait=0;
    public void setDataNoNbrWait(int dataNoNbrWait)
    {
        this.dataNoNbrWait = dataNoNbrWait;
    }

    public int getDataAllNbr() {
        return dataAllNbr;
    }

    public int getDataNbr() {
        return dataNbr;
    }

    public int getDataNoNbr() {
        return dataNoNbr;
    }

    public int getDataNoNbrWait() {
        return dataNoNbrWait;
    }
}
