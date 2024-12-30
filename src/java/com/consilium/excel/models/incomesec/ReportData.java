package com.consilium.excel.models.incomesec;

public class ReportData {
    /**
     * 報表產生時間
     **/
    protected String reportTime = "";

    /**
     * 報表起始日期
     **/
    protected String startDate = "";

    /**
     * 報表結束日期
     **/
    protected String endDate = "";

    /**
     * 設定報表產生時間
     *
     * @param reportTime 報表產生時間
     **/
    public void setReportTime(String reportTime) {
        if (reportTime == null) {
            this.reportTime = "";
        } else {
            this.reportTime = reportTime.trim();
        }
    }

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
     * 取得報表產生時間
     *
     * @return reportTime 報表產生時間
     **/
    public String getReportTime() {
        return this.reportTime;
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
}
