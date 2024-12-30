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
 *     File name:       DynaHashValue.java
 *
 *     History:
 *     Date             Author              Comments
 *     -----------------------------------------------------------------------
 *     Sep 09, 2010     Eric                Initial Release
 *****************************************************************************/  

package com.consilium.excel.models;

import java.util.*;

/**
 * <code>DynaHashValue</code>為Excel Report動態欄位的Value Table Object
 **/
public class DynaHashValue 
{
    /**
     * 欄代碼列表
     **/
    protected ArrayList colCodeList = new ArrayList();
    
    /**
     * 列代碼列表
     **/
    protected ArrayList rowCodeList = new ArrayList();
    
    /**
     * 欄名稱列表
     **/
    protected ArrayList colNameList = new ArrayList();
    
    /**
     * 列名稱列表
     **/
    protected ArrayList rowNameList = new ArrayList();
    
    /**
     * 資料表
     **/
    protected Hashtable valTable = new Hashtable();


    
    public DynaHashValue()
    {
    }

    /**
     * 設定欄名稱列表
     *
     * @param colNameList 欄名稱列表
     **/
    public void setColNameList(ArrayList colNameList)
    {
        this.colNameList = colNameList;
    }
    
    /**
     * 取得欄名稱列表
     *
     * @return colNameList 欄名稱列表
     **/
    public ArrayList getColNameList()
    {
        return this.colNameList;
    }

    /**
     * 設定列名稱列表
     *
     * @param rowNameList 列名稱列表
     **/
    public void setRowNameList(ArrayList rowNameList)
    {
        this.rowNameList = rowNameList;
    }
    
    /**
     * 取得列名稱列表
     *
     * @return rowNameList 列名稱列表
     **/
    public ArrayList getRowNameList()
    {
        return this.rowNameList;
    }

    /**
     * 設定欄代碼列表
     *
     * @param colCodeList 欄代碼列表
     **/
    public void setColCodeList(ArrayList colCodeList)
    {
        this.colCodeList = colCodeList;
    }
    
    /**
     * 取得欄代碼列表
     *
     * @return colCodeList 欄代碼列表
     **/
    public ArrayList getColCodeList()
    {
        return this.colCodeList;
    }

    /**
     * 設定列代碼列表
     *
     * @param rowCodeList 列代碼列表
     **/
    public void setRowCodeList(ArrayList rowCodeList)
    {
        this.rowCodeList = rowCodeList;
    }
    
    /**
     * 取得列代碼列表
     *
     * @return rowCodeList 列代碼列表
     **/
    public ArrayList getRowCodeList()
    {
        return this.rowCodeList;
    }

    /**
     * 加入欄標題
     *
     * @param colCode 欄代碼
     * @param colName 欄名稱
     **/
    public void addColTitle(String colCode, String colName)
    {
        this.colCodeList.add(colCode);
        this.colNameList.add(colName);
        this.valTable.put("colName@@" + colCode, colName);
    }

    /**
     * 取得欄標題
     *
     * @param colCode 欄代碼
     **/
    public String getColName(String colCode)
    {
        String colName = (String) this.valTable.get("colName@@" + colCode);
        if(colName == null) colName = colCode;
        
        return colName;    
    }

    /**
     * 加入列標題
     *
     * @param rowCode 列代碼
     * @param rowName 列名稱
     **/
    public void addRowTitle(String rowCode, String rowName)
    {
        this.rowCodeList.add(rowCode);
        this.rowNameList.add(rowName);
        this.valTable.put("rowName@@" + rowCode, rowName);
    }

    /**
     * 取得列標題
     *
     * @param rowCode 列代碼
     **/
    public String getRowName(String rowCode)
    {
        String rowName = (String) this.valTable.get("rowName@@" + rowCode);
        if(rowName == null) rowName = rowCode;
        
        return rowName;    
    }

    /**
     * 放入統計物件
     *
     * @param rowName 列名稱
     * @param colName 欄名稱
     * @param val 統計物件
     * @return 已存在的統計物件
     **/
    public Object setValue(String rowName, String colName, Object val)
    {
        return this.valTable.put(rowName + "@" + colName, val);
    }

    /**
     * 取得統計物件
     *
     * @param rowName 列名稱
     * @param colName 欄名稱
     * @return 統計物件
     **/
    public Object getValue(String rowName, String colName)
    {
        return this.valTable.get(rowName + "@" + colName);
    }

    /**
     * 取得字串值
     *
     * @param rowName 列名稱
     * @param colName 欄名稱
     * @return 字串值
     **/
    public String getString(String rowName, String colName)
    {
        Object obj = this.valTable.get(rowName + "@" + colName);
        if(obj != null)
        {
            return obj.toString();
        }
        else
        {
            return "";
        }
    }

    /**
     * 累加長整數統計值
     *
     * @param rowName 列名稱
     * @param colName 欄名稱
     * @param val 長整數統計值
     **/
    public void addLong(String rowName, String colName, long val)
    {
        DynaLongVO longVo = null;
        try
        {
            longVo = (DynaLongVO) this.getValue(rowName, colName);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        if(longVo == null)
        {
            longVo = new DynaLongVO(val);
            this.setValue(rowName, colName, longVo);
        }
        else
        {
            longVo.addVal(val);
        }
    }

    /**
     * 取得長整數統計值
     *
     * @param rowName 列名稱
     * @param colName 欄名稱
     * @return 長整數統計值
     **/
    public long getLong(String rowName, String colName)
    {
        long tempVal = 0;
        
        DynaLongVO longVo = null;
        try
        {
            longVo = (DynaLongVO) this.getValue(rowName, colName);
            
            if(longVo != null)
            {
                tempVal = longVo.getVal();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return tempVal;
    }

    /**
     * 累加整數統計值
     *
     * @param rowName 列名稱
     * @param colName 欄名稱
     * @param val 整數統計值
     **/
    public void addInt(String rowName, String colName, int val)
    {
        DynaIntVO intVo = null;
        try
        {
            intVo = (DynaIntVO) this.getValue(rowName, colName);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        if(intVo == null)
        {
            intVo = new DynaIntVO(val);
            this.setValue(rowName, colName, intVo);
        }
        else
        {
            intVo.addVal(val);
        }
    }

    /**
     * 取得整數統計值
     *
     * @param rowName 列名稱
     * @param colName 欄名稱
     * @return 整數統計值
     **/
    public int getInt(String rowName, String colName)
    {
        int tempVal = 0;
        
        DynaIntVO intVo = null;
        try
        {
            intVo = (DynaIntVO) this.getValue(rowName, colName);
            
            if(intVo != null)
            {
                tempVal = intVo.getVal();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return tempVal;
    }
    
} //class DynaHashValue