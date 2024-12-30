/****************************************************************************
 *
 * Copyright (c) 2011 ESound Tech. All Rights Reserved.
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
 *     File name:       CallFlowDetailObj.java
 *
 *     History:
 *     Date               Author            Comments
 *     -----------------------------------------------------------------------
 *     AUG 3, 2011       Aaron           Initial Release
 *     AUG 15, 2011      Aaron           新增接收退回/初報/結報/結案欄位，預先加入計算工作日功能
 *****************************************************************************/
package com.consilium.excel.models;

/**
 * <code>CallFlowDetailObj</code>
 * 派案明細表物件
 **/
public class CallFlowDetailObj
{
    /**
     *單位別
     **/
    protected String unitType = "" ;

    /**
     *單位代碼
     **/
    protected String unitCode = "" ;

    /**
     *個案編號
     **/
    protected String caseId = "" ;
    
    /**
     *類別
     **/
    protected String type = "" ;

    /**
     *通報日期
     **/
    protected String approveTime = "" ;

    /**
     *接線日期
     **/
    protected String createTime = "" ;

    /**
     *案件狀態
     **/
    protected String caseStatus = "" ;

    /**
     *接收退回日期
     **/
    protected String receiveTime = "" ;

    /**
     *初報日期
     **/
    protected String firstReportTime = "" ;

    /**
     *結報日期
     **/
    protected String endReportTime = "" ;

    /**
     *結案日期
     **/
    protected String endCaseTime = "" ;

    /**
     *接收退回間隔
     **/
    protected String receiveDay = "" ;

    /**
     *初報間隔
     **/
    protected String firstReportDay = "" ;

    /**
     *結報間隔
     **/
    protected String endReportDay = "" ;

    /**
     *結案間隔
     **/
    protected String endCaseDay = "" ;

    /**
     * 設定單位別
     *
     * @param unitType 單位別
     **/
    public void setUnitType ( String unitType )
    {
        if ( unitType == null )
        {
            this.unitType = "" ;
        }
        else
        {
            this.unitType = unitType.trim () ;
        }
    }

    /**
     * 設定單位代碼
     *
     * @param unitCode 單位代碼
     **/
    public void setUnitCode ( String unitCode )
    {
        if ( unitCode == null )
        {
            this.unitCode = "" ;
        }
        else
        {
            this.unitCode = unitCode.trim () ;
        }
    }

    /**
     * 設定類別
     *
     * @param type 類別
     **/
    public void setType ( String type )
    {
        if ( type == null )
        {
            this.type = "" ;
        }
        else
        {
            this.type = type.trim () ;
        }
    }

    /**
     * 設定個案編號
     *
     * @param caseId 個案編號
     **/
    public void setCaseId ( String caseId )
    {
        if ( caseId == null )
        {
            this.caseId = "" ;
        }
        else
        {
            this.caseId = caseId.trim () ;
        }
    }

    /**
     * 設定通報日期
     *
     * @param approveTime 通報日期
     **/
    public void setApproveTime ( String approveTime )
    {
        if ( approveTime == null )
        {
            this.approveTime = "" ;
        }
        else
        {
            this.approveTime = approveTime.trim () ;
        }
    }

    /**
     * 設定接線日期
     *
     * @param createTime 接線日期
     **/
    public void setCreateTime ( String createTime )
    {
        if ( createTime == null )
        {
            this.createTime = "" ;
        }
        else
        {
            this.createTime = createTime.trim () ;
        }
    }

    /**
     * 設定案件狀態
     *
     * @param caseStatus 案件狀態
     **/
    public void setCaseStatus( String caseStatus )
    {
        if( caseStatus == null )
        {
            this.caseStatus = "" ;
        }
        else
        {
            this.caseStatus = caseStatus.trim() ;
        }
    }

    /**
     * 設定接收退回日期
     *
     * @param receiveTime 接收退回日期
     **/
    public void setReceiveTime( String receiveTime )
    {
        if( receiveTime == null )
        {
            this.receiveTime = "" ;
        }
        else
        {
            this.receiveTime = receiveTime.trim() ;
        }
    }

    /**
     * 設定初報日期
     *
     * @param firstReportTime 初報日期
     **/
    public void setFirstReportTime( String firstReportTime )
    {
        if( firstReportTime == null )
        {
            this.firstReportTime = "" ;
        }
        else
        {
            this.firstReportTime = firstReportTime.trim() ;
        }
    }

    /**
     * 設定結報日期
     *
     * @param endReportTime 結報日期
     **/
    public void setEndReportTime( String endReportTime )
    {
        if( endReportTime == null )
        {
            this.endReportTime = "" ;
        }
        else
        {
            this.endReportTime = endReportTime.trim() ;
        }
    }

    /**
     * 設定結案日期
     *
     * @param endCaseTime 結案日期
     **/
    public void setEndCaseTime( String endCaseTime )
    {
        if( endCaseTime == null )
        {
            this.endCaseTime = "" ;
        }
        else
        {
            this.endCaseTime = endCaseTime.trim() ;
        }
    }

    /**
     * 設定接收退回間隔
     *
     * @param receiveDay 接收退回間隔
     **/
    public void setReceiveDay( String receiveDay )
    {
        if( receiveDay == null )
        {
            this.receiveDay = "" ;
        }
        else
        {
            this.receiveDay = receiveDay.trim() ;
        }
    }

    /**
     * 設定初報間隔
     *
     * @param firstReportDay 初報間隔
     **/
    public void setFirstReportDay( String firstReportDay )
    {
        if( firstReportDay == null )
        {
            this.firstReportDay = "" ;
        }
        else
        {
            this.firstReportDay = firstReportDay.trim() ;
        }
    }

    /**
     * 設定結報間隔
     *
     * @param endReportDay 結報間隔
     **/
    public void setEndReportDay( String endReportDay )
    {
        if( endReportDay == null )
        {
            this.endReportDay = "" ;
        }
        else
        {
            this.endReportDay = endReportDay.trim() ;
        }
    }

    /**
     * 設定結案間隔
     *
     * @param endCaseDay 結案間隔
     **/
    public void setEndCaseDay( String endCaseDay )
    {
        if( endCaseDay == null )
        {
            this.endCaseDay = "" ;
        }
        else
        {
            this.endCaseDay = endCaseDay.trim() ;
        }
    }

    /**
     * 取得單位別
     *
     * @return unitType 單位別
     **/
    public String getUnitType ()
    {
        return this.unitType ;
    }

    /**
     * 取得單位代碼
     *
     * @return unitCode 單位代碼
     **/
    public String getUnitCode ()
    {
        return this.unitCode ;
    }

    /**
     * 取得類別
     *
     * @return type 類別
     **/
    public String getType ()
    {
        return this.type ;
    }

    /**
     * 取得個案編號
     *
     * @return caseId 個案編號
     **/
    public String getCaseId ()
    {
        return this.caseId ;
    }

    /**
     * 取得通報日期
     *
     * @return approveTime 通報日期
     **/
    public String getApproveTime ()
    {
        return this.approveTime ;
    }

    /**
     * 取得接線日期
     *
     * @return createTime 接線日期
     **/
    public String getCreateTime ()
    {
        return this.createTime ;
    }

    /**
     * 取得案件狀態
     *
     * @return caseStatus 案件狀態
     **/
    public String getCaseStatus()
    {
        return this.caseStatus ;
    }

    /**
     * 取得接收退回日期
     *
     * @return receiveTime 接收退回日期
     **/
    public String getReceiveTime()
    {
        return this.receiveTime ;
    }

    /**
     * 取得初報日期
     *
     * @return firstReportTime 初報日期
     **/
    public String getFirstReportTime()
    {
        return this.firstReportTime ;
    }

    /**
     * 取得結報日期
     *
     * @return endReportTime 結報日期
     **/
    public String getEndReportTime()
    {
        return this.endReportTime ;
    }

    /**
     * 取得結案日期
     *
     * @return endCaseTime 結案日期
     **/
    public String getEndCaseTime()
    {
        return this.endCaseTime ;
    }

    /**
     * 取得接收退回間隔
     *
     * @return receiveDay 接收退回間隔
     **/
    public String getReceiveDay()
    {
        return this.receiveDay ;
    }

    /**
     * 取得初報間隔
     *
     * @return firstReportDay 初報間隔
     **/
    public String getFirstReportDay()
    {
        return this.firstReportDay ;
    }

    /**
     * 取得結報間隔
     *
     * @return endReportDay 結報間隔
     **/
    public String getEndReportDay()
    {
        return this.endReportDay ;
    }

    /**
     * 取得結案間隔
     *
     * @return endCaseDay 結案間隔
     **/
    public String getEndCaseDay()
    {
        return this.endCaseDay ;
    }
}
