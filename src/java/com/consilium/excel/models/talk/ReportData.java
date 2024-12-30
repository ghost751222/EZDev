
package com.consilium.excel.models.talk;

import java.util.ArrayList;

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
 *     File name:       ReportData.java
 *
 *     History:
 *     Date             Author          Comments
 *     ----------------------------------------------------------------------
 *     DEC 03, 2009     SHENG-HAN WU    Initial Release
 *     DEC 15, 2009     SHENG-HAN WU    修改 平均通話&平均鈴響&平均等待&最長等待時間
 *****************************************************************************/


/**
 * <code>ReportData</code>
 * 服務水準統計表 物件
 *
 * @author SHENG-HAN WU
 * @version 1.0
 **/
public class ReportData {
    /**
     * 報表起始日期
     **/
    protected String startDate = "";

    /**
     * 報表結束日期
     **/
    protected String endDate = "";

    /**
     * 報表查詢時間
     **/
    protected String reportTime = "";

    /**
     * 展現方式
     **/
    protected String timeType = "";

    /**
     * 平均通話(秒)
     **/
    protected String periodAverageCommunication;

    /**
     * 平均鈴響(秒)
     **/
    protected String periodAverageRang;

    /**
     * 平均等待(秒)
     **/
    protected String periodAverageWait;

    /**
     * 最長等待時間(秒)
     **/
    protected String periodMaxWait;

    /**
     * 最大等待通數(通)
     **/
    protected long periodMaxCommunication;

    /**
     * 總接通數(通)
     **/
    protected long periodTotalConnected;

    /**
     * 總放棄等待(通)
     **/
    protected long periodTotalWait;

    /**
     * 總來話數(通)
     **/
    protected long periodTotalIncoming;

    /**
     * 平均等待通數(通)
     **/
    protected long averageWaitCount;

    /**
     * 座席平均等待時間
     **/
    protected String averageLoginReady;

    /**
     * 座席平均離席時間
     **/
    protected String averageLoginNotReady;

    /**
     * 座席平均響鈴未接通數
     **/
    protected int averageCallNotAnswer;

    /**
     *
     */
    private ArrayList serviceLevelObjectList = new ArrayList();

    /**
     * 設定報表起始日期
     *
     * @param startDate 報表起始日期
     **/
    public void setStartDate(String startDate) {
        if (startDate == null) {
            this.startDate = "";
        } else {
            this.startDate = startDate.trim();
        }
    }

    /**
     * 設定報表結束日期
     *
     * @param endDate 報表結束日期
     **/
    public void setEndDate(String endDate) {
        if (endDate == null) {
            this.endDate = "";
        } else {
            this.endDate = endDate.trim();
        }
    }

    /**
     * 設定報表查詢時間
     *
     * @param reportTime 報表查詢時間
     **/
    public void setReportTime(String reportTime) {
        if (reportTime == null) {
            this.reportTime = "";
        } else {
            this.reportTime = reportTime.trim();
        }
    }

    /**
     * 設定 展現方式
     *
     * @param timeType
     **/
    public void setTimeType(String timeType) {
        if (timeType == null) {
            this.timeType = "";
        } else {
            this.timeType = timeType.trim();
        }
    }

    /**
     * 設定平均通話(秒)
     *
     * @param periodAverageCommunication 平均通話(秒)
     **/
    public void setPeriodAverageCommunication(String periodAverageCommunication) {
        this.periodAverageCommunication = periodAverageCommunication;
    }

    /**
     * 設定平均鈴響(秒)
     *
     * @param periodAverageRang 平均鈴響(秒)
     **/
    public void setPeriodAverageRang(String periodAverageRang) {
        this.periodAverageRang = periodAverageRang;
    }

    /**
     * 設定平均等待(秒)
     *
     * @param periodAverageWait 平均等待(秒)
     **/
    public void setPeriodAverageWait(String periodAverageWait) {
        this.periodAverageWait = periodAverageWait;
    }

    /**
     * 設定最長等待時間(秒)
     *
     * @param periodMaxWait 最長等待時間(秒)
     **/
    public void setPeriodMaxWait(String periodMaxWait) {
        this.periodMaxWait = periodMaxWait;
    }

    /**
     * 設定最大等待通數(通)
     *
     * @param periodMaxCommunication 最大等待通數(通)
     **/
    public void setPeriodMaxCommunication(long periodMaxCommunication) {
        this.periodMaxCommunication = periodMaxCommunication;
    }

    /**
     * 設定總接通數(通)
     *
     * @param periodTotalConnected 總接通數(通)
     **/
    public void setPeriodTotalConnected(long periodTotalConnected) {
        this.periodTotalConnected = periodTotalConnected;
    }

    /**
     * 設定總放棄等待(通)
     *
     * @param periodTotalWait 總放棄等待(通)
     **/
    public void setPeriodTotalWait(long periodTotalWait) {
        this.periodTotalWait = periodTotalWait;
    }

    /**
     * 設定總來話數(通)
     *
     * @param periodTotalIncoming 總來話數(通)
     **/
    public void setPeriodTotalIncoming(long periodTotalIncoming) {
        this.periodTotalIncoming = periodTotalIncoming;
    }

    /**
     * 設定平均等待通數(通)
     *
     * @param averageWaitCount 平均等待通數(通)
     **/
    public void setAverageWaitCount(long averageWaitCount) {
        this.averageWaitCount = averageWaitCount;
    }

    /**
     * 設定座席平均等待時間
     *
     * @param averageLoginReady 座席平均等待時間
     **/
    public void setAverageLoginReady(String averageLoginReady) {
        this.averageLoginReady = averageLoginReady;
    }

    /**
     * 設定座席平均離席時間
     *
     * @param averageLoginNotReady 座席平均離席時間
     **/
    public void setAverageLoginNotReady(String averageLoginNotReady) {
        this.averageLoginNotReady = averageLoginNotReady;
    }

    /**
     * 設定座席平均響鈴未接通數
     *
     * @param averageCallNotAnswer 座席平均響鈴未接通數
     **/
    public void setAverageCallNotAnswer(int averageCallNotAnswer) {
        this.averageCallNotAnswer = averageCallNotAnswer;
    }

    /**
     * 取得報表起始日期
     *
     * @return startDate 報表起始日期
     **/
    public String getStartDate() {
        return this.startDate;
    }

    /**
     * 取得報表結束日期
     *
     * @return endDate 報表結束日期
     **/
    public String getEndDate() {
        return this.endDate;
    }

    /**
     * 取得報表查詢時間
     *
     * @return reportTime 報表查詢時間
     **/
    public String getReportTime() {
        return this.reportTime;
    }

    /**
     * 取得展現方式
     *
     * @return startDate 展現方式
     **/
    public String getTimeType() {
        return this.timeType;
    }

    /**
     * 取得平均通話(秒)
     *
     * @return periodAverageCommunication 平均通話(秒)
     **/
    public String getPeriodAverageCommunication() {
        return this.periodAverageCommunication;
    }

    /**
     * 取得平均鈴響(秒)
     *
     * @return periodAverageRang 平均鈴響(秒)
     **/
    public String getPeriodAverageRang() {
        return this.periodAverageRang;
    }

    /**
     * 取得平均等待(秒)
     *
     * @return periodAverageWait 平均等待(秒)
     **/
    public String getPeriodAverageWait() {
        return this.periodAverageWait;
    }

    /**
     * 取得最長等待時間(秒)
     *
     * @return periodMaxWait 最長等待時間(秒)
     **/
    public String getPeriodMaxWait() {
        return this.periodMaxWait;
    }

    /**
     * 取得最大等待通數(通)
     *
     * @return periodMaxCommunication 最大等待通數(通)
     **/
    public long getPeriodMaxCommunication() {
        return this.periodMaxCommunication;
    }

    /**
     * 取得總接通數(通)
     *
     * @return periodTotalConnected 總接通數(通)
     **/
    public long getPeriodTotalConnected() {
        return this.periodTotalConnected;
    }

    /**
     * 取得總放棄等待(通)
     *
     * @return periodTotalWait 總放棄等待(通)
     **/
    public long getPeriodTotalWait() {
        return this.periodTotalWait;
    }

    /**
     * 取得總來話數(通)
     *
     * @return periodTotalIncoming 總來話數(通)
     **/
    public long getPeriodTotalIncoming() {
        return this.periodTotalIncoming;
    }

    /**
     * 平均等待通數(通)
     *
     * @return averageWaitCount 平均等待通數(通)
     **/
    public long getAverageWaitCount() {
        return this.averageWaitCount;
    }

    /**
     * 取得座席平均等待時間
     *
     * @return averageLoginReady 座席平均等待時間
     **/
    public String getAverageLoginReady() {
        return this.averageLoginReady;
    }

    /**
     * 取得座席平均離席時間
     *
     * @return averageLoginNotReady 座席平均離席時間
     **/
    public String getAverageLoginNotReady() {
        return this.averageLoginNotReady;
    }

    /**
     * 取得座席平均響鈴未接通數
     *
     * @return averageCallNotAnswer 座席平均響鈴未接通數
     **/
    public int getAverageCallNotAnswer() {
        return this.averageCallNotAnswer;
    }

    /**
     * 設定物件的列表
     *
     * @param serviceLevelObjectList 年的列表
     **/
    public void setServiceLevelObjectList(ArrayList serviceLevelObjectList) {
        this.serviceLevelObjectList = serviceLevelObjectList;
    }

    /**
     * 取得物件的列表
     *
     * @return serverLevelObjectList 年的列表
     **/
    public ArrayList getServiceLevelObjectList() {
        return this.serviceLevelObjectList;
    }

    /**
     * 將(年)資料物件放入yearObject中
     *
     * @param obj 年資料物件
     **/
    public void addServiceLevelObject(ServiceLevelObject obj) {
        this.serviceLevelObjectList.add(obj);
    }

    protected int totalAllNbr = 0;

    public void setTotalAllNbr(int totalAllNbr) {
        this.totalAllNbr = totalAllNbr;
    }

    protected int totalNbr = 0;

    public void setTotalNbr(int totalNbr) {
        this.totalNbr = totalNbr;
    }

    /**
     * 設定未等候即放棄數
     *
     * @param totalNoNbr 未等候即放棄數
     **/
    protected int totalNoNbr = 0;

    public void setTotalNoNbr(int totalNoNbr) {
        this.totalNoNbr = totalNoNbr;
    }

    /**
     * 設定等候放棄數
     *
     * @param totalNoNbrWait 等候放棄數
     **/
    protected int totalNoNbrWait = 0;

    public void setTotalNoNbrWait(int totalNoNbrWait) {
        this.totalNoNbrWait = totalNoNbrWait;
    }

    public int getTotalAllNbr() {
        return totalAllNbr;
    }

    public int getTotalNbr() {
        return totalNbr;
    }

    public int getTotalNoNbr() {
        return totalNoNbr;
    }

    public int getTotalNoNbrWait() {
        return totalNoNbrWait;
    }
}
