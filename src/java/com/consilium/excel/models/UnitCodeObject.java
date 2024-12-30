package com.consilium.excel.models;

/****************************************************************************
 *
 * Copyright (c) 2010 ESound Tech. All Rights Reserved.
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
 *     File name:       UnitCodeObject.java
 *
 *     History:
 *     Date                        Comments
 *     -----------------------------------------------------------------------
 *     Feb 22, 2010     Initial Release
 *     Jan 18, 2011     加入sNamd,orderNumber    mars
 *****************************************************************************/


/**
 * UnitCodeObject是由單位資訊所組成的單位代碼物件.
 *
 * @author Eric Chuang
 * @version 1.3
 **/
public class UnitCodeObject {
    //上層單位代碼
    private String superUnit;

    //單位代碼
    private String unitCode;

    //單位類別
    private String unitType;

    //單位名稱
    private String unitName = "";

    //行政區域代碼
    private String cityCode;

    //單位連絡人
    private String unitContact;

    //單位連絡電話
    private String unitTel;

    //單位傳真號碼
    private String unitFax;

    //單位地址
    private String unitAddress;

    //警察單位層級1:局 2:分局 3:派出所
    private int policeOfficeLevel;

    //是否為性侵害加害人系統管理單位(是:Y)
    private String isDvpcPoliceSuperUnit;

    //單位短名(如:消防署高雄港務消防隊災害預防課 就存預防課)
    private String sName = "";

    //排序號碼
    private int orderNumber;

    //所屬上層
    private String spUnitCode;

    /**
     * UnitCodeObject的建構式,用來產生<code>UnitCodeObject</code>物件.
     **/
    public UnitCodeObject() {
    } //constructor UnitCodeObject

    /**
     * 取得上層單位代碼.
     *
     * @return 上層單位代碼.
     **/
    public String getSuperUnit() {
        return this.superUnit;
    } //method getSuperUnit

    /**
     * 設定上層單位代碼.
     *
     * @param superUnit 上層單位代碼.
     **/
    public void setSuperUnit(String superUnit) {
        this.superUnit = superUnit;
    } //method setSuperUnit

    /**
     * 取得所屬上層.
     *
     * @return 所屬上層.
     **/
    public String getSpUnitCode() {
        return this.spUnitCode;
    } //method getSpUnitCode

    /**
     * 設定所屬上層.
     *
     * @param spUnitCode 所屬上層.
     **/
    public void setSpUnitCode(String spUnitCode) {
        this.spUnitCode = spUnitCode;
    } //method setSpUnitCode

    /**
     * 取得單位代碼.
     *
     * @return 單位代碼.
     **/
    public String getUnitCode() {
        return this.unitCode;
    } //method getUnitCode

    /**
     * 設定單位代碼.
     *
     * @param unitCode 單位代碼.
     **/
    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    } //method setUnitCode

    /**
     * 取得單位類別.
     *
     * @return 單位類別.
     **/
    public String getUnitType() {
        return this.unitType;
    } //method getUnitType

    /**
     * 設定單位類別.
     *
     * @param unitType 單位類別.
     **/
    public void setUnitType(String unitType) {
        this.unitType = unitType;
    } //method setUnitType

    /**
     * 取得單位名稱.
     *
     * @return 單位名稱.
     **/
    public String getUnitName() {
        return this.unitName;
    } //method getUnitName

    /**
     * 設定單位名稱.
     *
     * @param unitName 單位名稱.
     **/
    public void setUnitName(String unitName) {
        if (unitName == null) {
            this.unitName = "";
        } else {
            this.unitName = unitName;
        }

    } //method setUnitName

    /**
     * 取得行政區域代碼.
     *
     * @return 行政區域代碼.
     **/
    public String getCityCode() {
        return this.cityCode;
    } //method getCityCode

    /**
     * 設定行政區域代碼.
     *
     * @param cityCode 行政區域代碼.
     **/
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    } //method setCityCode

    /**
     * 取得連絡人姓名.
     *
     * @return 連絡人姓名.
     **/
    public String getUnitContact() {
        return this.unitContact;
    } //method getUnitContact

    /**
     * 設定連絡人姓名.
     *
     * @param unitContact 連絡人姓名.
     **/
    public void setUnitContact(String unitContact) {
        this.unitContact = unitContact;
    } //method setUnitContact

    /**
     * 取得連絡電話.
     *
     * @return 連絡電話.
     **/
    public String getUnitTel() {
        return this.unitTel;
    } //method getUnitTel

    /**
     * 設定連絡電話.
     *
     * @param unitTel 連絡電話.
     **/
    public void setUnitTel(String unitTel) {
        this.unitTel = unitTel;
    } //method setUnitTel


    /**
     * 設定單位短名.
     *
     * @param sName 單位短名.
     **/
    public void setSName(String sName) {
        if (sName == null) {
            this.sName = "";
        } else {
            this.sName = sName;
        }

    }

    /**
     * 設定排序號碼.
     *
     * @param orderNumber 排序號碼.
     **/
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }


    /**
     * 取得單位傳真號碼.
     *
     * @return 單位傳真號碼.
     **/
    public String getUnitFax() {
        return this.unitFax;
    } //method getUnitFax

    /**
     * 設定單位傳真號碼.
     *
     * @param unitFax 單位傳真號碼.
     **/
    public void setUnitFax(String unitFax) {
        this.unitFax = unitFax;
    } //method setUnitFax

    /**
     * 取得單位地址.
     *
     * @return 單位地址.
     **/
    public String getUnitAddress() {
        return this.unitAddress;
    } //method getUnitAddress

    /**
     * 設定單位地址.
     *
     * @param unitAddress 單位地址.
     **/
    public void setUnitAddress(String unitAddress) {
        this.unitAddress = unitAddress;
    } //method setUnitAddress

    /**
     * 取得警察單位層級.
     *
     * @return 單位層級.
     **/
    public int getPoliceOfficeLevel() {
        return this.policeOfficeLevel;
    } //method getPoliceOfficeLevel


    /**
     * 取得排序號碼.
     *
     * @return 排序號碼.
     **/
    public int getOrderNumber() {
        return this.orderNumber;
    } //method getPoliceOfficeLevel

    /**
     * 取得單位短名.
     *
     * @return 單位短名
     **/
    public String getSName() {
        return this.sName;
    } //method getPoliceOfficeLevel


    /**
     * 設定警察單位層級.
     *
     * @param policeOfficeLevel 單位層級.
     **/
    public void setPoliceOfficeLevel(int policeOfficeLevel) {
        this.policeOfficeLevel = policeOfficeLevel;
    } //method setPoliceOfficeLevel

    /**
     * 取得是否為性侵害加害人系統管理單位.
     *
     * @return 是否為性侵害加害人系統管理單位.
     **/
    public String getIsDvpcPoliceSuperUnit() {
        return this.isDvpcPoliceSuperUnit;
    } //method getIsDvpcPoliceSuperUnit

    /**
     * 設定是否為性侵害加害人系統管理單位.
     *
     * @param isDvpcPoliceSuperUnit 是否為性侵害加害人系統管理單位.
     **/
    public void setIsDvpcPoliceSuperUnit(String isDvpcPoliceSuperUnit) {
        if (isDvpcPoliceSuperUnit == null || isDvpcPoliceSuperUnit.equals("")) {
            this.isDvpcPoliceSuperUnit = "N";
        } else {
            this.isDvpcPoliceSuperUnit = "Y";
        }
    } //method setIsDvpcPoliceSuperUnit

    /**
     * 取得警察單位的分局名稱.
     *
     * @return 警察單位的分局名稱.
     **/
    public String getBranchName() {
        String branchName = "";

        if (this.unitType != null && this.unitType.equals("D")) {
            int bIdx = this.unitName.lastIndexOf("警察局");
            int eIdx = this.unitName.lastIndexOf("分局");
            if (bIdx >= 0 && eIdx > bIdx) {
                branchName = this.unitName.substring(bIdx + 3, eIdx + 2);
            }
        }

        return branchName;
    } //method getBranchName

    /**
     * 取得單位名稱縮寫.
     *
     * @return 單位名稱縮寫.
     **/
    public String getShortName() {
        String shortName = this.unitName;
        if (shortName != null) {
            if (this.unitType != null && this.unitType.equals("D")) {
                if (this.policeOfficeLevel == 2) {
                    int idx = shortName.lastIndexOf("警察局");
                    if (idx >= 0) {
                        shortName = shortName.substring(idx + 3);
                    }
                } else if (this.policeOfficeLevel == 3) {
                    int idx = shortName.lastIndexOf("分局");
                    if (idx >= 0) {
                        shortName = shortName.substring(idx + 2);
                    }
                }
            }
        } else {
            shortName = "";
        }

        return shortName;
    } //method getShortName

    /**
     * 判斷此單位是否可以使用帳戶登入系統.
     *
     * @return 此單位是否可以使用帳戶登入系統.
     **/
    public boolean canUseAccount() {
        return true; //this.getUnitType().equals("H") & (this.getUnitCode().equals("A02") | this.getUnitCode().equals("B02"));
    } //method canUseAccount

    /**
     * 判斷此單位是否可受理派遣案件.
     *
     * @return 此單位是否可受理派遣案件.
     **/
    public boolean canReceiveCase() {
        return this.getUnitType().equals("M"); //this.getUnitType().equals("H") & !(this.getUnitCode().equals("A01") | this.getUnitCode().equals("A02") | this.getUnitCode().equals("B02"));
    } //method canReceiveCase

    /**
     * 判斷此單位是否可開立案件.
     *
     * @return 此單位是否可開立案件.
     **/
    public boolean canCreateCase() {
        return this.getUnitType().equals("H") & (this.getUnitCode().equals("A02") | this.getUnitCode().equals("B02"));
    } //method canCreateCase

    /**
     * 判斷此單位是否可使用諮詢記錄表內的聯絡及通譯紀錄.
     *
     * @return 此單位是否可使用諮詢記錄表內的聯絡及通譯紀錄.
     **/
    public boolean canUseNotes() {
        return this.getUnitType().equals("H") & (this.getUnitCode().equals("A02") | this.getUnitCode().equals("B02"));
    } //method canUseNotes

} //class UnitCodeObject