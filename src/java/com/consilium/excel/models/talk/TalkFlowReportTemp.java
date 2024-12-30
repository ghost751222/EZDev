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
package com.consilium.excel.models.talk;

import java.util.Hashtable;
import java.text.NumberFormat;

public class TalkFlowReportTemp
{
    /**
     * 進線數
     **/
    public static int ALL_CALL_ANSWERED = 1;
    
    /**
     * 接通數
     **/
    public static int CALL_ANSWERED = 2;
    
    /**
     * 未等候即放棄數
     **/
    public static int NO_CALL_ANSWERED = 3;
    
    /**
     * 等候放棄數
     **/
    public static int NO_CALL_ANSWERED_WAIT = 6;
    
    /**
     * 平均通話時間
     **/
    public static int AVERAGE_DCP_DURATION = 4;
    
    /**
     * 總通話時間
     **/
    public static int DCP_DURATION = 5;

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