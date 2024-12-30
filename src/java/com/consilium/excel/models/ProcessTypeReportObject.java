/****************************************************************************
 *
 * Copyright (c) 2014 ESound Tech. All Rights Reserved.
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
 *     File name:       ProcessTypeReportObject.java
 *
 *     History:
 *     Date               Author            Comments
 *     -----------------------------------------------------------------------
 *     DEC 11, 2014       mars           Initial Release
 *****************************************************************************/
package com.consilium.excel.models;

/**
  * <code>ProcessTypeReportObject</code>
  * 報表資料物件
  **/
public class ProcessTypeReportObject
{
	/**
	 * 服務類別主類ID
	 **/
	private String typeId = "";

	/**
	 * 服務類別主類名稱
	 **/
	private String typeName = "";

	/**
	 * 服務類別次類ID
	 **/
	private String itemId = "";

	/**
	 * 服務類別次類名稱
	 **/
	private String itemName = "";

	/**
	 * 直接回覆數量
	 **/
	private int valueA;

	/**
	 * 電話轉接數量
	 **/
	private int valueB;

	/**
	 * 三方會談數量
	 **/
	private int valueC;

	/**
	 * 案件派送數量
	 **/
	private int valueD;

	/**
	 * 設定服務類別主類ID
	 *
	 * @param typeId 服務類別主類ID
	 **/
	public void setTypeId(String typeId)
	{
	    if (typeId == null)
	    {
	        this.typeId = "";
	    }
	    else
	    {
	        this.typeId = typeId.trim();
	    }
	} //method setTypeId

	/**
	 * 取得服務類別主類ID
	 *
	 * @return typeId 服務類別主類ID
	 **/
	public String getTypeId()
	{
	    return this.typeId;
	} //method getTypeId

	/**
	 * 設定服務類別主類名稱
	 *
	 * @param typeName 服務類別主類名稱
	 **/
	public void setTypeName(String typeName)
	{
	    if (typeName == null)
	    {
	        this.typeName = "";
	    }
	    else
	    {
	        this.typeName = typeName.trim();
	    }
	} //method setTypeName

	/**
	 * 取得服務類別主類名稱
	 *
	 * @return typeName 服務類別主類名稱
	 **/
	public String getTypeName()
	{
	    return this.typeName;
	} //method getTypeName

	/**
	 * 設定服務類別次類ID
	 *
	 * @param itemId 服務類別次類ID
	 **/
	public void setItemId(String itemId)
	{
	    if (itemId == null)
	    {
	        this.itemId = "";
	    }
	    else
	    {
	        this.itemId = itemId.trim();
	    }
	} //method setItemId

	/**
	 * 取得服務類別次類ID
	 *
	 * @return itemId 服務類別次類ID
	 **/
	public String getItemId()
	{
	    return this.itemId;
	} //method getItemId

	/**
	 * 設定服務類別次類名稱
	 *
	 * @param itemName 服務類別次類名稱
	 **/
	public void setItemName(String itemName)
	{
	    if (itemName == null)
	    {
	        this.itemName = "";
	    }
	    else
	    {
	        this.itemName = itemName.trim();
	    }
	} //method setItemName

	/**
	 * 取得服務類別次類名稱
	 *
	 * @return itemName 服務類別次類名稱
	 **/
	public String getItemName()
	{
	    return this.itemName;
	} //method getItemName

	/**
	 * 設定直接回覆數量
	 *
	 * @param valueA 直接回覆數量
	 **/
	public void setValueA(int valueA)
	{
	    this.valueA = valueA;
	} //method setValueA

	/**
	 * 取得直接回覆數量
	 *
	 * @return valueA 直接回覆數量
	 **/
	public int getValueA()
	{
	    return this.valueA;
	} //method getValueA

	/**
	 * 設定電話轉接數量
	 *
	 * @param valueB 電話轉接數量
	 **/
	public void setValueB(int valueB)
	{
	    this.valueB = valueB;
	} //method setValueB

	/**
	 * 取得電話轉接數量
	 *
	 * @return valueB 電話轉接數量
	 **/
	public int getValueB()
	{
	    return this.valueB;
	} //method getValueB

	/**
	 * 設定三方會談數量
	 *
	 * @param valueC 三方會談數量
	 **/
	public void setValueC(int valueC)
	{
	    this.valueC = valueC;
	} //method setValueC

	/**
	 * 取得三方會談數量
	 *
	 * @return valueC 三方會談數量
	 **/
	public int getValueC()
	{
	    return this.valueC;
	} //method getValueC

	/**
	 * 設定案件派送數量
	 *
	 * @param valueD 案件派送數量
	 **/
	public void setValueD(int valueD)
	{
	    this.valueD = valueD;
	} //method setValueD

	/**
	 * 取得案件派送數量
	 *
	 * @return valueD 案件派送數量
	 **/
	public int getValueD()
	{
	    return this.valueD;
	} //method getValueD

}
