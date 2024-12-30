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
 *     File name:       ServiceItemObject.java
 *
 *     History:
 *     Date                           Comments
 *     -----------------------------------------------------------------------
 *     DEC 9, 2014                    Initial Release
 *****************************************************************************/
package com.consilium.excel.models;


import javax.sql.DataSource;
import java.sql.*;

/**
 * <code>ServiceItemObject</code>
 * 次類別物件
 **/
public class ServiceItemObject {
    /**
     * 次類別代碼
     **/
    private String itemId = "";

    /**
     * 主類別代碼
     **/
    private String typeId = "";

    /**
     * 次類別名稱
     **/
    private String itemName = "";

    /**
     * 排序
     **/
    private String showIndex = "";

    /**
     * 狀態
     **/
    private String state = "";

    /**
     * 資料的第一次建立時間
     **/
    private String createTime = "";

    /**
     * 資料建立人員代碼
     **/
    private String creatorId = "";

    /**
     * 最後修改人員代碼
     **/
    private String lastModifier = "";

    /**
     * 最後修改時間
     **/
    private String lastUpdateTime = "";

    /**
     * 設定次類別代碼
     *
     * @param itemId 次類別代碼
     **/
    public void setItemId(String itemId) {
        if (itemId == null) {
            this.itemId = "";
        } else {
            this.itemId = itemId.trim();
        }
    } //method setItemId

    /**
     * 取得次類別代碼
     *
     * @return itemId 次類別代碼
     **/
    public String getItemId() {
        return this.itemId;
    } //method getItemId

    /**
     * 設定主類別代碼
     *
     * @param typeId 主類別代碼
     **/
    public void setTypeId(String typeId) {
        if (typeId == null) {
            this.typeId = "";
        } else {
            this.typeId = typeId.trim();
        }
    } //method setTypeId

    /**
     * 取得主類別代碼
     *
     * @return typeId 主類別代碼
     **/
    public String getTypeId() {
        return this.typeId;
    } //method getTypeId

    /**
     * 設定次類別名稱
     *
     * @param itemName 次類別名稱
     **/
    public void setItemName(String itemName) {
        if (itemName == null) {
            this.itemName = "";
        } else {
            this.itemName = itemName.trim();
        }
    } //method setItemName

    /**
     * 取得次類別名稱
     *
     * @return itemName 次類別名稱
     **/
    public String getItemName() {
        return this.itemName;
    } //method getItemName

    /**
     * 設定排序
     *
     * @param showIndex 排序
     **/
    public void setShowIndex(String showIndex) {
        if (showIndex == null) {
            this.showIndex = "";
        } else {
            this.showIndex = showIndex.trim();
        }
    } //method setShowIndex

    /**
     * 取得排序
     *
     * @return showIndex 排序
     **/
    public String getShowIndex() {
        return this.showIndex;
    } //method getShowIndex

    /**
     * 設定狀態
     *
     * @param state 狀態
     **/
    public void setState(String state) {
        if (state == null) {
            this.state = "";
        } else {
            this.state = state.trim();
        }
    } //method setState

    /**
     * 取得狀態
     *
     * @return state 狀態
     **/
    public String getState() {
        return this.state;
    } //method getState

    /**
     * 設定資料的第一次建立時間
     *
     * @param createTime 資料的第一次建立時間
     **/
    public void setCreateTime(String createTime) {
        if (createTime == null) {
            this.createTime = "";
        } else {
            this.createTime = createTime.trim();
        }
    } //method setCreateTime

    /**
     * 取得資料的第一次建立時間
     *
     * @return createTime 資料的第一次建立時間
     **/
    public String getCreateTime() {
        return this.createTime;
    } //method getCreateTime

    /**
     * 設定資料建立人員代碼
     *
     * @param creatorId 資料建立人員代碼
     **/
    public void setCreatorId(String creatorId) {
        if (creatorId == null) {
            this.creatorId = "";
        } else {
            this.creatorId = creatorId.trim();
        }
    } //method setCreatorId

    /**
     * 取得資料建立人員代碼
     *
     * @return creatorId 資料建立人員代碼
     **/
    public String getCreatorId() {
        return this.creatorId;
    } //method getCreatorId

    /**
     * 設定最後修改人員代碼
     *
     * @param lastModifier 最後修改人員代碼
     **/
    public void setLastModifier(String lastModifier) {
        if (lastModifier == null) {
            this.lastModifier = "";
        } else {
            this.lastModifier = lastModifier.trim();
        }
    } //method setLastModifier

    /**
     * 取得最後修改人員代碼
     *
     * @return lastModifier 最後修改人員代碼
     **/
    public String getLastModifier() {
        return this.lastModifier;
    } //method getLastModifier

    /**
     * 設定最後修改時間
     *
     * @param lastUpdateTime 最後修改時間
     **/
    public void setLastUpdateTime(String lastUpdateTime) {
        if (lastUpdateTime == null) {
            this.lastUpdateTime = "";
        } else {
            this.lastUpdateTime = lastUpdateTime.trim();
        }
    } //method setLastUpdateTime

    /**
     * 取得最後修改時間
     *
     * @return lastUpdateTime 最後修改時間
     **/
    public String getLastUpdateTime() {
        return this.lastUpdateTime;
    } //method getLastUpdateTime

    /**
     * 建構次類別物件.
     *
     * @throws SQLException
     **/
    public ServiceItemObject() throws SQLException {
    }

    /**
     * 建構次類別物件.
     *
     * @param itemId 次類別代碼
     * @throws SQLException
     **/
    public ServiceItemObject(String itemId) throws SQLException {
        this.setItemId(itemId);
        this.load();
    }

    /**
     * 載入次類別物件.
     *
     * @throws SQLException 如果資料庫存取錯誤
     **/
    public void load() throws SQLException {
        DataSource ds = null;
        Connection conn = null;


            ds = (DataSource) ServiceLocator.getInstance().getDataSource();


        try {
            conn = ds.getConnection();
            this.load(conn);
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    } //method load

    /**
     * 載入次類別物件.
     *
     * @param conn 資料庫連線
     * @throws SQLException 如果資料庫存取錯誤
     **/
    public void load(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sqlStmt = null;

            sqlStmt = "SELECT * FROM SERVICE_ITEM WHERE ITEMID = ? ";
            pstmt = conn.prepareStatement(sqlStmt);
            pstmt.setString(1, this.getItemId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                this.setItemId(rs.getString("ITEMID"));
                this.setTypeId(rs.getString("TYPEID"));
                this.setItemName(rs.getString("ITEMNAME"));
                this.setShowIndex(rs.getString("SHOWINDEX"));
                this.setState(rs.getString("STATE"));
                //this.setCreateTime(StringUtil.formatDCDate(rs.getTimestamp("CREATETIME")));
                this.setCreatorId(rs.getString("CREATORID"));
                this.setLastModifier(rs.getString("LASTMODIFIER"));
                //this.setLastUpdateTime(StringUtil.formatDCDate(rs.getTimestamp("LASTUPDATETIME")));
            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    } //method load

    /**
     * 儲存次類別.
     *
     * @throws SQLException 如果在存取資料庫有任何錯誤產生時.
     **/
    public void store() throws SQLException {
        Connection conn = null;

        try {
            DataSource ds = null;

            conn = ds.getConnection();
            conn.setAutoCommit(false);

            this.store(conn);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw e;
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
    } //method store

    /**
     * 儲存次類別.
     *
     * @param conn 資料庫連線物件
     * @throws SQLException 如果在存取資料庫有任何錯誤產生時.
     **/
    public void store(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sqlStmt = "";
            boolean isInsert = this.getItemId().equals("");
            int idx = 0;

            if (isInsert) {
               // this.setItemId(SequenceGenerator.getInstance().getSequence("SERVICE_ITEM_SEQ", "", 0));

                sqlStmt =
                        " INSERT INTO SERVICE_ITEM " +
                                " (ITEMID, TYPEID, ITEMNAME, SHOWINDEX, STATE, CREATETIME, CREATORID, LASTMODIFIER, LASTUPDATETIME) " +
                                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

                pstmt = conn.prepareStatement(sqlStmt);
                pstmt.setString(++idx, this.getItemId());
                pstmt.setString(++idx, this.getTypeId());
                pstmt.setString(++idx, this.getItemName());
                pstmt.setString(++idx, this.getShowIndex().equals("") ? "9999" : this.getShowIndex());
                pstmt.setString(++idx, this.getState().equals("") ? "1" : this.getState());
                pstmt.setTimestamp(++idx, new Timestamp(new java.util.Date().getTime()));
                pstmt.setString(++idx, this.getCreatorId());
                pstmt.setString(++idx, this.getLastModifier());
                pstmt.setTimestamp(++idx, new Timestamp(new java.util.Date().getTime()));

                pstmt.executeUpdate();
            } else {
                sqlStmt =
                        " UPDATE SERVICE_ITEM SET " +
                                " TYPEID = ?, ITEMNAME = ?, SHOWINDEX = ?, STATE = ?, " +
                                " LASTMODIFIER = ?, LASTUPDATETIME = ? WHERE ITEMID = ? ";

                pstmt = conn.prepareStatement(sqlStmt);
                pstmt.setString(++idx, this.getTypeId());
                pstmt.setString(++idx, this.getItemName());
                pstmt.setString(++idx, this.getShowIndex().equals("") ? "9999" : this.getShowIndex());
                pstmt.setString(++idx, this.getState().equals("") ? "1" : this.getState());
                pstmt.setString(++idx, this.getLastModifier());
                pstmt.setTimestamp(++idx, new Timestamp(new java.util.Date().getTime()));
                pstmt.setString(++idx, this.getItemId());

                pstmt.executeUpdate();
            }
           // ServiceInstance.getInstance().setItemName(this.getItemId(), this.getItemName());

            pstmt.close();
            pstmt = null;
        } catch (SQLException ex) {
            throw ex;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    } //method store

}
