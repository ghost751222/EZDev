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
 *     File name:       TalkFlowReportTemp.java
 *
 *     History:
 *     Date               Author                  Comments
 *     -----------------------------------------------------------------------
 *     Dec 09, 2009       David               Initial Release
 *****************************************************************************/
package com.consilium.excel.models;

import java.util.Hashtable;
import java.text.NumberFormat;

public class ExternalReportTemp
{
    /**
     * 進線數
     **/
    public static int ALL_CALL_ANSWERED = 1;
    
    /**
     * 身分證掛失-成功
     **/
    public static int TYPE1S = 2;
    
    /**
     * 身分證掛失-失敗
     **/
    public static int TYPE1F = 3;
    
    /**
     *  警政治安事項-成功
     **/
    public static int TYPE2S = 4;
    
    /**
     *  警政治安事項-失敗
     **/
    public static int TYPE2F = 5;
    
    /**
     * 緊急情況通報-成功
     **/
    public static int TYPE9S = 6;

    /**
     * 緊急情況通報-失敗
     **/
    public static int TYPE9F = 7;



    /**
     * STT-成功
     **/
    public static int TYPESS = 8;

    /**
     * STT-失敗
     **/
    public static int TYPESF = 9;


    /**
     * 直接轉戶所-成功
     **/
    public static int TYPEHS = 10;

    /**
     * 直接轉戶所-失敗
     **/
    public static int TYPEHF = 11;

    /**
     * 加總
     **/
    public static String THE_TOTAL = "TOTAL";

    /**
     * 存放分時話務流量資料統計值資料
     **/
    private Hashtable valueTable = new Hashtable();
    
    /**
     * 設定分時話務流量資料統計值.
     * 
     * @param date 日期
     * @param type 統計值類別
     * @param value 統計值
     * 
     **/
    public void setCallByHourValue(String date, int type, int value) 
    {
        Integer returnValue;
        valueTable.put(date + "-" + type, new Integer(value));
        if ((returnValue = (Integer)valueTable.put(THE_TOTAL + "-" + type, new Integer(value))) != null) 
        {
            valueTable.put(THE_TOTAL + "-" + type, new Integer(value + returnValue.intValue()));
        }
    }

    /**
     * 取得分時話務流量資料統計值.
     * 
     * @return 表格值
     **/
    public int getCallByHourValue(String date, int type) 
    {
        Integer returnValue = (Integer)valueTable.get(date + "-" + type);
        return (returnValue != null) ? returnValue.intValue() : 0;
    }
    
    public String formatDate(int sec)
    {
        double ss, mm, hh;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(2); 
        
        ss = sec%60;
        mm = (sec/60) % 60;
        hh = sec/3600;

        return nf.format(hh)+":"+nf.format(mm)+":"+nf.format(ss);
    }
}
