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
 *     File name:       ServiceTypeObject.java
 *
 *     History:
 *     Date                          Comments
 *     -----------------------------------------------------------------------
 *     DEC 9, 2014                   Initial Release
 *****************************************************************************/
package com.consilium.excel.models;


import java.util.ArrayList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * <code>ServiceTypeObject</code>
 * 主類別物件
 **/
public class ServiceTypeObject
{
	/**
	 * 主類別代碼
	 **/
	private String typeId = "";

	/**
	 * 主類別名稱
	 **/
	private String typeName = "";

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
	 * 所有次類別清單(非刪除)
	 **/
	private ArrayList<ServiceItemObject> itemList = null;

	/**
	 * 啟用中次類別清單
	 **/
	private ArrayList<ServiceItemObject> itemEnableList = null;

	/**
	 * 設定主類別代碼
	 *
	 * @param typeId 主類別代碼
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
	 * 取得主類別代碼
	 *
	 * @return typeId 主類別代碼
	 **/
	public String getTypeId()
	{
		return this.typeId;
	} //method getTypeId

	/**
	 * 設定主類別名稱
	 *
	 * @param typeName 主類別名稱
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
	 * 取得主類別名稱
	 *
	 * @return typeName 主類別名稱
	 **/
	public String getTypeName()
	{
		return this.typeName;
	} //method getTypeName

	/**
	 * 設定排序
	 *
	 * @param showIndex 排序
	 **/
	public void setShowIndex(String showIndex)
	{
		if (showIndex == null)
		{
			this.showIndex = "";
		}
		else
		{
			this.showIndex = showIndex.trim();
		}
	} //method setShowIndex

	/**
	 * 取得排序
	 *
	 * @return showIndex 排序
	 **/
	public String getShowIndex()
	{
		return this.showIndex;
	} //method getShowIndex

	/**
	 * 設定狀態
	 *
	 * @param state 狀態
	 **/
	public void setState(String state)
	{
		if (state == null)
		{
			this.state = "";
		}
		else
		{
			this.state = state.trim();
		}
	} //method setState

	/**
	 * 取得狀態
	 *
	 * @return state 狀態
	 **/
	public String getState()
	{
		return this.state;
	} //method getState

	/**
	 * 設定資料的第一次建立時間
	 *
	 * @param createTime 資料的第一次建立時間
	 **/
	public void setCreateTime(String createTime)
	{
		if (createTime == null)
		{
			this.createTime = "";
		}
		else
		{
			this.createTime = createTime.trim();
		}
	} //method setCreateTime

	/**
	 * 取得資料的第一次建立時間
	 *
	 * @return createTime 資料的第一次建立時間
	 **/
	public String getCreateTime()
	{
		return this.createTime;
	} //method getCreateTime

	/**
	 * 設定資料建立人員代碼
	 *
	 * @param creatorId 資料建立人員代碼
	 **/
	public void setCreatorId(String creatorId)
	{
		if (creatorId == null)
		{
			this.creatorId = "";
		}
		else
		{
			this.creatorId = creatorId.trim();
		}
	} //method setCreatorId

	/**
	 * 取得資料建立人員代碼
	 *
	 * @return creatorId 資料建立人員代碼
	 **/
	public String getCreatorId()
	{
		return this.creatorId;
	} //method getCreatorId

	/**
	 * 設定最後修改人員代碼
	 *
	 * @param lastModifier 最後修改人員代碼
	 **/
	public void setLastModifier(String lastModifier)
	{
		if (lastModifier == null)
		{
			this.lastModifier = "";
		}
		else
		{
			this.lastModifier = lastModifier.trim();
		}
	} //method setLastModifier

	/**
	 * 取得最後修改人員代碼
	 *
	 * @return lastModifier 最後修改人員代碼
	 **/
	public String getLastModifier()
	{
		return this.lastModifier;
	} //method getLastModifier

	/**
	 * 設定最後修改時間
	 *
	 * @param lastUpdateTime 最後修改時間
	 **/
	public void setLastUpdateTime(String lastUpdateTime)
	{
		if (lastUpdateTime == null)
		{
			this.lastUpdateTime = "";
		}
		else
		{
			this.lastUpdateTime = lastUpdateTime.trim();
		}
	} //method setLastUpdateTime

	/**
	 * 取得最後修改時間
	 *
	 * @return lastUpdateTime 最後修改時間
	 **/
	public String getLastUpdateTime()
	{
		return this.lastUpdateTime;
	} //method getLastUpdateTime

	/**
	 * 取得所有次類別清單(非刪除)
	 *
	 * @return itemList 所有次類別清單(非刪除)
	 **/
	public ArrayList<ServiceItemObject> getItemList()
	{
		return this.itemList;
	} //method getItemList

	/**
	 * 取得啟用中次類別清單(非刪除)
	 *
	 * @return itemEnableList 啟用中次類別清單(非刪除)
	 **/
	public ArrayList<ServiceItemObject> getItemEnableList()
	{
		return this.itemEnableList;
	} //method getItemEnableList

	/**
	 * 建構主類別物件
	 *
	 * @throws SQLException
	 **/
	public ServiceTypeObject() throws SQLException{
		this.setTypeId(typeId);
		this.load();
	}

	public ServiceTypeObject(String typeId) throws SQLException{
		this.setTypeId(typeId);
		this.load();
	}

	/**
	 * 載入物件.
	 * @throws SQLException 如果資料庫存取錯誤
	 **/
	public void load() throws SQLException
	{
		DataSource ds = null;
		Connection conn = null;


			ds = (DataSource) ServiceLocator.getInstance().getDataSource();


		try
		{
			conn = ds.getConnection();
			this.load(conn);
		}
		catch(SQLException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if(conn != null)
				{
					conn.close();
				}
			}
			catch(SQLException ex)
			{
				ex.printStackTrace();
			}
		}
	} //method load

	/**
	 * 載入物件.
	 *
	 * @param conn 資料庫連線
	 * @throws SQLException 如果資料庫存取錯誤
	 **/
	public void load(Connection conn) throws SQLException
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			ArrayList<ServiceItemObject> tmpItemList = new ArrayList<ServiceItemObject>();
			ArrayList<ServiceItemObject> tmpItemEnableList = new ArrayList<ServiceItemObject>();

			String sqlStmt = null;

			sqlStmt = "SELECT * FROM SERVICE_TYPE WHERE TYPEID = ?  and typeId not in (Select codeCode from APP_CODE where category='R09') ";
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setString(1, this.getTypeId());
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				this.setTypeId(rs.getString("TYPEID"));
				this.setTypeName(rs.getString("TYPENAME"));
				this.setShowIndex(rs.getString("SHOWINDEX"));
				this.setState(rs.getString("STATE"));
				//this.setCreateTime(StringUtil.formatDCDate(rs.getTimestamp("CREATETIME")));
				this.setCreatorId(rs.getString("CREATORID"));
				this.setLastModifier(rs.getString("LASTMODIFIER"));
				//this.setLastUpdateTime(StringUtil.formatDCDate(rs.getTimestamp("LASTUPDATETIME")));
			}

			sqlStmt = "SELECT * FROM SERVICE_ITEM WHERE TYPEID = ? and itemId not in (Select codeCode from app_code where category='R10') ORDER BY SHOWINDEX ASC ";
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setString(1, this.getTypeId());
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				ServiceItemObject itemObj = new ServiceItemObject();
				itemObj.setItemId(rs.getString("ITEMID"));
				itemObj.setTypeId(rs.getString("TYPEID"));
				itemObj.setItemName(rs.getString("ITEMNAME"));
				itemObj.setShowIndex(rs.getString("SHOWINDEX"));
				itemObj.setState(rs.getString("STATE"));
				//itemObj.setCreateTime(StringUtil.formatDCDate(rs.getTimestamp("CREATETIME")));
				itemObj.setCreatorId(rs.getString("CREATORID"));
				itemObj.setLastModifier(rs.getString("LASTMODIFIER"));
				//itemObj.setLastUpdateTime(StringUtil.formatDCDate(rs.getTimestamp("LASTUPDATETIME")));
				if(!itemObj.getState().equals("2"))
				{
					tmpItemList.add(itemObj);
				}
				if(itemObj.getState().equals("1"))
				{
					tmpItemEnableList.add(itemObj);
				}
			}
			this.itemList = tmpItemList;
			this.itemEnableList = tmpItemEnableList;

			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;

		}
		catch(SQLException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if(rs != null)
				{
					rs.close();
				}
			}
			catch(SQLException ex)
			{
				ex.printStackTrace();
			}

			try
			{
				if(pstmt != null)
				{
					pstmt.close();
				}
			}
			catch(SQLException ex)
			{
				ex.printStackTrace();
			}
		}
	} //method load

}
