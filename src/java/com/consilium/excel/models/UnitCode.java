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
 *     File name:       UnitCode.java
 *
 *     History:
 *     Date                        Comments
 *     -----------------------------------------------------------------------
 *     Feb 22, 2010     Initial Release
 *     Dec 24, 2010     加入五都相容機制
 *     Jan 14, 2011     加入內政部單位     MARS
 *********************************************/

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;


/**
 * UnitCode是一個單位代碼的Singleton物件,提供作為取得單位資訊之用.
 *
 * @author Eric Chuang
 * @version 1.0
 **/
public class UnitCode {
    /**
     * <code>UnitCode</code>物件的Singleton instance.
     **/
    private static UnitCode instance = null;

    /**
     * 代碼表是否己載入.
     **/
    private boolean tableFlag = false;

    /**
     * 存放所有代表單位的<code>UnitCodeObject</code>物件的table,其中key是由單位類別
     * 與單位代碼所組成的.
     **/
    private Hashtable codeTable;

    /**
     * 存放所有代表單位的<code>UnitCodeObject</code>物件的table,不過與
     * <code>codeTable</code>不同的是key為單位類別,而value則是由類別的所有單位物件
     * 所組成的<code>Vector</code>.
     **/
    private Hashtable codeListTable;

    private ArrayList allMtypeList;

    /**
     * 存放警察單位第二級(分局)資料，以citycode為key，傳回該縣市的單位ArrayList
     */
    private Hashtable policeLevel2Table;

    /**
     * 存放性侵害加害人系統管理單位，以citycode為key，傳回該縣市的管理單位
     */
    private Hashtable dvpcPoliceTable;

    /**
     * 存放內政部各單位下級單位資料，以上級單位UNITCODE為key，傳回該單位的下級單位的ArrayList;
     */
    private Hashtable moiSubUnitTable;

    /**
     * 存放內政部各單位下級單位資料，以SPUNITCODE為分類
     */
    private Hashtable moiSpUnits;


    /**
     * 存放性侵害加害人系統管理單位的串列
     */
    private ArrayList dvpcPoliceList;

    /**
     * 存放內政部下級單位的UnitCodeObject
     */
    private ArrayList moiSubUnitList = new ArrayList();

    /**
     * 存放內政部上級單位的UnitCodeObject
     */
    private ArrayList moiSuperUnitList = new ArrayList();

    /**
     * UnitCode的建構式,此建構式只允許<code>getInstance</code> method
     * 來建立<code>UnitCode</code>物件.
     *
     * @see #getInstance()
     **/
    private UnitCode() {
    } //constructor UnitCode

    /**
     * 取得<code>UnitCode</code>的instance.
     *
     * @return a singleton UnitCode instance.
     **/
    public static synchronized UnitCode getInstance() {
        if (instance == null) {
            instance = new UnitCode();

            if (!instance.loadTable()) {
                instance.codeTable = new Hashtable();
                instance.codeListTable = new Hashtable();
            } else {
                instance.setMoiUnits();
            }


        } else if (!instance.tableFlag) {
            instance.loadTable();
            instance.setMoiUnits();
        }

        return instance;
    } //method getInstance

    /**
     * 取得所指定單位別的單位列表.
     *
     * @param unitType 單位類別.
     * @return 由<code>UnitCodeObject</code>物件所組成的<code>Vector</code>,
     * 如果所指定的單位類別不存在則會回傳一個空的<code>Vector</code>.
     **/
    public Vector getUnitList(String unitType) {
        Vector codeList = null;

        if (unitType != null)
            codeList = (Vector) codeListTable.get(unitType);

        if (codeList == null) {
            codeList = new Vector();
        }

        return codeList;
    } //method getUnitList

    /**
     * 取得代表所指定單位別及單位代碼的<code>UnitCodeObject</code>物件.
     *
     * @param unitType 單位類別.
     * @param unitCode 單位代碼.
     * @return 代表所指定單位別及單位代碼的<code>UnitCodeObject</code>物件.
     **/
    public UnitCodeObject getUnitCodeObject(String unitType, String unitCode) {
        UnitCodeObject retValue = null;

        //五都相容機制
        if (unitCode != null && unitCode.length() == 10) {
            unitCode = unitCode.replaceAll("6500000000", "1000100000").replaceAll("6600000000", "1001900000").replaceAll("6700000000", "1002100000");
        }

        if (unitType != null && unitCode != null)
            retValue = (UnitCodeObject) this.codeTable.get(unitType + "_" + unitCode);

        if (retValue == null) {
            retValue = new UnitCodeObject();
            retValue.setUnitType(unitType);
            retValue.setUnitCode(unitCode);
        }

        return retValue;
    } //method getUnitCodeObject

    /**
     * 取得所指定單位別及單位代碼的單位名稱.
     *
     * @param unitType 單位類別.
     * @param unitCode 單位代碼.
     * @return 單位名稱.
     **/
    public String getUnitName(String unitType, String unitCode) {
        UnitCodeObject codeObj = null;

        String retValue = null;

        //五都相容機制
        if (unitCode != null && unitCode.length() == 10) {
            unitCode = unitCode.replaceAll("6500000000", "1000100000").replaceAll("6600000000", "1001900000").replaceAll("6700000000", "1002100000");
        }

        if (unitType != null && unitCode != null) {
            codeObj = (UnitCodeObject) this.codeTable.get(unitType + "_" + unitCode);
            if (codeObj != null) {
                retValue = codeObj.getUnitName();
            }
        }

        if (retValue == null) {
            retValue = "";
        }

        return retValue;
    } //method getUnitName

    /**
     * 當資料庫有異時,系統可透過<code>refrsh</code> method來更新單位代碼表內
     * 所有的單位物件.
     **/
    public boolean refresh() {
        boolean flag;
        flag = this.loadTable();
        this.setMoiUnits();
        return flag;
    } //method refresh


    /**
     * 取得內政部所有上級單位的ArrayList
     **/
    // Add by mars 2011/01/14
    public ArrayList getMoiSuperUnits() {
        return this.moiSuperUnitList;
    }

    /**
     * 取得內政部某上級單位的所有下級單位
     **/
    // Add by mars 2011/01/14
    public ArrayList getMoiSubUnits(String unitCode) {
        ArrayList subList = new ArrayList();
        if (this.moiSubUnitTable == null) this.setMoiUnits();
        if (unitCode != null) {
            subList = (ArrayList) this.moiSubUnitTable.get(unitCode);
        }

        return subList;

    }

    /**
     * 取得內政部某上級單位的所有下級單位(依SPUNITCODE分類)
     **/
    // Add by mars 2011/01/14
    public ArrayList getMoiSubUnitsBySpUnitCode(String unitCode, String spUnitCode) {
        String key = "";
        ArrayList list;
        if (unitCode == null || spUnitCode == null) {
            key = "";
        } else {
            key = unitCode + "-" + spUnitCode;
        }

        list = (ArrayList) moiSpUnits.get(key);
        if (list == null) {
            list = new ArrayList();

        }
        return list;

    }

    /**
     * 從資料庫內載入單位代碼資料.
     **/
    private synchronized boolean loadTable() {
        boolean isSuccessful = false;

        UnitCodeObject codeObj;

        policeLevel2Table = new Hashtable();
        dvpcPoliceTable = new Hashtable();
        dvpcPoliceList = new ArrayList();

        Hashtable tmpCodeTable = new Hashtable();
        Hashtable tmpListTable = new Hashtable();
        ArrayList tmpAllMtypeList = new ArrayList();

        ArrayList tmpMoiSuperUnitList = new ArrayList();
        ArrayList tmpMoiSubUnitList = new ArrayList();

        Vector tmpVector;

        ArrayList policeLevel2List = null;

        Connection conn = null;

        try {
            DataSource dvpcDS = ServiceLocator.getInstance().getDataSource();

            conn = dvpcDS.getConnection();
            Statement stmt = conn.createStatement();
            //ResultSet rs = stmt.executeQuery("SELECT * FROM UNITS ORDER BY CITYCODE, UNITTYPE, UNITCODE");
            ResultSet rs = stmt.executeQuery("SELECT * FROM UNITS T ORDER BY UNITTYPE, POLICEOFFICELEVEL, SUPERUNIT, ORDERNUMBER, UNITCODE");

            while (rs.next()) {
                codeObj = new UnitCodeObject();
                codeObj.setSuperUnit(rs.getString("SUPERUNIT"));
                codeObj.setUnitType(rs.getString("UNITTYPE"));
                codeObj.setUnitCode(rs.getString("UNITCODE"));
                codeObj.setCityCode(rs.getString("CITYCODE"));
                codeObj.setUnitName(rs.getString("UNITNAME"));
                codeObj.setUnitContact(rs.getString("UNITCONTACT"));
                codeObj.setUnitAddress(rs.getString("UNITADDRESS"));
                codeObj.setUnitTel(rs.getString("UNITTEL"));
                codeObj.setUnitFax(rs.getString("UNITFAX"));
                codeObj.setPoliceOfficeLevel(rs.getInt("POLICEOFFICELEVEL"));
                codeObj.setIsDvpcPoliceSuperUnit(rs.getString("ISDVPCPOLICESUPERUNIT"));
                codeObj.setSName(rs.getString("SNAME"));
                codeObj.setOrderNumber(rs.getInt("ORDERNUMBER"));
                codeObj.setSpUnitCode(rs.getString("SPUNITCODE"));

                tmpCodeTable.put(codeObj.getUnitType() + "_" + codeObj.getUnitCode(), codeObj);

                tmpVector = (Vector) tmpListTable.get(codeObj.getUnitType());
                if (tmpVector == null) {
                    tmpVector = new Vector();
                    tmpListTable.put(codeObj.getUnitType(), tmpVector);
                }

                tmpVector.add(codeObj);

                // Add by Jimmy 2006/01/16
                if (codeObj.getUnitType().equals("D") && codeObj.getPoliceOfficeLevel() == 2) {
                    policeLevel2List = (ArrayList) policeLevel2Table.get(codeObj.getCityCode());
                    if (policeLevel2List == null) {
                        policeLevel2List = new ArrayList();
                        policeLevel2Table.put(codeObj.getCityCode(), policeLevel2List);
                    }

                    policeLevel2List.add(codeObj);
                }

                // Add by Jimmy 2006/01/25
                if (codeObj.getIsDvpcPoliceSuperUnit().equals("Y")) {
                    dvpcPoliceTable.put(codeObj.getCityCode(), codeObj);
                    dvpcPoliceList.add(codeObj);
                }

                // Add by mars 2011/01/14
                if (codeObj.getSuperUnit() == null &&  "M".equals(codeObj.getUnitType()) && codeObj.getPoliceOfficeLevel() == 1) {
                    tmpMoiSuperUnitList.add(codeObj);
                    tmpAllMtypeList.add(codeObj);
                }

                // Add by mars 2011/01/14
                if ("M".equals(codeObj.getUnitType()) && codeObj.getPoliceOfficeLevel() == 2) {
                    tmpMoiSubUnitList.add(codeObj);
                    tmpAllMtypeList.add(codeObj);
                }


            } //end of while

            isSuccessful = true;

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        if (isSuccessful) {
            this.codeTable = tmpCodeTable;
            this.codeListTable = tmpListTable;
            this.allMtypeList = tmpAllMtypeList;
            this.moiSuperUnitList = tmpMoiSuperUnitList;
            this.moiSubUnitList = tmpMoiSubUnitList;
            this.tableFlag = true;
        }

        return isSuccessful;

    } //method loadTable


    // Add by mars 2011/01/14
    //安排內政部單位的上下級關係
    private synchronized void setMoiUnits() {
        Hashtable tmpMoiSubUnits = new Hashtable();
        UnitCodeObject uObj;
        ArrayList tmpList;
        for (int i = 0; i < moiSuperUnitList.size(); i++) {
            uObj = (UnitCodeObject) moiSuperUnitList.get(i);
            ArrayList subList = new ArrayList();
            tmpMoiSubUnits.put(uObj.getUnitCode(), subList);

        }

        for (int i = 0; i < moiSubUnitList.size(); i++) {
            uObj = (UnitCodeObject) moiSubUnitList.get(i);
            if (tmpMoiSubUnits.get(uObj.getSuperUnit()) != null) {
                tmpList = (ArrayList) tmpMoiSubUnits.get(uObj.getSuperUnit());
                tmpList.add(uObj);
            }
        }

        this.moiSubUnitTable = tmpMoiSubUnits;

        Hashtable tmpMoiSpUnits = new Hashtable();
        String unitCode = "";
        String spUnitCode = "";
        ArrayList spList;

//          for(int i=0;i<moiSuperUnitList.size();i++)
//          {
//              uObj=(UnitCodeObject)moiSuperUnitList.get(i);
//              unitCode=uObj.getUnitCode();
//              spUnitCode=uObj.getSpUnitCode();
//              if(tmpMoiSpUnits.get(unitCode+"-"+spUnitCode)==null)
//              {
//                  spList=new ArrayList();
//                  spList.add(uObj);
//                  tmpMoiSpUnits.put(unitCode+"-"+spUnitCode,spList);
//              }
//              else
//              {
//                  spList=(ArrayList)tmpMoiSpUnits.get(unitCode+"-"+spUnitCode);
//                  spList.add(uObj);
//              }
//
//          }

        for (int i = 0; i < allMtypeList.size(); i++) {
            //uObj=(UnitCodeObject)moiSubUnitList.get(i);
            uObj = (UnitCodeObject) allMtypeList.get(i);
            if (uObj.getPoliceOfficeLevel() == 1) {
                unitCode = uObj.getUnitCode();
            } else if (uObj.getPoliceOfficeLevel() == 2) {
                unitCode = uObj.getSuperUnit();
            }

            spUnitCode = uObj.getSpUnitCode();
            if (tmpMoiSpUnits.get(unitCode + "-" + spUnitCode) == null) {
                spList = new ArrayList();
                spList.add(uObj);
                tmpMoiSpUnits.put(unitCode + "-" + spUnitCode, spList);
            } else {
                spList = (ArrayList) tmpMoiSpUnits.get(unitCode + "-" + spUnitCode);
                spList.add(uObj);
            }
        }

        moiSpUnits = tmpMoiSpUnits;
    }

    // Add by Jimmy 2006/01/16

    /**
     * 取得所指定縣市的警察單位第二層物件串列.
     *
     * @param cityCode 縣市代碼.
     * @return 警察單位第二層物件串列.
     **/
    public ArrayList getPoliceLevel2(String cityCode) {
        return (ArrayList) this.policeLevel2Table.get(cityCode);
    }

    // Add by Jimmy 2006/01/25

    /**
     * 取得性侵害加害人系統管理單位物件串列.
     *
     * @return 性侵害加害人系統管理單位物件串列.
     **/
    public ArrayList getDvpcPoliceSuperUnitList() {
        return this.dvpcPoliceList;
    }

    // Add by Jimmy 2006/01/25

    /**
     * 取得性侵害加害人系統管理單位物件.
     *
     * @param cityCode 縣市代碼.
     * @return 性侵害加害人系統管理單位物件.
     **/
    public UnitCodeObject getDvpcPoliceSuperUnit(String cityCode) {
        return (UnitCodeObject) this.dvpcPoliceTable.get(cityCode);
    }
} //class UnitCode