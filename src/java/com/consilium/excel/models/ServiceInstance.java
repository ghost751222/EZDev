package com.consilium.excel.models;
import com.consilium.excel.models.ServiceLocator;
import com.consilium.excel.models.ServiceTypeObject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * ServiceInstance是一個服務類別的Singleton物件,提供作為取得服務類別資訊之用.
 **/
public class ServiceInstance {
    /**
     * <code>ServiceInstance</code>物件的Singleton instance.
     **/
    private static ServiceInstance instance = null;

    /**
     * 服務類別表是否己載入.
     **/
    private boolean tableFlag = false;

    /**
     * 存放所有的主類別的<code>ServiceTypeObject</code>物件的HashMap,
     * 其key為主類別序號,而value則為主類別的<code>ServiceTypeObject</code>物件.
     **/
    private HashMap<String, ServiceTypeObject> typeMap;

    /**
     * 存放所有的主類別的<code>ServiceTypeObject</code>物件的ArrayList,
     * value是由主類別的<code>ServiceTypeObject</code>物件所組成的<code>ArrayList</code>.
     **/
    private ArrayList<ServiceTypeObject> typeList;

    /**
     * 存放所有的次類別名稱的HashMap,
     * 其key為次類別序號,而value則為次類別名稱.
     **/
    private HashMap<String, String> itemNameMap;

    /**
     * ServiceInstance的建構式,此建構式只允許<code>getInstance</code> method
     * 來建立<code>ServiceInstance</code>物件.
     *
     * @see #getInstance()
     **/
    private ServiceInstance() {
    } //constructor ServiceInstance

    /**
     * 取得<code>ServiceInstance</code>的instance.
     *
     * @return a singleton ServiceInstance instance.
     **/
    public static synchronized ServiceInstance getInstance() {
        if (instance == null) {
            instance = new ServiceInstance();

            if (!instance.loadTable()) {
                instance.typeMap = new HashMap<String, ServiceTypeObject>();
                instance.typeList = new ArrayList<ServiceTypeObject>();
                instance.itemNameMap = new HashMap<String, String>();
            }
        } else if (!instance.tableFlag) {
            instance.loadTable();
        }

        return instance;
    } //method getInstance

    /**
     * 取得所有主類別的列表.
     *
     * @return 由<code>ServiceTypeObject</code>物件所組成的<code>ArrayList</code>,
     * 如果所指定的服務類別總類不存在則會回傳一個空的<code>ArrayList</code>.
     **/
    public ArrayList<ServiceTypeObject> getTypeList() {
        if (this.typeList == null) {
            this.typeList = new ArrayList<ServiceTypeObject>();
        }
        return this.typeList;
    } //method getTypeList

    /**
     * 取得啟用中主類別的列表.
     *
     * @return 由<code>ServiceTypeObject</code>物件所組成的<code>ArrayList</code>,
     * 如果所指定的服務類別總類不存在則會回傳一個空的<code>ArrayList</code>.
     **/
    public ArrayList<ServiceTypeObject> getTypeEnableList() {
        ArrayList<ServiceTypeObject> tmpTypeList = new ArrayList<ServiceTypeObject>();
        if (this.typeList != null) {
            for (ServiceTypeObject typeObj : this.typeList) {
                if (typeObj.getState().equals("1"))
                    tmpTypeList.add(typeObj);
            }
        }
        return tmpTypeList;
    } //method getTypeEnableList

    /**
     * 取得主類別的<code>ServiceTypeObject</code>物件.
     *
     * @param typeId 主類別代碼.
     * @return <code>ServiceTypeObject</code>物件.
     **/
    public ServiceTypeObject getTypeObject(String typeId) {
        ServiceTypeObject retObj = null;
        if (typeId != null && this.typeMap != null) {
            retObj = this.typeMap.get(typeId);
        }
        return retObj;
    } //method getTypeObject

    /**
     * 取得主類別名稱.
     *
     * @param typeId 主類別代碼.
     * @return 主類別名稱.
     **/
    public String getTypeName(String typeId) {
        String retValue = null;
        if (typeId != null && this.typeMap != null) {
            ServiceTypeObject typeObj = this.typeMap.get(typeId);
            if (typeObj != null) {
                retValue = typeObj.getTypeName();
            }
        }
        if (retValue == null) {
            retValue = "";
        }
        return retValue;
    } //method getTypeName

    /**
     * 取得次類別名稱.
     *
     * @param itemId 次類別代碼.
     * @return 次類別名稱.
     **/
    public String getItemName(String itemId) {
        String retValue = null;
        if (itemId != null && this.itemNameMap != null) {
            retValue = this.itemNameMap.get(itemId);
        }
        if (retValue == null) {
            retValue = "";
        }
        return retValue;
    } //method getItemName

    /**
     * 設定次類別名稱.
     *
     * @param itemId   次類別代碼.
     * @param itemName 次類別名稱.
     **/
    public void setItemName(String itemId, String itemName) {
        if (itemId != null && itemName != null && this.itemNameMap != null) {
            this.itemNameMap.put(itemId, itemName);
        }
    } //method setItemName

    /**
     * 當資料庫有異時,系統可透過<code>refrsh</code> method來更新服務類別表內
     * 所有的服務類別物件.
     **/
    public boolean refresh() {
        return this.loadTable();
    } //method refresh

    /**
     * 從資料庫內載入服務類別資料.
     **/
    private synchronized boolean loadTable() {
        boolean isSuccessful = false;

        HashMap<String, ServiceTypeObject> tmpTypeMap = new HashMap<String, ServiceTypeObject>();
        ArrayList<ServiceTypeObject> tmpTypeList = new ArrayList<ServiceTypeObject>();
        HashMap<String, String> tmpItemNameMap = new HashMap<String, String>();

        Connection conn = null;

        try {
            DataSource dvpcDS = (DataSource) ServiceLocator.getInstance().getDataSource();

            conn = dvpcDS.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(" SELECT TYPEID FROM SERVICE_TYPE where typeId not in (Select codeCode from APP_CODE where category='R09') ORDER BY SHOWINDEX ASC ");
            while (rs.next()) {
                ServiceTypeObject typeObj = new ServiceTypeObject(rs.getString("TYPEID"));
                tmpTypeMap.put(typeObj.getTypeId(), typeObj);
                tmpTypeList.add(typeObj);
            } //end of while

            rs = stmt.executeQuery(" SELECT ITEMID,ITEMNAME FROM SERVICE_ITEM where itemId not in (Select codeCode from app_code where category='R10')");
            while (rs.next()) {
                tmpItemNameMap.put(rs.getString("ITEMID"), rs.getString("ITEMNAME"));
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
            this.typeMap = tmpTypeMap;
            this.typeList = tmpTypeList;
            this.itemNameMap = tmpItemNameMap;
            this.tableFlag = true;
        }

        return isSuccessful;

    } //method loadTable


}//class ServiceInstance

