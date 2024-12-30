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
package com.consilium.excel.models;

public class ExternalReportObject {
    /**
     * 資料時間
     **/
    protected String dataTime = "";

    /**
     * 進線數 身分證掛失轉接統計表
     **/
    protected int dataAllNbr1;

    /**
     * 進線數 非上班時段民眾轉接統計表
     **/

    protected int dataAllNbr2;

    /**
     * 身分證掛失-成功
     **/
    private int type1S;

    /**
     * 身分證掛失-失敗
     **/
    private int type1F;

    /**
     * 警政治安事項-成功
     **/
    private int type2S;

    /**
     * 警政治安事項-失敗
     **/
    private int type2F;

    /**
     * 緊急情況通報-成功
     **/
    private int type9S;

    /**
     * 緊急情況通報-失敗
     **/
    private int type9F;


    /**
     * STT-成功
     **/
    private int typeSS;

    /**
     * STT-失敗
     **/
    private int typeSF;


    /**
     * 直接轉戶-成功
     **/
    private int typeHS;

    /**
     * 直接轉戶-失敗
     **/
    private int typeHF;

    /**
     * 設定資料時間
     *
     * @param dataTime 資料時間
     **/
    public void setDataTime(String dataTime) {
        if (dataTime == null) {
            this.dataTime = "";
        } else {
            this.dataTime = dataTime.trim();
        }
    }


    /**
     * 取得資料時間
     *
     * @return dataTime 資料時間
     **/
    public String getDataTime() {
        return this.dataTime;
    }


    /**
     * 設定身分證掛失-成功
     *
     * @param type1S 身分證掛失-成功
     **/
    public void setType1S(int type1S) {
        this.type1S = type1S;
    } //method setType1S

    /**
     * 取得身分證掛失-成功
     *
     * @return type1S 身分證掛失-成功
     **/
    public int getType1S() {
        return this.type1S;
    } //method getType1S

    /**
     * 設定身分證掛失-失敗
     *
     * @param type1F 身分證掛失-失敗
     **/
    public void setType1F(int type1F) {
        this.type1F = type1F;
    } //method setType1F

    /**
     * 取得身分證掛失-失敗
     *
     * @return type1F 身分證掛失-失敗
     **/
    public int getType1F() {
        return this.type1F;
    } //method getType1F

    /**
     * 設定警政治安事項-成功
     *
     * @param type2S 警政治安事項-成功
     **/
    public void setType2S(int type2S) {
        this.type2S = type2S;
    } //method setType2S

    /**
     * 取得警政治安事項-成功
     *
     * @return type2S 警政治安事項-成功
     **/
    public int getType2S() {
        return this.type2S;
    } //method getType2S

    /**
     * 設定警政治安事項-失敗
     *
     * @param type2F 警政治安事項-失敗
     **/
    public void setType2F(int type2F) {
        this.type2F = type2F;
    } //method setType2F

    /**
     * 取得警政治安事項-失敗
     *
     * @return type2F 警政治安事項-失敗
     **/
    public int getType2F() {
        return this.type2F;
    } //method getType2F

    /**
     * 設定緊急情況通報-成功
     *
     * @param type9S 緊急情況通報-成功
     **/
    public void setType9S(int type9S) {
        this.type9S = type9S;
    } //method setType9S

    /**
     * 取得緊急情況通報-成功
     *
     * @return type9S 緊急情況通報-成功
     **/
    public int getType9S() {
        return this.type9S;
    } //method getType9S

    /**
     * 設定緊急情況通報-失敗
     *
     * @param type9F 緊急情況通報-失敗
     **/
    public void setType9F(int type9F) {
        this.type9F = type9F;
    } //method setType9F

    /**
     * 取得緊急情況通報-失敗
     *
     * @return type9F 緊急情況通報-失敗
     **/
    public int getType9F() {
        return this.type9F;
    } //method getType9F


    public int getTypeSS() {
        return typeSS;
    }

    public void setTypeSS(int typeSS) {
        this.typeSS = typeSS;
    }

    public int getTypeSF() {
        return typeSF;
    }

    public void setTypeSF(int typeSF) {
        this.typeSF = typeSF;
    }

    public int getTypeHS() {
        return typeHS;
    }

    public void setTypeHS(int typeHS) {
        this.typeHS = typeHS;
    }

    public int getTypeHF() {
        return typeHF;
    }

    public void setTypeHF(int typeHF) {
        this.typeHF = typeHF;
    }

    public int getDataAllNbr1() {
        return dataAllNbr1;
    }

    public void setDataAllNbr1(int dataAllNbr1) {
        this.dataAllNbr1 = dataAllNbr1;
    }

    public int getDataAllNbr2() {
        return dataAllNbr2;
    }

    public void setDataAllNbr2(int dataAllNbr2) {
        this.dataAllNbr2 = dataAllNbr2;
    }
}
