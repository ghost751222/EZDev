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
package com.consilium.excel.models;

import java.util.ArrayList;

public class ExternalReportData
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
	 * 身分證掛失-成功總數
	 **/
	private int totalType1S;

	/**
	 * 身分證掛失-失敗總數
	 **/
	private int totalType1F;

	/**
	 * 警政治安事項-成功總數
	 **/
	private int totalType2S;

	/**
	 * 警政治安事項-失敗總數
	 **/
	private int totalType2F;

	/**
	 * 緊急情況通報-成功總數
	 **/
	private int totalType9S;

	/**
	 * 緊急情況通報-失敗總數
	 **/
	private int totalType9F;

	/**
	 * STT-成功總數
	 **/
	private int totalTypeSS;

	/**
	 * STT-失敗總數
	 **/

	private int totalTypeSF;

	/**
	 * 直接轉戶-成功總數
	 **/
	private int totalTypeHS;
	/**
	 * 直接轉戶-失敗總數
	 **/
	private int totalTypeHF;
	/**
     *總進線數
     **/
    protected int totalAllNbr1;
	protected int totalAllNbr2;
    
    /**
     * 存放非上班時段民眾轉接統計表統計值資料
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
    public void addDataList(ExternalReportObject obj)
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
 	 * 設定身分證掛失-成功總數
 	 *
 	 * @param totalType1S 身分證掛失-成功總數
 	 **/
 	public void setTotalType1S(int totalType1S)
 	{
 	    this.totalType1S = totalType1S;
 	} //method setTotalType1S

 	/**
 	 * 取得身分證掛失-成功總數
 	 *
 	 * @return totalType1S 身分證掛失-成功總數
 	 **/
 	public int getTotalType1S()
 	{
 	    return this.totalType1S;
 	} //method getTotalType1S

 	/**
 	 * 設定身分證掛失-失敗總數
 	 *
 	 * @param totalType1F 身分證掛失-失敗總數
 	 **/
 	public void setTotalType1F(int totalType1F)
 	{
 	    this.totalType1F = totalType1F;
 	} //method setTotalType1F

 	/**
 	 * 取得身分證掛失-失敗總數
 	 *
 	 * @return totalType1F 身分證掛失-失敗總數
 	 **/
 	public int getTotalType1F()
 	{
 	    return this.totalType1F;
 	} //method getTotalType1F

 	/**
 	 * 設定警政治安事項-成功總數
 	 *
 	 * @param totalType2S 警政治安事項-成功總數
 	 **/
 	public void setTotalType2S(int totalType2S)
 	{
 	    this.totalType2S = totalType2S;
 	} //method setTotalType2S

 	/**
 	 * 取得警政治安事項-成功總數
 	 *
 	 * @return totalType2S 警政治安事項-成功總數
 	 **/
 	public int getTotalType2S()
 	{
 	    return this.totalType2S;
 	} //method getTotalType2S

 	/**
 	 * 設定警政治安事項-失敗總數
 	 *
 	 * @param totalType2F 警政治安事項-失敗總數
 	 **/
 	public void setTotalType2F(int totalType2F)
 	{
 	    this.totalType2F = totalType2F;
 	} //method setTotalType2F

 	/**
 	 * 取得警政治安事項-失敗總數
 	 *
 	 * @return totalType2F 警政治安事項-失敗總數
 	 **/
 	public int getTotalType2F()
 	{
 	    return this.totalType2F;
 	} //method getTotalType2F

 	/**
 	 * 設定緊急情況通報-成功總數
 	 *
 	 * @param totalType9S 緊急情況通報-成功總數
 	 **/
 	public void setTotalType9S(int totalType9S)
 	{
 	    this.totalType9S = totalType9S;
 	} //method setTotalType9S

 	/**
 	 * 取得緊急情況通報-成功總數
 	 *
 	 * @return totalType9S 緊急情況通報-成功總數
 	 **/
 	public int getTotalType9S()
 	{
 	    return this.totalType9S;
 	} //method getTotalType9S

 	/**
 	 * 設定緊急情況通報-失敗總數
 	 *
 	 * @param totalType9F 緊急情況通報-失敗總數
 	 **/
 	public void setTotalType9F(int totalType9F)
 	{
 	    this.totalType9F = totalType9F;
 	} //method setTotalType9F

 	/**
 	 * 取得緊急情況通報-失敗總數
 	 *
 	 * @return totalType9F 緊急情況通報-失敗總數
 	 **/
 	public int getTotalType9F()
 	{
 	    return this.totalType9F;
 	} //method getTotalType9F

	public int getTotalTypeSS() {
		return totalTypeSS;
	}

	public void setTotalTypeSS(int totalTypeSS) {
		this.totalTypeSS = totalTypeSS;
	}

	public int getTotalTypeSF() {
		return totalTypeSF;
	}

	public void setTotalTypeSF(int totalTypeSF) {
		this.totalTypeSF = totalTypeSF;
	}

	public int getTotalTypeHS() {
		return totalTypeHS;
	}

	public void setTotalTypeHS(int totalTypeHS) {
		this.totalTypeHS = totalTypeHS;
	}

	public int getTotalTypeHF() {
		return totalTypeHF;
	}

	public void setTotalTypeHF(int totalTypeHF) {
		this.totalTypeHF = totalTypeHF;
	}


	public int getTotalAllNbr1() {
		return totalAllNbr1;
	}

	public void setTotalAllNbr1(int totalAllNbr1) {
		this.totalAllNbr1 = totalAllNbr1;
	}

	public int getTotalAllNbr2() {
		return totalAllNbr2;
	}

	public void setTotalAllNbr2(int totalAllNbr2) {
		this.totalAllNbr2 = totalAllNbr2;
	}
}
