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
 *     File name:       DynaStringVO.java
 *
 *     History:
 *     Date             Author              Comments
 *     -----------------------------------------------------------------------
 *     Sep 08, 2010     Eric                Initial Release
 *****************************************************************************/  

package com.consilium.excel.models;

/**
 * <code>DynaStringVO</code>為Excel Report字串欄位值的Value Object
 **/
public class DynaStringVO 
{
    /**
     * 字串值
     **/
    protected String val = "";
    
    public DynaStringVO()
    {
        
    }
    
    public DynaStringVO(String val)
    {
        this.setVal(val);
    }

    /**
     * 設定字串值
     *
     * @param val 字串值
     **/
    public void setVal(String val)
    {
        if(val == null)
        {
            this.val = "";
        }
        else
        {
            this.val = val.trim();
        }
    }
    
    /**
     * 取得字串值
     *
     * @return val 字串值
     **/
    public String getVal()
    {
        return this.val;
    }
    
    /**
     * 代表此物件的字串值
     *
     * @return 字串值
     **/
    public String toString()
    {
        return this.val;
    }

} //class DynaStringVO