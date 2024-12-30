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
 *     File name:       TalkFlowReportData.java
 *
 *     History:
 *     Date               Author                  Comments
 *     -----------------------------------------------------------------------
 *     Dec 09, 2009       David               Initial Release
 *****************************************************************************/
package com.consilium.excel.models.talk;

import java.text.NumberFormat;
import java.util.ArrayList;

public class TalkFlowReportData
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
     *列印人
     **/
    protected String userName = "";
    
    /**
     *列印單位
     **/
    protected String userUnit = "";
    
    /**
     *總接通數
     **/
    protected int totalNbr;

    protected int avgNbr;
    protected int maxNbr;

    
    /**
     *未等候即放棄數
     **/
    protected int totalNoNbr;
    protected int avgNoNbr;
    protected int maxNoNbr;
    
    /**
     *等候放棄數
     **/
    protected int totalNoNbrWait;
    protected int avgNoNbrWait;
    protected int maxNoNbrWait;
    /**
     *總進線數
     **/
    protected int totalAllNbr;
    protected int avgAllNbr;
    protected int maxAllNbr;

    /**
     *全部總通話時間
     **/
    protected String totalTotal = "";
    
    /**
     *全部平均通話時間
     **/
    protected String totalAverage = "";



    //最多等候通數
    protected  int totalWaitCallNBR;
    protected  int maxWaitCallNBR;
    protected  int avgWaitCallNBR;
    //最長等候時間
    protected  int totalCallDurationBeforeAnswer;
    protected  int maxCallDurationBeforeAnswer;
    protected  int avgCallDurationBeforeAnswer;

    //平均響鈴時間
    protected  int totalRingDurAnswerCall;
    protected  int maxRingDurAnswerCall;
    protected  int avgRingDurAnswerCall;
    //平均等候時間
    protected  int totalCallsDurationBeforeAnswer;
    protected  int maxCallsDurationBeforeAnswer;
    protected  int avgCallsDurationBeforeAnswer;
    //平均通話長度
    protected  int totalCallsAgentCommDuration;
    protected  int maxCallsAgentCommDuration;
    protected  int avgCallsAgentCommDuration;
    //座席響鈴未接通總數
    protected  int totalNBRInboundCallNotAnswer;
    protected  int maxNBRInboundCallNotAnswer;
    protected  int avgNBRInboundCallNotAnswer;




    /**
     * 存放分時話務流量統計值資料
     **/
    private ArrayList dataList = new ArrayList();
    
    /**
     * 設定分時話務流量統計值的列表
     *
     * @param dataList 分時話務流量統計值的列表
     **/
    public void setDataList(ArrayList dataList)
    {
        this.dataList = dataList;
    }
    
    /**
     * 取得分時話務流量統計值的列表
     *
     * @return dataList 分時話務流量統計值的列表
     **/
    public ArrayList getDataList()
    {
        return this.dataList;
    }
    
    /**
     * 將(服務水準報表統計值)資料物件放入 ReportObject 中
     *
     * @param obj 服務水準報表統計值物件
     **/
    public void addDataList(TalkFlowReportObject obj)
    {
        this.dataList.add(obj);
    }
    
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
     * 設定列印人
     *
     * @param userName 列印人
     **/
     public void setUserName(String userName)
     {
         if (userName == null)
         {
             this.userName = "";
         }
         else
         {
             this.userName = userName.trim();
         }
     }
    
    /**
     * 設定列印單位
     *
     * @param userUnit 列印單位
     **/
     public void setUserUnit(String userUnit)
     {
         if (userUnit == null)
         {
             this.userUnit = "";
         }
         else
         {
             this.userUnit = userUnit.trim();
         }
     }
    
    /**
     * 設定總接通數
     *
     * @param totalNbr 總接通數
     **/
     public void setTotalNbr(int totalNbr)
     {
         this.totalNbr = totalNbr;
     }
     
    /**
     * 設定未等候即放棄數
     *
     * @param totalNoNbr 未等候即放棄數
     **/
     public void setTotalNoNbr(int totalNoNbr)
     {
         this.totalNoNbr = totalNoNbr;
     }
     
    /**
     * 設定等候放棄數
     *
     * @param totalNoNbrWait 等候放棄數
     **/
     public void setTotalNoNbrWait(int totalNoNbrWait)
     {
         this.totalNoNbrWait = totalNoNbrWait;
     }
     
    /**
     * 設定總進線數
     *
     * @param totalAllNbr 總進線數
     **/
     public void setTotalAllNbr(int totalAllNbr)
     {
         this.totalAllNbr = totalAllNbr;
     }
    
    /**
     * 設定全部總通話時間
     *
     * @param totalTotal 全部總通話時間
     **/
     public void setTotalTotal(String totalTotal)
     {
         if (totalTotal == null)
         {
             this.totalTotal = "";
         }
         else
         {
             this.totalTotal = totalTotal.trim();
         }
     }
     
    /**
     * 設定全部平均通話時間
     *
     * @param totalAverage 全部平均通話時間
     **/
     public void setTotalAverage(String totalAverage)
     {
         if (totalAverage == null)
         {
             this.totalAverage = "";
         }
         else
         {
             this.totalAverage = totalAverage.trim();
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
    
    /**
     * 取得列印人
     *
     * @return userName 列印人
     **/
     public String getUserName()
     {
         return this.userName;
     }
    
    /**
     * 取得列印單位
     *
     * @return userUnit 列印單位
     **/
     public String getUserUnit()
     {
         return this.userUnit;
     }
    
    /**
     * 取得總接通數
     *
     * @return totalNbr 總接通數
     **/
     public int getTotalNbr()
     {
         return this.totalNbr;
     }
     
    /**
     * 取得未等候即放棄數
     *
     * @return totalNoNbr 未等候即放棄數
     **/
     public int getTotalNoNbr()
     {
         return this.totalNoNbr;
     }
     
    /**
     * 取得等候放棄數
     *
     * @return totalNoNbrWait 等候放棄數
     **/
     public int getTotalNoNbrWait()
     {
         return this.totalNoNbrWait;
     }
     
    /**
     * 取得總進線數
     *
     * @return totalAllNbr 總進線數
     **/
     public int getTotalAllNbr()
     {
         return this.totalAllNbr;
     }
    
    /**
     * 取得全部總通話時間
     *
     * @return totalTotal 全部總通話時間
     **/
     public String getTotalTotal()
     {
         return this.totalTotal;
     }
     
    /**
     * 取得全部平均通話時間
     *
     * @return totalAverage 全部平均通話時間
     **/
     public String getTotalAverage()
     {
         return this.totalAverage;
     }


    public int getAvgNbr() {
        return avgNbr;
    }

    public void setAvgNbr(int avgNbr) {
        this.avgNbr = avgNbr;
    }

    public int getMaxNbr() {
        return maxNbr;
    }

    public void setMaxNbr(int maxNbr) {
        this.maxNbr = maxNbr;
    }

    public int getAvgNoNbr() {
        return avgNoNbr;
    }

    public void setAvgNoNbr(int avgNoNbr) {
        this.avgNoNbr = avgNoNbr;
    }

    public int getMaxNoNbr() {
        return maxNoNbr;
    }

    public void setMaxNoNbr(int maxNoNbr) {
        this.maxNoNbr = maxNoNbr;
    }

    public int getAvgNoNbrWait() {
        return avgNoNbrWait;
    }

    public void setAvgNoNbrWait(int avgNoNbrWait) {
        this.avgNoNbrWait = avgNoNbrWait;
    }

    public int getMaxNoNbrWait() {
        return maxNoNbrWait;
    }

    public void setMaxNoNbrWait(int maxNoNbrWait) {
        this.maxNoNbrWait = maxNoNbrWait;
    }

    public int getAvgAllNbr() {
        return avgAllNbr;
    }

    public void setAvgAllNbr(int avgAllNbr) {
        this.avgAllNbr = avgAllNbr;
    }

    public int getMaxAllNbr() {
        return maxAllNbr;
    }

    public void setMaxAllNbr(int maxAllNbr) {
        this.maxAllNbr = maxAllNbr;
    }


    public int getTotalWaitCallNBR() {
        return totalWaitCallNBR;
    }

    public void setTotalWaitCallNBR(int totalWaitCallNBR) {
        this.totalWaitCallNBR = totalWaitCallNBR;
    }

    public int getMaxWaitCallNBR() {
        return maxWaitCallNBR;
    }

    public void setMaxWaitCallNBR(int maxWaitCallNBR) {
        this.maxWaitCallNBR = maxWaitCallNBR;
    }

    public int getAvgWaitCallNBR() {
        return avgWaitCallNBR;
    }

    public void setAvgWaitCallNBR(int avgWaitCallNBR) {
        this.avgWaitCallNBR = avgWaitCallNBR;
    }

    public int getTotalCallDurationBeforeAnswer() {
        return totalCallDurationBeforeAnswer;
    }

    public void setTotalCallDurationBeforeAnswer(int totalCallDurationBeforeAnswer) {
        this.totalCallDurationBeforeAnswer = totalCallDurationBeforeAnswer;
    }

    public int getMaxCallDurationBeforeAnswer() {
        return maxCallDurationBeforeAnswer;
    }

    public void setMaxCallDurationBeforeAnswer(int maxCallDurationBeforeAnswer) {
        this.maxCallDurationBeforeAnswer = maxCallDurationBeforeAnswer;
    }

    public int getAvgCallDurationBeforeAnswer() {
        return avgCallDurationBeforeAnswer;
    }

    public void setAvgCallDurationBeforeAnswer(int avgCallDurationBeforeAnswer) {
        this.avgCallDurationBeforeAnswer = avgCallDurationBeforeAnswer;
    }

    public int getTotalRingDurAnswerCall() {
        return totalRingDurAnswerCall;
    }

    public void setTotalRingDurAnswerCall(int totalRingDurAnswerCall) {
        this.totalRingDurAnswerCall = totalRingDurAnswerCall;
    }

    public int getMaxRingDurAnswerCall() {
        return maxRingDurAnswerCall;
    }

    public void setMaxRingDurAnswerCall(int maxRingDurAnswerCall) {
        this.maxRingDurAnswerCall = maxRingDurAnswerCall;
    }

    public int getAvgRingDurAnswerCall() {
        return avgRingDurAnswerCall;
    }

    public void setAvgRingDurAnswerCall(int avgRingDurAnswerCall) {
        this.avgRingDurAnswerCall = avgRingDurAnswerCall;
    }

    public int getTotalCallsDurationBeforeAnswer() {
        return totalCallsDurationBeforeAnswer;
    }

    public void setTotalCallsDurationBeforeAnswer(int totalCallsDurationBeforeAnswer) {
        this.totalCallsDurationBeforeAnswer = totalCallsDurationBeforeAnswer;
    }

    public int getMaxCallsDurationBeforeAnswer() {
        return maxCallsDurationBeforeAnswer;
    }

    public void setMaxCallsDurationBeforeAnswer(int maxCallsDurationBeforeAnswer) {
        this.maxCallsDurationBeforeAnswer = maxCallsDurationBeforeAnswer;
    }

    public int getAvgCallsDurationBeforeAnswer() {
        return avgCallsDurationBeforeAnswer;
    }

    public void setAvgCallsDurationBeforeAnswer(int avgCallsDurationBeforeAnswer) {
        this.avgCallsDurationBeforeAnswer = avgCallsDurationBeforeAnswer;
    }

    public int getTotalCallsAgentCommDuration() {
        return totalCallsAgentCommDuration;
    }

    public void setTotalCallsAgentCommDuration(int totalCallsAgentCommDuration) {
        this.totalCallsAgentCommDuration = totalCallsAgentCommDuration;
    }

    public int getMaxCallsAgentCommDuration() {
        return maxCallsAgentCommDuration;
    }

    public void setMaxCallsAgentCommDuration(int maxCallsAgentCommDuration) {
        this.maxCallsAgentCommDuration = maxCallsAgentCommDuration;
    }

    public int getAvgCallsAgentCommDuration() {
        return avgCallsAgentCommDuration;
    }

    public void setAvgCallsAgentCommDuration(int avgCallsAgentCommDuration) {
        this.avgCallsAgentCommDuration = avgCallsAgentCommDuration;
    }

    public int getTotalNBRInboundCallNotAnswer() {
        return totalNBRInboundCallNotAnswer;
    }

    public void setTotalNBRInboundCallNotAnswer(int totalNBRInboundCallNotAnswer) {
        this.totalNBRInboundCallNotAnswer = totalNBRInboundCallNotAnswer;
    }

    public int getMaxNBRInboundCallNotAnswer() {
        return maxNBRInboundCallNotAnswer;
    }

    public void setMaxNBRInboundCallNotAnswer(int maxNBRInboundCallNotAnswer) {
        this.maxNBRInboundCallNotAnswer = maxNBRInboundCallNotAnswer;
    }

    public int getAvgNBRInboundCallNotAnswer() {
        return avgNBRInboundCallNotAnswer;
    }

    public void setAvgNBRInboundCallNotAnswer(int avgNBRInboundCallNotAnswer) {
        this.avgNBRInboundCallNotAnswer = avgNBRInboundCallNotAnswer;
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
