/****************************************************************************
 *
 * Copyright (c) 2017 ESound Tech. All Rights Reserved.
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
 *     File name:       WinDataObject.java
 *
 *     History:
 *     Date               Author            Comments
 *     -----------------------------------------------------------------------
 *     JAN 3, 2017       mars           Initial Release
 *****************************************************************************/
package com.consilium.excel.models.window;


/**
  * <code>WinDataObject</code>
  * 窗口資料
  **/
public class WinDataObject
{
	/**
	 * 職稱1
	 **/
	private String occupation1 = "";

	/**
	 * 姓名1
	 **/
	private String name1 = "";

	/**
	 * 電話1
	 **/
	private String tel1 = "";

	/**
	 * 分機1
	 **/
	private String ext1 = "";

	/**
	 * 電子郵件1
	 **/
	private String email1 = "";

	/**
	 * 職稱2
	 **/
	private String occupation2 = "";

	/**
	 * 姓名2
	 **/
	private String name2 = "";

	/**
	 * 電話2
	 **/
	private String tel2 = "";

	/**
	 * 分機2
	 **/
	private String ext2 = "";

	/**
	 * 電子郵件2
	 **/
	private String email2 = "";

	/**
	 * 單位代碼
	 **/
	private String unitCode = "";

	/**
	 * 單位名稱
	 **/
	private String unitName = "";
	

	/**
	 * 子單位代碼
	 **/
	private String subUnitCode = "";

	/**
	 * 子單位名稱
	 **/
	private String subUnitName = "";

	/**
	 * 設定職稱1
	 *
	 * @param occupation1 職稱1
	 **/
	public void setOccupation1(String occupation1)
	{
	    if (occupation1 == null)
	    {
	        this.occupation1 = "";
	    }
	    else
	    {
	        this.occupation1 = occupation1.trim();
	    }
	} //method setOccupation1

	/**
	 * 取得職稱1
	 *
	 * @return occupation1 職稱1
	 **/
	public String getOccupation1()
	{
	    return this.occupation1;
	} //method getOccupation1

	/**
	 * 設定姓名1
	 *
	 * @param name1 姓名1
	 **/
	public void setName1(String name1)
	{
	    if (name1 == null)
	    {
	        this.name1 = "";
	    }
	    else
	    {
	        this.name1 = name1.trim();
	    }
	} //method setName1

	/**
	 * 取得姓名1
	 *
	 * @return name1 姓名1
	 **/
	public String getName1()
	{
	    return this.name1;
	} //method getName1

	/**
	 * 設定電話1
	 *
	 * @param tel1 電話1
	 **/
	public void setTel1(String tel1)
	{
	    if (tel1 == null)
	    {
	        this.tel1 = "";
	    }
	    else
	    {
	        this.tel1 = tel1.trim();
	    }
	} //method setTel1

	/**
	 * 取得電話1
	 *
	 * @return tel1 電話1
	 **/
	public String getTel1()
	{
	    return this.tel1;
	} //method getTel1

	/**
	 * 設定分機1
	 *
	 * @param ext1 分機1
	 **/
	public void setExt1(String ext1)
	{
	    if (ext1 == null)
	    {
	        this.ext1 = "";
	    }
	    else
	    {
	        this.ext1 = ext1.trim();
	    }
	} //method setExt1

	/**
	 * 取得分機1
	 *
	 * @return ext1 分機1
	 **/
	public String getExt1()
	{
	    return this.ext1;
	} //method getExt1

	/**
	 * 設定電子郵件1
	 *
	 * @param email1 電子郵件1
	 **/
	public void setEmail1(String email1)
	{
	    if (email1 == null)
	    {
	        this.email1 = "";
	    }
	    else
	    {
	        this.email1 = email1.trim();
	    }
	} //method setEmail1

	/**
	 * 取得電子郵件1
	 *
	 * @return email1 電子郵件1
	 **/
	public String getEmail1()
	{
	    return this.email1;
	} //method getEmail1

	/**
	 * 設定職稱2
	 *
	 * @param occupation2 職稱2
	 **/
	public void setOccupation2(String occupation2)
	{
	    if (occupation2 == null)
	    {
	        this.occupation2 = "";
	    }
	    else
	    {
	        this.occupation2 = occupation2.trim();
	    }
	} //method setOccupation2

	/**
	 * 取得職稱2
	 *
	 * @return occupation2 職稱2
	 **/
	public String getOccupation2()
	{
	    return this.occupation2;
	} //method getOccupation2

	/**
	 * 設定姓名2
	 *
	 * @param name2 姓名2
	 **/
	public void setName2(String name2)
	{
	    if (name2 == null)
	    {
	        this.name2 = "";
	    }
	    else
	    {
	        this.name2 = name2.trim();
	    }
	} //method setName2

	/**
	 * 取得姓名2
	 *
	 * @return name2 姓名2
	 **/
	public String getName2()
	{
	    return this.name2;
	} //method getName2

	/**
	 * 設定電話2
	 *
	 * @param tel2 電話2
	 **/
	public void setTel2(String tel2)
	{
	    if (tel2 == null)
	    {
	        this.tel2 = "";
	    }
	    else
	    {
	        this.tel2 = tel2.trim();
	    }
	} //method setTel2

	/**
	 * 取得電話2
	 *
	 * @return tel2 電話2
	 **/
	public String getTel2()
	{
	    return this.tel2;
	} //method getTel2

	/**
	 * 設定分機2
	 *
	 * @param ext2 分機2
	 **/
	public void setExt2(String ext2)
	{
	    if (ext2 == null)
	    {
	        this.ext2 = "";
	    }
	    else
	    {
	        this.ext2 = ext2.trim();
	    }
	} //method setExt2

	/**
	 * 取得分機2
	 *
	 * @return ext2 分機2
	 **/
	public String getExt2()
	{
	    return this.ext2;
	} //method getExt2

	/**
	 * 設定電子郵件2
	 *
	 * @param email2 電子郵件2
	 **/
	public void setEmail2(String email2)
	{
	    if (email2 == null)
	    {
	        this.email2 = "";
	    }
	    else
	    {
	        this.email2 = email2.trim();
	    }
	} //method setEmail2

	/**
	 * 取得電子郵件2
	 *
	 * @return email2 電子郵件2
	 **/
	public String getEmail2()
	{
	    return this.email2;
	} //method getEmail2

	/**
	 * 設定單位代碼
	 *
	 * @param unitCode 單位代碼
	 **/
	public void setUnitCode(String unitCode)
	{
	    if (unitCode == null)
	    {
	        this.unitCode = "";
	    }
	    else
	    {
	        this.unitCode = unitCode.trim();
	    }
	} //method setUnitCode

	/**
	 * 取得單位代碼
	 *
	 * @return unitCode 單位代碼
	 **/
	public String getUnitCode()
	{
	    return this.unitCode;
	} //method getUnitCode

	/**
	 * 設定單位名稱
	 *
	 * @param unitName 單位名稱
	 **/
	public void setUnitName(String unitName)
	{
	    if (unitName == null)
	    {
	        this.unitName = "";
	    }
	    else
	    {
	        this.unitName = unitName.trim();
	    }
	} //method setUnitName

	/**
	 * 取得單位名稱
	 *
	 * @return unitName 單位名稱
	 **/
	public String getUnitName()
	{
	    return this.unitName;
	} //method getUnitName
	
	/**
	 * 設定子單位代碼
	 *
	 * @param subUnitCode 子單位代碼
	 **/
	public void setSubUnitCode(String subUnitCode)
	{
	    if (subUnitCode == null)
	    {
	        this.subUnitCode = "";
	    }
	    else
	    {
	        this.subUnitCode = subUnitCode.trim();
	    }
	} //method setSubUnitCode

	/**
	 * 取得子單位代碼
	 *
	 * @return subUnitCode 子單位代碼
	 **/
	public String getSubUnitCode()
	{
	    return this.subUnitCode;
	} //method getSubUnitCode

	/**
	 * 設定子單位名稱
	 *
	 * @param subUnitName 子單位名稱
	 **/
	public void setSubUnitName(String subUnitName)
	{
	    if (subUnitName == null)
	    {
	        this.subUnitName = "";
	    }
	    else
	    {
	        this.subUnitName = subUnitName.trim();
	    }
	} //method setSubUnitName

	/**
	 * 取得子單位名稱
	 *
	 * @return subUnitName 子單位名稱
	 **/
	public String getSubUnitName()
	{
	    return this.subUnitName;
	} //method getSubUnitName


}
