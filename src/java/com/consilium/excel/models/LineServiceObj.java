/****************************************************************************
*
* Copyright (c) 2018 ESound Tech. All Rights Reserved.
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
*     File name:       LineServiceExcelImpl.java
*
*     History:
*     Date               Author                  Comments
*     -----------------------------------------------------------------------
*     Nov 06, 2018       Jack                    Initial Release
*****************************************************************************/
package com.consilium.excel.models;

public class LineServiceObj 
{
	private String keyWord = "";
	
	private long count = 0;

	public String getKeyWord() 
	{
		return keyWord;
	}

	public void setKeyWord(String keyWord) 
	{
		if(keyWord != null)
			this.keyWord = keyWord;
		else
			this.keyWord = "";
	}

	public long getCount() 
	{
		return count;
	}

	public void setCount(long count) 
	{
		this.count = count;
	}
}
