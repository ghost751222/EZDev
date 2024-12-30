package com.consilium.excel.models.talk;

import java.text.NumberFormat;
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
 *     File name:       TalkFlowReportObject.java
 *
 *     History:
 *     Date               Author                  Comments
 *     -----------------------------------------------------------------------
 *     Dec 09, 2009       David               Initial Release
 *****************************************************************************/


public class TalkFlowReportObject
{
    /**
     *資料時間
     **/
    protected String dataTime = "";

    /**
     *接通數
     **/
    protected int dataNbr;

    /**
     *未等候即放棄數
     **/
    protected int dataNoNbr;

    /**
     *等候放棄數
     **/
    protected int dataNoNbrWait;

    /**
     *進線數
     **/
    protected int dataAllNbr;

    /**
     *總通話時間
     **/
    protected String dataTotal = "";

    /**
     *平均通話時間
     **/
    protected String dataAverage = "";

    /**
     * 設定資料時間
     *
     * @param dataTime 資料時間
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
     * 設定接通數
     *
     * @param dataNbr 接通數
     **/
    public void setDataNbr(int dataNbr)
    {
        this.dataNbr = dataNbr;
    }

    /**
     * 設定未等候即放棄數
     *
     * @param dataNoNbr 未等候即放棄數
     **/
    public void setDataNoNbr(int dataNoNbr)
    {
        this.dataNoNbr = dataNoNbr;
    }

    /**
     * 設定等候放棄數
     *
     * @param dataNoNbrWait 等候放棄數
     **/
    public void setDataNoNbrWait(int dataNoNbrWait)
    {
        this.dataNoNbrWait = dataNoNbrWait;
    }

    /**
     * 設定進線數
     *
     * @param dataAllNbr 進線數
     **/
    public void setDataAllNbr(int dataAllNbr)
    {
        this.dataAllNbr = dataAllNbr;
    }

    /**
     * 設定總通話時間
     *
     * @param dataTotal 總通話時間
     **/
    public void setDataTotal(String dataTotal)
    {
        if (dataTotal == null)
        {
            this.dataTotal = "";
        }
        else
        {
            this.dataTotal = dataTotal.trim();
        }
    }

    /**
     * 設定平均通話時間
     *
     * @param dataAverage 平均通話時間
     **/
    public void setDataAverage(String dataAverage)
    {
        if (dataAverage == null)
        {
            this.dataAverage = "";
        }
        else
        {
            this.dataAverage = dataAverage.trim();
        }
    }

    /**
     * 取得資料時間
     *
     * @return dataTime 資料時間
     **/
    public String getDataTime()
    {
        return this.dataTime;
    }

    /**
     * 取得接通數
     *
     * @return dataNbr 接通數
     **/
    public int getDataNbr()
    {
        return this.dataNbr;
    }

    /**
     * 取得未等候即放棄數
     *
     * @return dataNoNbr 未等候即放棄數
     **/
    public int getDataNoNbr()
    {
        return this.dataNoNbr;
    }

    /**
     * 取得等候放棄數
     *
     * @return dataNoNbr 等候放棄數
     **/
    public int getDataNoNbrWait()
    {
        return this.dataNoNbrWait;
    }

    /**
     * 取得進線數
     *
     * @return dataAllNbr 進線數
     **/
    public int getDataAllNbr()
    {
        return this.dataAllNbr;
    }

    /**
     * 取得總通話時間
     *
     * @return dataTotal 總通話時間
     **/
    public String getDataTotal()
    {
        return this.dataTotal;
    }

    /**
     * 取得平均通話時間
     *
     * @return dataAverage 平均通話時間
     **/
    public String getDataAverage()
    {
        return this.dataAverage;
    }
}
