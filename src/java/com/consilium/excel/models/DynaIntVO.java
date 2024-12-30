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
 *     File name:       DynaIntVO.java
 *
 *     History:
 *     Date             Author              Comments
 *     -----------------------------------------------------------------------
 *     Sep 08, 2010     Eric                Initial Release
 *****************************************************************************/  

package com.consilium.excel.models;

/**
 * <code>DynaIntVO</code>為Excel Report整數欄位值的Value Object
 **/
public class DynaIntVO 
{
    /**
     * 數值
     **/
    protected int val = 0;
    
    public DynaIntVO()
    {
        
    }
    
    public DynaIntVO(int val)
    {
        this.setVal(val);
    }

    /**
     * 設定數值
     *
     * @param val 數值
     **/
    public void setVal(int val)
    {
        this.val = val;
    }
    
    /**
     * 取得數值
     *
     * @return val 數值
     **/
    public int getVal()
    {
        return this.val;
    }

    /**
     * 累加數值
     *
     * @param val 數值
     **/
    public void addVal(int val)
    {
        this.val += val;
    }
    
    /**
     * 代表此物件的字串值
     *
     * @return 字串值
     **/
    public String toString()
    {
        return "" + this.val;
    }

} //class DynaIntVO