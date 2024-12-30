package com.consilium.excel.models.agentefficient;

import java.util.ArrayList;

public class AgentEfficientReportObject
{
     /**
      *str全部等待時間
      **/
     protected String strALLTotalLoginReadyIdleDuration = "";

     /**
      *str全部離席時間
      **/
     protected String strAllTotalLoginNotReadyIdleDuration = "";

     /**
      *str全部文書作業
      **/
     protected String strAllTotalLoginPcpIdleDuration = "";

     /**
      *str全部談話時間
      **/
     protected String strAllTotalDcpInboundCallsDuration = "";

     /**
      *str全部總登入時間
      **/
     protected String strAllTotalLoginDuration = "";
     
     /**
      *str全部總外撥時間
      **/
     protected String strAllTotalPrivateOutBoundDuration = "";
     
     
     /**
      *str全部總保留時間
      **/
     protected String strAllTotalHoldDuration = "";
    
    /**
     *報表產生時間
     **/
    protected String printDate = "";

    /**
     *報表起始日期
     **/
    protected String startDate = "";

    /**
     *報表結束日期
     **/
    protected String endDate = "";

    /**
     *預警資料列
     **/
    protected ArrayList dataList = new ArrayList();
    
   
    
    /**
     *ArrayList筆數，是用來判斷查詢出的資料是否為空
     **/
     protected int dataListnum=0;
    
    /**
     *列印單位
     **/
    protected String userUnit = "";
    
    /**
     *列印人員
     **/
    protected String userName = "";
    
    
    /**
     *全部等待時間
     **/
    protected int intALLTotalLoginReadyIdleDuration;

    /**
     *全部離席時間
     **/
    protected int intAllTotalLoginNotReadyIdleDuration;

    /**
     *全部文書作業
     **/
    protected int intAllTotalLoginPcpIdleDuration;

    /**
     *全部談話時間
     **/
    protected int intAllTotalDcpInboundCallsDuration;

    /**
     *全部總登入時間
     **/
    protected int intAllTotalLoginDuration;

    /**
     *全部總受理通數
     **/
    protected int intAllTnbrDcpInboundCallsAnswered;
    
    /**
     *全部總保留時間
     **/
    protected int intAllTotalHoldDuration;
    
    /**
     *全部總外撥時間
     **/
    protected int intAlltotalPrivateOutBoundDuration;

    
    /**
     *總平均每通秒數
     **/
    protected double AllaverageEveryCallSec = 0;
    
    /**
     *總平均每通秒數
     **/
    protected String strAllaverageEveryCallSec = "00:00:00";

        
    /**
     *全部等待時間百分比
     **/
    protected double intALLTotalLoginReadyIdleDurationPercent;

    /**
     *全部離席時間百分比
     **/
    protected double intAllTotalLoginNotReadyIdleDurationPercent;

    /**
     *全部文書作業百分比
     **/
    protected double intAllTotalLoginPcpIdleDurationPercent;

    /**
     *全部談話時間百分比
     **/
    protected double intAllTotalDcpInboundCallsDurationPercent;
    
    
    /**
	 * 有效值機時間
	 **/
	private String strALLValidWorkTime = "";

	/**
	 * 值機率
	 **/
	private String strALLWorkPercent = "";

	/**
	 * 利用率
	 **/
	private String strALLUsePercent = "";

	/**
	 * 平均處理時間
	 **/
	private String strALLAHT = "00:00:00";

	/**
	 * 平均文書處理時間
	 **/
	private String strALLACW = "00:00:00";

	/**
	 * 總進線數
	 **/
	private String strALLCallLogAmount = "0";

	/**
	 * 無效電話
	 **/
	private String strALLNotValidPhone = "0";

	/**
	 * 進線數(不包無效電話)
	 **/
	private int intAllInbound;

	/**
	 * 有效值機時間
	 **/
	private int intAllValidWorkTime;

	/**
	 * 值機率
	 **/
	private double doubleAllWorkPercent;

	/**
	 * 利用率
	 **/
	private double doubleAllUsePercent;

	/**
	 * 平均處理時間
	 **/
	private int intAllAHT;

	/**
	 * 平均文書處理時間
	 **/
	private int intAllACW;

	/**
	 * 總進線數
	 **/
	private int intAllCallLogAmount;

	/**
	 * 無效電話
	 **/
	private int intAllNotValidPhone;

    /**
     *全部暫時離席_code0
     **/
    protected String allTotalNotReadyDuration_0 = "";

    /**
     *全部暫時離席_code1
     **/
    protected String allTotalNotReadyDuration_1 = "";

    /**
     *全部暫時離席_code2
     **/
    protected String allTotalNotReadyDuration_2 = "";

    /**
     *全部暫時離席_code3
     **/
    protected String allTotalNotReadyDuration_3 = "";

    /**
     *全部暫時離席_code4
     **/
    protected String allTotalNotReadyDuration_4 = "";

    /**
     *全部暫時離席_code5
     **/
    protected String allTotalNotReadyDuration_5 = "";

    /**
     *全部暫時離席_code6
     **/
    protected String allTotalNotReadyDuration_6 = "";

    /**
     *全部暫時離席_code7
     **/
    protected String allTotalNotReadyDuration_7 = "";

    /**
     *全部暫時離席_code8
     **/
    protected String allTotalNotReadyDuration_8 = "";

    /**
     *全部暫時離席_code9
     **/
    protected String allTotalNotReadyDuration_9 = "";

    /**
     *全部暫時離席_code10
     **/
    protected String allTotalNotReadyDuration_10 = "";

    /**
     *全部暫時離席百分比_code0
     **/
    protected double intAllTotalNotReadyDurationPercent_0 = 0;

    /**
     *全部暫時離席百分比_code1
     **/
    protected double intAllTotalNotReadyDurationPercent_1 = 0;

    /**
     *全部暫時離席百分比_code2
     **/
    protected double intAllTotalNotReadyDurationPercent_2 = 0;

    /**
     *全部暫時離席百分比_code3
     **/
    protected double intAllTotalNotReadyDurationPercent_3 = 0;

    /**
     *全部暫時離席百分比_code4
     **/
    protected double intAllTotalNotReadyDurationPercent_4 = 0;

    /**
     *全部暫時離席百分比_code5
     **/
    protected double intAllTotalNotReadyDurationPercent_5 = 0;

    /**
     *全部暫時離席百分比_code6
     **/
    protected double intAllTotalNotReadyDurationPercent_6 = 0;

    /**
     *全部暫時離席百分比_code7
     **/
    protected double intAllTotalNotReadyDurationPercent_7 = 0;

    /**
     *全部暫時離席百分比_code8
     **/
    protected double intAllTotalNotReadyDurationPercent_8 = 0;

    /**
     *全部暫時離席百分比_code9
     **/
    protected double intAllTotalNotReadyDurationPercent_9 = 0;

    /**
     *全部暫時離席百分比_code10
     **/
    protected double intAllTotalNotReadyDurationPercent_10 = 0;
    
    /**
    * 設定報表產生時間
    *
    * @param printDate 報表產生時間
    **/
    public void setPrintDate(String printDate)
    {
           if (printDate == null)
           {
               this.printDate = "";
           }
           else
           {
               this.printDate = printDate.trim();
           }
    }

    /**
    * 設定報表起始日期
    *
    * @param startDate 報表起始日期
    **/
    public void setStartDate(String startDate)
    {
           if (startDate == null)
           {
               this.startDate = "";
           }
           else
           {
               this.startDate = startDate.trim();
           }
    }

    /**
    * 設定報表結束日期
    *
    * @param endDate 報表結束日期
    **/
    public void setEndDate(String endDate)
       {
           if (endDate == null)
           {
               this.endDate = "";
           }
           else
           {
               this.endDate = endDate.trim();
           }
    }

    /**
    * 設定預警資料列
    *
    * @param dataList 預警資料列
    **/
    public void setDataList(ArrayList dataList)
    {
           this.dataList = dataList;
    }
       
      /**
       * 設定列印單位
       *
       * @param String 設定列印單位
       **/
       public void setUserUnit(String userUnit)
       {
           this.userUnit = userUnit;
       }
    
      /**
       * 設定列印人員
       *
       * @param String 列印人員
       **/
       public void setUserName(String userName)
       {
           this.userName = userName;
       }
       
      /**
       * 設定dataListnum
       *
       * @param int dataListnum
       **/
       public void setDataListnum(int dataListnum)
       {
           this.dataListnum= dataListnum;
       }
       
    /**
     * 設定總每通平均秒數
     *
     * @param double AllaverageEveryCallSec
     **/
     public void setAllaverageEveryCallSec(double AllaverageEveryCallSec)
     {
         this.AllaverageEveryCallSec= AllaverageEveryCallSec;
     }
     
    /**
     * 設定總每通平均秒數
     *
     * @param String strAllaverageEveryCallSec
     **/
     public void setStrAllaverageEveryCallSec(String strAllaverageEveryCallSec)
     {
         this.strAllaverageEveryCallSec= strAllaverageEveryCallSec;
     }
       


      /**
       * 取得dataListnum
       *
       * @return dataListnum 
       **/
       public int getDataListnum()
       {
           return this.dataListnum;
       }
    /**
    * 取得報表產生時間
    *
    * @return printDate 報表產生時間
    **/
    public String getPrintDate()
    {
           return this.printDate;
    }

    /**
    * 取得報表起始日期
    *
    * @return startDate 報表起始日期
    **/
    public String getStartDate()
    {
           return this.startDate;
    }

    /**
    * 取得報表結束日期
    *
    * @return endDate 報表結束日期
    **/
    public String getEndDate()
    {
           return this.endDate;
    }
       
      /**
       * 取得列印單位
       *
       * @return userUnit 使用單位
       **/
       public String getUserUnit()
       {
           return this.userUnit;
       }
    
      /**
       * 取得列印人員
       *
       * @return userName  列印人員
       **/
       public String getUserName()
       {
           return this.userName;
       }
    
        /**
         * 取得預警資料列
         *
         * @return dataList 預警資料列
         **/
        public ArrayList getDataList()
        {
            return this.dataList;
        }
        
        
    /**
     * 取得總每通平均秒數
     *
     * @param double AllaverageEveryCallSec
     **/
     public double getAllaverageEveryCallSec()
     {
         return this.AllaverageEveryCallSec;
     }
     
    /**
     * 取得總每通平均秒數
     *
     * @param String strAllaverageEveryCallSec
     **/
     public String getStrAllaverageEveryCallSec()
     {
         return this.strAllaverageEveryCallSec;
     }
       
        
       
        /**
         * 將報表物件存放入List中!!
         * @param obj PredictRemitAlertDateObject
         */
        public void addDataList(AgentEfficientDataObject obj)
        {
            this.dataList.add(obj);
        }
        
        /**
         * 設定全部等待時間
         *
         * @param intALLTotalLoginReadyIdleDuration 全部等待時間
         **/
         public void setIntALLTotalLoginReadyIdleDuration(int intALLTotalLoginReadyIdleDuration)
         {
             this.intALLTotalLoginReadyIdleDuration = intALLTotalLoginReadyIdleDuration;
         }

        /**
         * 設定全部離席時間
         *
         * @param intAllTotalLoginNotReadyIdleDuration 全部離席時間
         **/
         public void setIntAllTotalLoginNotReadyIdleDuration(int intAllTotalLoginNotReadyIdleDuration)
         {
             this.intAllTotalLoginNotReadyIdleDuration = intAllTotalLoginNotReadyIdleDuration;
         }

        /**
         * 設定全部文書作業
         *
         * @param intAllTotalLoginPcpIdleDuration 全部文書作業
         **/
         public void setIntAllTotalLoginPcpIdleDuration(int intAllTotalLoginPcpIdleDuration)
         {
             this.intAllTotalLoginPcpIdleDuration = intAllTotalLoginPcpIdleDuration;
         }

        /**
         * 設定全部談話時間
         *
         * @param intAllTotalDcpInboundCallsDuration 全部談話時間
         **/
         public void setIntAllTotalDcpInboundCallsDuration(int intAllTotalDcpInboundCallsDuration)
         {
             this.intAllTotalDcpInboundCallsDuration = intAllTotalDcpInboundCallsDuration;
         }

        /**
         * 設定全部總登入時間
         *
         * @param intAllTotalLoginDuration 全部總登入時間
         **/
         public void setIntAllTotalLoginDuration(int intAllTotalLoginDuration)
         {
             this.intAllTotalLoginDuration = intAllTotalLoginDuration;
         }

        /**
         * 設定全部總受理通數
         *
         * @param intAllTnbrDcpInboundCallsAnswered 全部總受理通數
         **/
         public void setIntAllTnbrDcpInboundCallsAnswered(int intAllTnbrDcpInboundCallsAnswered)
         {
             this.intAllTnbrDcpInboundCallsAnswered = intAllTnbrDcpInboundCallsAnswered;
         }

        /**
         * 取得全部等待時間
         *
         * @return intALLTotalLoginReadyIdleDuration 全部等待時間
         **/
         public int getIntALLTotalLoginReadyIdleDuration()
         {
             return this.intALLTotalLoginReadyIdleDuration;
         }
         
         
         /**
          * 設定全部總外撥時間
          *
          * @param intAlltotalPrivateOutBoundDuration 全部總外撥時間
          **/
          public void setIntAlltotalPrivateOutBoundDuration(int intAlltotalPrivateOutBoundDuration)
          {
              this.intAlltotalPrivateOutBoundDuration = intAlltotalPrivateOutBoundDuration;
          }

         /**
          * 取得全部總外撥時間
          *
          * @return intAlltotalPrivateOutBoundDuration 全部總外撥時間
          **/
          public int getIntAlltotalPrivateOutBoundDuration()
          {
              return this.intAlltotalPrivateOutBoundDuration;
          }
         
         

        /**
         * 取得全部離席時間
         *
         * @return intAllTotalLoginNotReadyIdleDuration 全部離席時間
         **/
         public int getIntAllTotalLoginNotReadyIdleDuration()
         {
             return this.intAllTotalLoginNotReadyIdleDuration;
         }

        /**
         * 取得全部文書作業
         *
         * @return intAllTotalLoginPcpIdleDuration 全部文書作業
         **/
         public int getIntAllTotalLoginPcpIdleDuration()
         {
             return this.intAllTotalLoginPcpIdleDuration;
         }

        /**
         * 取得全部談話時間
         *
         * @return intAllTotalDcpInboundCallsDuration 全部談話時間
         **/
         public int getIntAllTotalDcpInboundCallsDuration()
         {
             return this.intAllTotalDcpInboundCallsDuration;
         }

        /**
         * 取得全部總登入時間
         *
         * @return intAllTotalLoginDuration 全部總登入時間
         **/
         public int getIntAllTotalLoginDuration()
         {
             return this.intAllTotalLoginDuration;
         }

        /**
         * 取得全部總受理通數
         *
         * @return intAllTnbrDcpInboundCallsAnswered 全部總受理通數
         **/
         public int getIntAllTnbrDcpInboundCallsAnswered()
         {
             return this.intAllTnbrDcpInboundCallsAnswered;
         }
         
        /**
         * 設定全部等待時間百分比
         *
         * @param intALLTotalLoginReadyIdleDurationPercent 全部等待時間百分比
         **/
         public void setIntALLTotalLoginReadyIdleDurationPercent(double intALLTotalLoginReadyIdleDurationPercent)
         {
             this.intALLTotalLoginReadyIdleDurationPercent = intALLTotalLoginReadyIdleDurationPercent;
         }

        /**
         * 設定全部離席時間百分比
         *
         * @param intAllTotalLoginNotReadyIdleDurationPercent 全部離席時間百分比
         **/
         public void setIntAllTotalLoginNotReadyIdleDurationPercent(double intAllTotalLoginNotReadyIdleDurationPercent)
         {
             this.intAllTotalLoginNotReadyIdleDurationPercent = intAllTotalLoginNotReadyIdleDurationPercent;
         }

        /**
         * 設定全部文書作業百分比
         *
         * @param intAllTotalLoginPcpIdleDurationPercent 全部文書作業百分比
         **/
         public void setIntAllTotalLoginPcpIdleDurationPercent(double intAllTotalLoginPcpIdleDurationPercent)
         {
             this.intAllTotalLoginPcpIdleDurationPercent = intAllTotalLoginPcpIdleDurationPercent;
         }

        /**
         * 設定全部談話時間百分比
         *
         * @param intAllTotalDcpInboundCallsDurationPercent 全部談話時間百分比
         **/
         public void setIntAllTotalDcpInboundCallsDurationPercent(double intAllTotalDcpInboundCallsDurationPercent)
         {
             this.intAllTotalDcpInboundCallsDurationPercent = intAllTotalDcpInboundCallsDurationPercent;
         }

        /**
         * 取得全部等待時間百分比
         *
         * @return intALLTotalLoginReadyIdleDurationPercent 全部等待時間百分比
         **/
         public double getIntALLTotalLoginReadyIdleDurationPercent()
         {
             return this.intALLTotalLoginReadyIdleDurationPercent;
         }

        /**
         * 取得全部離席時間百分比
         *
         * @return intAllTotalLoginNotReadyIdleDurationPercent 全部離席時間百分比
         **/
         public double getIntAllTotalLoginNotReadyIdleDurationPercent()
         {
             return this.intAllTotalLoginNotReadyIdleDurationPercent;
         }

        /**
         * 取得全部文書作業百分比
         *
         * @return intAllTotalLoginPcpIdleDurationPercent 全部文書作業百分比
         **/
         public double getIntAllTotalLoginPcpIdleDurationPercent()
         {
             return this.intAllTotalLoginPcpIdleDurationPercent;
         }

        /**
         * 取得全部談話時間百分比
         *
         * @return intAllTotalDcpInboundCallsDurationPercent 全部談話時間百分比
         **/
         public double getIntAllTotalDcpInboundCallsDurationPercent()
         {
             return this.intAllTotalDcpInboundCallsDurationPercent;
         }
         
     /**
          * 設定str全部等待時間
          *
          * @param strALLTotalLoginReadyIdleDuration str全部等待時間
          **/
          public void setStrALLTotalLoginReadyIdleDuration(String strALLTotalLoginReadyIdleDuration)
          {
              if (strALLTotalLoginReadyIdleDuration == null)
              {
                  this.strALLTotalLoginReadyIdleDuration = "";
              }
              else
              {
                  this.strALLTotalLoginReadyIdleDuration = strALLTotalLoginReadyIdleDuration.trim();
              }
          }

         /**
          * 設定str全部離席時間
          *
          * @param strAllTotalLoginNotReadyIdleDuration str全部離席時間
          **/
          public void setStrAllTotalLoginNotReadyIdleDuration(String strAllTotalLoginNotReadyIdleDuration)
          {
              if (strAllTotalLoginNotReadyIdleDuration == null)
              {
                  this.strAllTotalLoginNotReadyIdleDuration = "";
              }
              else
              {
                  this.strAllTotalLoginNotReadyIdleDuration = strAllTotalLoginNotReadyIdleDuration.trim();
              }
          }

         /**
          * 設定str全部文書作業
          *
          * @param strAllTotalLoginPcpIdleDuration str全部文書作業
          **/
          public void setStrAllTotalLoginPcpIdleDuration(String strAllTotalLoginPcpIdleDuration)
          {
              if (strAllTotalLoginPcpIdleDuration == null)
              {
                  this.strAllTotalLoginPcpIdleDuration = "";
              }
              else
              {
                  this.strAllTotalLoginPcpIdleDuration = strAllTotalLoginPcpIdleDuration.trim();
              }
          }

         /**
          * 設定str全部談話時間
          *
          * @param strAllTotalDcpInboundCallsDuration str全部談話時間
          **/
          public void setStrAllTotalDcpInboundCallsDuration(String strAllTotalDcpInboundCallsDuration)
          {
              if (strAllTotalDcpInboundCallsDuration == null)
              {
                  this.strAllTotalDcpInboundCallsDuration = "";
              }
              else
              {
                  this.strAllTotalDcpInboundCallsDuration = strAllTotalDcpInboundCallsDuration.trim();
              }
          }

         /**
          * 設定str全部總登入時間
          *
          * @param strAllTotalLoginDuration str全部總登入時間
          **/
          public void setStrAllTotalLoginDuration(String strAllTotalLoginDuration)
          {
              if (strAllTotalLoginDuration == null)
              {
                  this.strAllTotalLoginDuration = "";
              }
              else
              {
                  this.strAllTotalLoginDuration = strAllTotalLoginDuration.trim();
              }
          }

         /**
          * 取得str全部等待時間
          *
          * @return strALLTotalLoginReadyIdleDuration str全部等待時間
          **/
          public String getStrALLTotalLoginReadyIdleDuration()
          {
              return this.strALLTotalLoginReadyIdleDuration;
          }

         /**
          * 取得str全部離席時間
          *
          * @return strAllTotalLoginNotReadyIdleDuration str全部離席時間
          **/
          public String getStrAllTotalLoginNotReadyIdleDuration()
          {
              return this.strAllTotalLoginNotReadyIdleDuration;
          }

         /**
          * 取得str全部文書作業
          *
          * @return strAllTotalLoginPcpIdleDuration str全部文書作業
          **/
          public String getStrAllTotalLoginPcpIdleDuration()
          {
              return this.strAllTotalLoginPcpIdleDuration;
          }

         /**
          * 取得str全部談話時間
          *
          * @return strAllTotalDcpInboundCallsDuration str全部談話時間
          **/
          public String getStrAllTotalDcpInboundCallsDuration()
          {
              return this.strAllTotalDcpInboundCallsDuration;
          }

         /**
          * 取得str全部總登入時間
          *
          * @return strAllTotalLoginDuration str全部總登入時間
          **/
          public String getStrAllTotalLoginDuration()
          {
              return this.strAllTotalLoginDuration;
          }
          
          
          /**
           * 設定str全部總外撥時間
           *
           * @return strAllTotalPrivateOutBoundDuration str全部總外撥時間
           **/
          public void setStrAllTotalPrivateOutBoundDuration(String strAllTotalPrivateOutBoundDuration)
          {
        	  if (strAllTotalPrivateOutBoundDuration == null)
              {
                  this.strAllTotalPrivateOutBoundDuration = "";
              }
              else
              {
                  this.strAllTotalPrivateOutBoundDuration = strAllTotalPrivateOutBoundDuration.trim();
              }
          }
          
          /**
           * 取得str全部總外撥時間
           *
           * @return strAllTotalPrivateOutBoundDuration str全部總外撥時間
           **/
          public String getStrAllTotalPrivateOutBoundDuration()
          {
              return this.strAllTotalPrivateOutBoundDuration;
          }
          
          
          
          /**
           * 設定str總保留時間
           *
           * @param int 設定總保留時間
           **/
          public void setStrAllTotalHoldDuration(String strAllTotalHoldDuration)
          {
              if(strAllTotalHoldDuration==null)
              {
            	  this.strAllTotalHoldDuration="";
              }
              else
              {
            	  this.strAllTotalHoldDuration=strAllTotalHoldDuration;
              }	  
  
          }
          
          /**
           * 取得str全部總保留時間
           *
           * @return strAllTotalPrivateOutBoundDuration str全部總外撥時間
           **/
          public String getStrAllTotalHoldDuration()
          {
              return this.strAllTotalHoldDuration;
          }
          
          
          /**
           * 設定總保留時間
           *
           * @param int 設定總保留時間
           **/
          public void setIntAllTotalHoldDuration(int intAllTotalHoldDuration)
          {
        	  this.intAllTotalHoldDuration=intAllTotalHoldDuration;
  
          }
          
          /**
           * 取得全部總保留時間
           *
           * @return intAllTotalHoldDuration 全部總外撥時間
           **/
          public int getIntAllTotalHoldDuration()
          {
              return this.intAllTotalHoldDuration;
          }
          
          /**
      	 * 設定有效值機時間
      	 *
      	 * @param strALLValidWorkTime 有效值機時間
      	 **/
      	public void setStrALLValidWorkTime(String strALLValidWorkTime)
      	{
      	    if (strALLValidWorkTime == null)
      	    {
      	        this.strALLValidWorkTime = "";
      	    }
      	    else
      	    {
      	        this.strALLValidWorkTime = strALLValidWorkTime.trim();
      	    }
      	} //method setStrALLValidWorkTime

      	/**
      	 * 取得有效值機時間
      	 *
      	 * @return strALLValidWorkTime 有效值機時間
      	 **/
      	public String getStrALLValidWorkTime()
      	{
      	    return this.strALLValidWorkTime;
      	} //method getStrALLValidWorkTime

      	/**
      	 * 設定值機率
      	 *
      	 * @param strALLWorkPercent 值機率
      	 **/
      	public void setStrALLWorkPercent(String strALLWorkPercent)
      	{
      	    if (strALLWorkPercent == null)
      	    {
      	        this.strALLWorkPercent = "";
      	    }
      	    else
      	    {
      	        this.strALLWorkPercent = strALLWorkPercent.trim();
      	    }
      	} //method setStrALLWorkPercent

      	/**
      	 * 取得值機率
      	 *
      	 * @return strALLWorkPercent 值機率
      	 **/
      	public String getStrALLWorkPercent()
      	{
      	    return this.strALLWorkPercent;
      	} //method getStrALLWorkPercent

      	/**
      	 * 設定利用率
      	 *
      	 * @param strALLUsePercent 利用率
      	 **/
      	public void setStrALLUsePercent(String strALLUsePercent)
      	{
      	    if (strALLUsePercent == null)
      	    {
      	        this.strALLUsePercent = "";
      	    }
      	    else
      	    {
      	        this.strALLUsePercent = strALLUsePercent.trim();
      	    }
      	} //method setStrALLUsePercent

      	/**
      	 * 取得利用率
      	 *
      	 * @return strALLUsePercent 利用率
      	 **/
      	public String getStrALLUsePercent()
      	{
      	    return this.strALLUsePercent;
      	} //method getStrALLUsePercent

      	/**
      	 * 設定平均處理時間
      	 *
      	 * @param strALLAHT 平均處理時間
      	 **/
      	public void setStrALLAHT(String strALLAHT)
      	{
      	    if (strALLAHT == null)
      	    {
      	        this.strALLAHT = "";
      	    }
      	    else
      	    {
      	        this.strALLAHT = strALLAHT.trim();
      	    }
      	} //method setStrALLAHT

      	/**
      	 * 取得平均處理時間
      	 *
      	 * @return strALLAHT 平均處理時間
      	 **/
      	public String getStrALLAHT()
      	{
      	    return this.strALLAHT;
      	} //method getStrALLAHT

      	/**
      	 * 設定平均文書處理時間
      	 *
      	 * @param strALLACW 平均文書處理時間
      	 **/
      	public void setStrALLACW(String strALLACW)
      	{
      	    if (strALLACW == null)
      	    {
      	        this.strALLACW = "";
      	    }
      	    else
      	    {
      	        this.strALLACW = strALLACW.trim();
      	    }
      	} //method setStrALLACW

      	/**
      	 * 取得平均文書處理時間
      	 *
      	 * @return strALLACW 平均文書處理時間
      	 **/
      	public String getStrALLACW()
      	{
      	    return this.strALLACW;
      	} //method getStrALLACW

      	/**
      	 * 設定總進線數
      	 *
      	 * @param strALLCallLogAmount 總進線數
      	 **/
      	public void setStrALLCallLogAmount(String strALLCallLogAmount)
      	{
      	    if (strALLCallLogAmount == null)
      	    {
      	        this.strALLCallLogAmount = "";
      	    }
      	    else
      	    {
      	        this.strALLCallLogAmount = strALLCallLogAmount.trim();
      	    }
      	} //method setStrALLCallLogAmount

      	/**
      	 * 取得總進線數
      	 *
      	 * @return strALLCallLogAmount 總進線數
      	 **/
      	public String getStrALLCallLogAmount()
      	{
      	    return this.strALLCallLogAmount;
      	} //method getStrALLCallLogAmount

      	/**
      	 * 設定無效電話
      	 *
      	 * @param strALLNotValidPhone 無效電話
      	 **/
      	public void setStrALLNotValidPhone(String strALLNotValidPhone)
      	{
      	    if (strALLNotValidPhone == null)
      	    {
      	        this.strALLNotValidPhone = "";
      	    }
      	    else
      	    {
      	        this.strALLNotValidPhone = strALLNotValidPhone.trim();
      	    }
      	} //method setStrALLNotValidPhone

      	/**
      	 * 取得無效電話
      	 *
      	 * @return strALLNotValidPhone 無效電話
      	 **/
      	public String getStrALLNotValidPhone()
      	{
      	    return this.strALLNotValidPhone;
      	} //method getStrALLNotValidPhone

      	/**
      	 * 設定進線數(不包無效電話)
      	 *
      	 * @param intAllInbound 進線數(不包無效電話)
      	 **/
      	public void setIntAllInbound(int intAllInbound)
      	{
      	    this.intAllInbound = intAllInbound;
      	} //method setIntAllInbound

      	/**
      	 * 取得進線數(不包無效電話)
      	 *
      	 * @return intAllInbound 進線數(不包無效電話)
      	 **/
      	public int getIntAllInbound()
      	{
      	    return this.intAllInbound;
      	} //method getIntAllInbound

      	/**
      	 * 設定有效值機時間
      	 *
      	 * @param intAllValidWorkTime 有效值機時間
      	 **/
      	public void setIntAllValidWorkTime(int intAllValidWorkTime)
      	{
      	    this.intAllValidWorkTime = intAllValidWorkTime;
      	} //method setIntAllValidWorkTime

      	/**
      	 * 取得有效值機時間
      	 *
      	 * @return intAllValidWorkTime 有效值機時間
      	 **/
      	public int getIntAllValidWorkTime()
      	{
      	    return this.intAllValidWorkTime;
      	} //method getIntAllValidWorkTime

      	/**
      	 * 設定值機率
      	 *
      	 * @param doubleAllWorkPercent 值機率
      	 **/
      	public void setDoubleAllWorkPercent(double doubleAllWorkPercent)
      	{
      	    this.doubleAllWorkPercent = doubleAllWorkPercent;
      	} //method setDoubleAllWorkPercent

      	/**
      	 * 取得值機率
      	 *
      	 * @return doubleAllWorkPercent 值機率
      	 **/
      	public double getDoubleAllWorkPercent()
      	{
      	    return this.doubleAllWorkPercent;
      	} //method getDoubleAllWorkPercent

      	/**
      	 * 設定利用率
      	 *
      	 * @param doubleAllUsePercent 利用率
      	 **/
      	public void setDoubleAllUsePercent(double doubleAllUsePercent)
      	{
      	    this.doubleAllUsePercent = doubleAllUsePercent;
      	} //method setDoubleAllUsePercent

      	/**
      	 * 取得利用率
      	 *
      	 * @return doubleAllUsePercent 利用率
      	 **/
      	public double getDoubleAllUsePercent()
      	{
      	    return this.doubleAllUsePercent;
      	} //method getDoubleAllUsePercent

      	/**
      	 * 設定平均處理時間
      	 *
      	 * @param intAllAHT 平均處理時間
      	 **/
      	public void setIntAllAHT(int intAllAHT)
      	{
      	    this.intAllAHT = intAllAHT;
      	} //method setIntAllAHT

      	/**
      	 * 取得平均處理時間
      	 *
      	 * @return intAllAHT 平均處理時間
      	 **/
      	public int getIntAllAHT()
      	{
      	    return this.intAllAHT;
      	} //method getIntAllAHT

      	/**
      	 * 設定平均文書處理時間
      	 *
      	 * @param intAllACW 平均文書處理時間
      	 **/
      	public void setIntAllACW(int intAllACW)
      	{
      	    this.intAllACW = intAllACW;
      	} //method setIntAllACW

      	/**
      	 * 取得平均文書處理時間
      	 *
      	 * @return intAllACW 平均文書處理時間
      	 **/
      	public int getIntAllACW()
      	{
      	    return this.intAllACW;
      	} //method getIntAllACW

      	/**
      	 * 設定總進線數
      	 *
      	 * @param intAllCallLogAmount 總進線數
      	 **/
      	public void setIntAllCallLogAmount(int intAllCallLogAmount)
      	{
      	    this.intAllCallLogAmount = intAllCallLogAmount;
      	} //method setIntAllCallLogAmount

      	/**
      	 * 取得總進線數
      	 *
      	 * @return intAllCallLogAmount 總進線數
      	 **/
      	public int getIntAllCallLogAmount()
      	{
      	    return this.intAllCallLogAmount;
      	} //method getIntAllCallLogAmount

      	/**
      	 * 設定無效電話
      	 *
      	 * @param intAllNotValidPhone 無效電話
      	 **/
      	public void setIntAllNotValidPhone(int intAllNotValidPhone)
      	{
      	    this.intAllNotValidPhone = intAllNotValidPhone;
      	} //method setIntAllNotValidPhone

      	/**
      	 * 取得無效電話
      	 *
      	 * @return intAllNotValidPhone 無效電話
      	 **/
      	public int getIntAllNotValidPhone()
      	{
      	    return this.intAllNotValidPhone;
      	} //method getIntAllNotValidPhone


    /**
     * 設定全部暫時離席_code0
     *
     * @param allTotalNotReadyDuration_0 全部暫時離席_code0
     **/
    public void setAllTotalNotReadyDuration_0(String allTotalNotReadyDuration_0)
    {
        this.allTotalNotReadyDuration_0 = allTotalNotReadyDuration_0;
    }

    /**
     * 取得全部暫時離席_code0
     *
     * @return allTotalNotReadyDuration_0 全部暫時離席_code0
     **/
    public String getAllTotalNotReadyDuration_0()
    {
        return this.allTotalNotReadyDuration_0;
    }

    /**
     * 設定全部暫時離席_code1
     *
     * @param allTotalNotReadyDuration_1 全部暫時離席_code1
     **/
    public void setAllTotalNotReadyDuration_1(String allTotalNotReadyDuration_1)
    {
        this.allTotalNotReadyDuration_1 = allTotalNotReadyDuration_1;
    }

    /**
     * 取得全部暫時離席_code1
     *
     * @return allTotalNotReadyDuration_1 全部暫時離席_code1
     **/
    public String getAllTotalNotReadyDuration_1()
    {
        return this.allTotalNotReadyDuration_1;
    }

    /**
     * 設定全部暫時離席_code2
     *
     * @param allTotalNotReadyDuration_2 全部暫時離席_code2
     **/
    public void setAllTotalNotReadyDuration_2(String allTotalNotReadyDuration_2)
    {
        this.allTotalNotReadyDuration_2 = allTotalNotReadyDuration_2;
    }

    /**
     * 取得全部暫時離席_code2
     *
     * @return allTotalNotReadyDuration_2 全部暫時離席_code2
     **/
    public String getAllTotalNotReadyDuration_2()
    {
        return this.allTotalNotReadyDuration_2;
    }

    /**
     * 設定全部暫時離席_code3
     *
     * @param allTotalNotReadyDuration_3 全部暫時離席_code3
     **/
    public void setAllTotalNotReadyDuration_3(String allTotalNotReadyDuration_3)
    {
        this.allTotalNotReadyDuration_3 = allTotalNotReadyDuration_3;
    }

    /**
     * 取得全部暫時離席_code3
     *
     * @return allTotalNotReadyDuration_3 全部暫時離席_code3
     **/
    public String getAllTotalNotReadyDuration_3()
    {
        return this.allTotalNotReadyDuration_3;
    }

    /**
     * 設定全部暫時離席_code4
     *
     * @param allTotalNotReadyDuration_4 全部暫時離席_code4
     **/
    public void setAllTotalNotReadyDuration_4(String allTotalNotReadyDuration_4)
    {
        this.allTotalNotReadyDuration_4 = allTotalNotReadyDuration_4;
    }

    /**
     * 取得全部暫時離席_code4
     *
     * @return allTotalNotReadyDuration_4 全部暫時離席_code4
     **/
    public String getAllTotalNotReadyDuration_4()
    {
        return this.allTotalNotReadyDuration_4;
    }

    /**
     * 設定全部暫時離席_code5
     *
     * @param allTotalNotReadyDuration_5 全部暫時離席_code5
     **/
    public void setAllTotalNotReadyDuration_5(String allTotalNotReadyDuration_5)
    {
        this.allTotalNotReadyDuration_5 = allTotalNotReadyDuration_5;
    }

    /**
     * 取得全部暫時離席_code5
     *
     * @return allTotalNotReadyDuration_5 全部暫時離席_code5
     **/
    public String getAllTotalNotReadyDuration_5()
    {
        return this.allTotalNotReadyDuration_5;
    }

    /**
     * 設定全部暫時離席_code6
     *
     * @param allTotalNotReadyDuration_6 全部暫時離席_code6
     **/
    public void setAllTotalNotReadyDuration_6(String allTotalNotReadyDuration_6)
    {
        this.allTotalNotReadyDuration_6 = allTotalNotReadyDuration_6;
    }

    /**
     * 取得全部暫時離席_code6
     *
     * @return allTotalNotReadyDuration_6 全部暫時離席_code6
     **/
    public String getAllTotalNotReadyDuration_6()
    {
        return this.allTotalNotReadyDuration_6;
    }

    /**
     * 設定全部暫時離席_code7
     *
     * @param allTotalNotReadyDuration_7 全部暫時離席_code7
     **/
    public void setAllTotalNotReadyDuration_7(String allTotalNotReadyDuration_7)
    {
        this.allTotalNotReadyDuration_7 = allTotalNotReadyDuration_7;
    }

    /**
     * 取得全部暫時離席_code7
     *
     * @return allTotalNotReadyDuration_7 全部暫時離席_code7
     **/
    public String getAllTotalNotReadyDuration_7()
    {
        return this.allTotalNotReadyDuration_7;
    }

    /**
     * 設定全部暫時離席_code8
     *
     * @param allTotalNotReadyDuration_8 全部暫時離席_code8
     **/
    public void setAllTotalNotReadyDuration_8(String allTotalNotReadyDuration_8)
    {
        this.allTotalNotReadyDuration_8 = allTotalNotReadyDuration_8;
    }

    /**
     * 取得全部暫時離席_code8
     *
     * @return allTotalNotReadyDuration_8 全部暫時離席_code8
     **/
    public String getAllTotalNotReadyDuration_8()
    {
        return this.allTotalNotReadyDuration_8;
    }

    /**
     * 設定全部暫時離席_code9
     *
     * @param allTotalNotReadyDuration_9 全部暫時離席_code9
     **/
    public void setAllTotalNotReadyDuration_9(String allTotalNotReadyDuration_9)
    {
        this.allTotalNotReadyDuration_9 = allTotalNotReadyDuration_9;
    }

    /**
     * 取得全部暫時離席_code9
     *
     * @return allTotalNotReadyDuration_9 全部暫時離席_code9
     **/
    public String getAllTotalNotReadyDuration_9()
    {
        return this.allTotalNotReadyDuration_9;
    }

    /**
     * 設定全部暫時離席_code10
     *
     * @param allTotalNotReadyDuration_10 全部暫時離席_code10
     **/
    public void setAllTotalNotReadyDuration_10(String allTotalNotReadyDuration_10)
    {
        this.allTotalNotReadyDuration_10 = allTotalNotReadyDuration_10;
    }

    /**
     * 取得全部暫時離席_code10
     *
     * @return allTotalNotReadyDuration_10 全部暫時離席_code10
     **/
    public String getAllTotalNotReadyDuration_10()
    {
        return this.allTotalNotReadyDuration_10;
    }

    /**
     * 設定全部暫時離席百分比_code0
     *
     * @param intAllTotalNotReadyDurationPercent_0 全部暫時離席百分比_code0
     **/
    public void setIntAllTotalNotReadyDurationPercent_0(double intAllTotalNotReadyDurationPercent_0)
    {
        this.intAllTotalNotReadyDurationPercent_0 = intAllTotalNotReadyDurationPercent_0;
    }

    /**
     * 取得全部暫時離席百分比_code0
     *
     * @return intAllTotalNotReadyDurationPercent_0 全部暫時離席百分比_code0
     **/
    public double getIntAllTotalNotReadyDurationPercent_0()
    {
        return this.intAllTotalNotReadyDurationPercent_0;
    }

    /**
     * 設定全部暫時離席百分比_code1
     *
     * @param intAllTotalNotReadyDurationPercent_1 全部暫時離席百分比_code1
     **/
    public void setIntAllTotalNotReadyDurationPercent_1(double intAllTotalNotReadyDurationPercent_1)
    {
        this.intAllTotalNotReadyDurationPercent_1 = intAllTotalNotReadyDurationPercent_1;
    }

    /**
     * 取得全部暫時離席百分比_code1
     *
     * @return intAllTotalNotReadyDurationPercent_1 全部暫時離席百分比_code1
     **/
    public double getIntAllTotalNotReadyDurationPercent_1()
    {
        return this.intAllTotalNotReadyDurationPercent_1;
    }

    /**
     * 設定全部暫時離席百分比_code2
     *
     * @param intAllTotalNotReadyDurationPercent_2 全部暫時離席百分比_code2
     **/
    public void setIntAllTotalNotReadyDurationPercent_2(double intAllTotalNotReadyDurationPercent_2)
    {
        this.intAllTotalNotReadyDurationPercent_2 = intAllTotalNotReadyDurationPercent_2;
    }

    /**
     * 取得全部暫時離席百分比_code2
     *
     * @return intAllTotalNotReadyDurationPercent_2 全部暫時離席百分比_code2
     **/
    public double getIntAllTotalNotReadyDurationPercent_2()
    {
        return this.intAllTotalNotReadyDurationPercent_2;
    }

    /**
     * 設定全部暫時離席百分比_code3
     *
     * @param intAllTotalNotReadyDurationPercent_3 全部暫時離席百分比_code3
     **/
    public void setIntAllTotalNotReadyDurationPercent_3(double intAllTotalNotReadyDurationPercent_3)
    {
        this.intAllTotalNotReadyDurationPercent_3 = intAllTotalNotReadyDurationPercent_3;
    }

    /**
     * 取得全部暫時離席百分比_code3
     *
     * @return intAllTotalNotReadyDurationPercent_3 全部暫時離席百分比_code3
     **/
    public double getIntAllTotalNotReadyDurationPercent_3()
    {
        return this.intAllTotalNotReadyDurationPercent_3;
    }

    /**
     * 設定全部暫時離席百分比_code4
     *
     * @param intAllTotalNotReadyDurationPercent_4 全部暫時離席百分比_code4
     **/
    public void setIntAllTotalNotReadyDurationPercent_4(double intAllTotalNotReadyDurationPercent_4)
    {
        this.intAllTotalNotReadyDurationPercent_4 = intAllTotalNotReadyDurationPercent_4;
    }

    /**
     * 取得全部暫時離席百分比_code4
     *
     * @return intAllTotalNotReadyDurationPercent_4 全部暫時離席百分比_code4
     **/
    public double getIntAllTotalNotReadyDurationPercent_4()
    {
        return this.intAllTotalNotReadyDurationPercent_4;
    }

    /**
     * 設定全部暫時離席百分比_code5
     *
     * @param intAllTotalNotReadyDurationPercent_5 全部暫時離席百分比_code5
     **/
    public void setIntAllTotalNotReadyDurationPercent_5(double intAllTotalNotReadyDurationPercent_5)
    {
        this.intAllTotalNotReadyDurationPercent_5 = intAllTotalNotReadyDurationPercent_5;
    }

    /**
     * 取得全部暫時離席百分比_code5
     *
     * @return intAllTotalNotReadyDurationPercent_5 全部暫時離席百分比_code5
     **/
    public double getIntAllTotalNotReadyDurationPercent_5()
    {
        return this.intAllTotalNotReadyDurationPercent_5;
    }

    /**
     * 設定全部暫時離席百分比_code6
     *
     * @param intAllTotalNotReadyDurationPercent_6 全部暫時離席百分比_code6
     **/
    public void setIntAllTotalNotReadyDurationPercent_6(double intAllTotalNotReadyDurationPercent_6)
    {
        this.intAllTotalNotReadyDurationPercent_6 = intAllTotalNotReadyDurationPercent_6;
    }

    /**
     * 取得全部暫時離席百分比_code6
     *
     * @return intAllTotalNotReadyDurationPercent_6 全部暫時離席百分比_code6
     **/
    public double getIntAllTotalNotReadyDurationPercent_6()
    {
        return this.intAllTotalNotReadyDurationPercent_6;
    }

    /**
     * 設定全部暫時離席百分比_code7
     *
     * @param intAllTotalNotReadyDurationPercent_7 全部暫時離席百分比_code7
     **/
    public void setIntAllTotalNotReadyDurationPercent_7(double intAllTotalNotReadyDurationPercent_7)
    {
        this.intAllTotalNotReadyDurationPercent_7 = intAllTotalNotReadyDurationPercent_7;
    }

    /**
     * 取得全部暫時離席百分比_code7
     *
     * @return intAllTotalNotReadyDurationPercent_7 全部暫時離席百分比_code7
     **/
    public double getIntAllTotalNotReadyDurationPercent_7()
    {
        return this.intAllTotalNotReadyDurationPercent_7;
    }

    /**
     * 設定全部暫時離席百分比_code8
     *
     * @param intAllTotalNotReadyDurationPercent_8 全部暫時離席百分比_code8
     **/
    public void setIntAllTotalNotReadyDurationPercent_8(double intAllTotalNotReadyDurationPercent_8)
    {
        this.intAllTotalNotReadyDurationPercent_8 = intAllTotalNotReadyDurationPercent_8;
    }

    /**
     * 取得全部暫時離席百分比_code8
     *
     * @return intAllTotalNotReadyDurationPercent_8 全部暫時離席百分比_code8
     **/
    public double getIntAllTotalNotReadyDurationPercent_8()
    {
        return this.intAllTotalNotReadyDurationPercent_8;
    }

    /**
     * 設定全部暫時離席百分比_code9
     *
     * @param intAllTotalNotReadyDurationPercent_9 全部暫時離席百分比_code9
     **/
    public void setIntAllTotalNotReadyDurationPercent_9(double intAllTotalNotReadyDurationPercent_9)
    {
        this.intAllTotalNotReadyDurationPercent_9 = intAllTotalNotReadyDurationPercent_9;
    }

    /**
     * 取得全部暫時離席百分比_code9
     *
     * @return intAllTotalNotReadyDurationPercent_9 全部暫時離席百分比_code9
     **/
    public double getIntAllTotalNotReadyDurationPercent_9()
    {
        return this.intAllTotalNotReadyDurationPercent_9;
    }

    /**
     * 設定全部暫時離席百分比_code10
     *
     * @param intAllTotalNotReadyDurationPercent_10 全部暫時離席百分比_code10
     **/
    public void setIntAllTotalNotReadyDurationPercent_10(double intAllTotalNotReadyDurationPercent_10)
    {
        this.intAllTotalNotReadyDurationPercent_10 = intAllTotalNotReadyDurationPercent_10;
    }

    /**
     * 取得全部暫時離席百分比_code10
     *
     * @return intAllTotalNotReadyDurationPercent_10 全部暫時離席百分比_code10
     **/
    public double getIntAllTotalNotReadyDurationPercent_10()
    {
        return this.intAllTotalNotReadyDurationPercent_10;
    }
          
         
}
