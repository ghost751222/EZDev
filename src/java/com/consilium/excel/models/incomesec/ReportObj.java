package com.consilium.excel.models.incomesec;

public class ReportObj {
    /**
     * 期間
     **/
    protected String ystr = "";

    /**
     * 類別
     **/
    protected String type = "";

    /**
     * 通數
     **/
    protected String cnt = "";

    /**
     * 秒數
     **/
    protected String ctime = "";

    /**
     * 設定期間
     *
     * @param ystr 期間
     **/
    public void setystr(String ystr) {
        if (ystr == null) {
            this.ystr = "";
        } else {
            this.ystr = ystr.trim();
        }
    } //method setystr

    /**
     * 取得期間
     *
     * @return ystr 期間
     **/
    public String getystr() {
        return this.ystr;
    } //method getystr

    /**
     * 設定類別
     *
     * @param type 類別
     **/
    public void setType(String type) {
        if (type == null) {
            this.type = "";
        } else {
            this.type = type.trim();
        }
    } //method settype

    /**
     * 取得類別
     *
     * @return type 類別
     **/
    public String getType() {
        return this.type;
    } //method gettype

    /**
     * 設定通數
     *
     * @param cnt 通數
     **/
    public void setCnt(String cnt) {
        if (cnt == null) {
            this.cnt = "";
        } else {
            this.cnt = cnt.trim();
        }
    } //method setcnt

    /**
     * 取得通數
     *
     * @return cnt 通數
     **/
    public String getCnt() {
        return this.cnt;
    } //method getcnt

    /**
     * 設定秒數
     *
     * @param ctime 秒數
     **/
    public void setCtime(String ctime) {
        if (ctime == null) {
            this.ctime = "";
        } else {
            this.ctime = ctime.trim();
        }
    } //method setctime

    /**
     * 取得秒數
     *
     * @return ctime 秒數
     **/
    public String getCtime() {
        return this.ctime;
    } //method getctime
}