 /****************************************************************************
  *
  * Copyright (c) 2008 ESound Tech. All Rights Reserved.
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
  *     File name:       AgentEfficientDataObject.java
  *
  *     History:
  *     Date               Author                  Comments
  *     -----------------------------------------------------------------------
  *     DEC 11, 2009      Mars               Initial Release
  *****************************************************************************/
package com.consilium.excel.models.agentefficient;

import java.util.Calendar;

/**
 * <code>AgentEfficientObject</code>
 * 人員績效統計物件
 * @author      Mars
 * @version     1.0
 **/
 public class AgentEfficientDataObject  implements Comparable<AgentEfficientDataObject>
 {
     /**
      *開始日期年
      **/
     protected int startYear;

     /**
      *開始日期月
      **/
     protected int startMon;

     /**
      *開始日期日
      **/
     protected int startDay;

     /**
      *結束日期年
      **/
     protected int endYear;

     /**
      *結束日期月
      **/
     protected int endMon;

     /**
      *結束日期日
      **/
     protected int endDay;

     /**
      *屬於年的第幾週
      **/
     protected int weekNum;
     
     /**
      *屬於月的第幾週
      **/
     protected int weekOfMon;

     /**
      *資料時間
      **/
     protected String beginTime = "";

     /**
      *排序用
      **/
     protected Integer sortNum;

     /**
      *人員姓名
      **/
     protected String agentName = "";
     
  
     protected String userId = "";

     /**
      *等待時間(String)
      **/
     protected String totalLoginReadyIdleDuration = "";
    
     /**
      *離席時間(String)
      **/
     protected String totalLoginNotReadyIdleDuration = "";
     

     /**
      *文書作業(String)
      **/
     protected String totalLoginPcpIdleDuration = "";

     /**
      *通話時間(String)
      **/
     protected String totalDcpInboundCallsDuration = "";
    

     /**
      *總登入時間
      **/
     protected String totalLoginDuration = "";
     
     /**
      *總外撥時間
      **/
     protected String totalPrivateOutBoundDuration = "";
     
     /**
      *總保留時間
      **/
     protected String totalHoldDuration = "";

     /**
      *總受理通數
      **/
     protected String nbrDcpInboundCallsAnswered = "";

     /**
      *平均每通秒數
      **/
     protected double averageEveryCallSec;
     
     /**
      *平均每通秒數
      **/
     protected String strAverageEveryCallSec="00:00:00";
     
     /**
      *記錄資是否從資料庫來 0為資料庫
      **/
     protected int DBflag = 0;
     
     /**
      *資料日期(顯示於報表用)
      **/
     protected String dataDate = "";

     /**
      *查詢開始日
      **/
     protected Calendar startQueryDate;
     
     /**
      *查詢結束日
      **/
     protected Calendar endQueryDate;
     
     
     /**
      *等待時間(int)
      **/
     protected int intTotalLoginReadyIdleDuration;

     /**
      *離席時間(int)
      **/
     protected int intTotalLoginNotReadyIdleDuration;

     /**
      *文書作業(int)
      **/
     protected int intTotalLoginPcpIdleDuration;

     /**
      *談話時間(int)
      **/
     protected int intTotalDcpInboundCallsDuration;

     /**
      *總登入時間(int)
      **/
     protected int intTotalLoginDuration;
     
     
     /**
      *總外撥時間(int)
      **/
     protected int intTotalPrivateOutBoundDuration;

     /**
      *總受理通數(int)
      **/
     protected int intTnbrDcpInboundCallsAnswered;
     
     
     /**
      *總保留時間(int)
      **/
     protected int intTotalHoldDuration;
     
     
     /**
 	 * 有效值機時間
 	 **/
 	private String validWorkTime = "";

 	/**
 	 * 值機率
 	 **/
 	private String workPercent = "";

 	/**
 	 * 利用率
 	 **/
 	private String usePercent = "";

 	/**
 	 * 平均處理時間
 	 **/
 	private String AHT = "00:00:00";

 	/**
 	 * 平均文書處理時間
 	 **/
 	private String ACW = "00:00:00";

 	/**
 	 * 總進線數
 	 **/
 	private String callLogAmount = "0";

 	/**
 	 * 無效電話
 	 **/
 	private String notValidPhone = "0";

 	/**
 	 * 進線數(不包無效電話)
 	 **/
 	private int inbound;

 	/**
 	 * 有效值機時間
 	 **/
 	private int intValidWorkTime;

 	/**
 	 * 值機率
 	 **/
 	private double doubleWorkPercent;

 	/**
 	 * 利用率
 	 **/
 	private double doubleUsePercent;

 	/**
 	 * 平均處理時間
 	 **/
 	private int intAHT;

 	/**
 	 * 平均文書處理時間
 	 **/
 	private int intACW;

 	/**
 	 * 總進線數
 	 **/
 	private int intCallLogAmount;

 	/**
 	 * 無效電話
 	 **/
 	private int intNotValidPhone;

 	/**
 	 * 進線數(不包無效電話)
 	 **/
 	private int intInbound;
     
     /**
      * 用於判斷是否由去年最後一週到今年第一週
      */
     static int tmpFirstWeekNum=0;
     

     /**
      * 最早登入時間
      */
     protected String firstLoginTime = "";
     
     /**
      * 最後登出時間
      */
     protected String lastLogoutTime = "";


     /**
      *暫時離席_code0
      **/
     protected String totalNotReadyDuration_0 = "";

     /**
      *暫時離席_code1
      **/
     protected String totalNotReadyDuration_1 = "";

     /**
      *暫時離席_code2
      **/
     protected String totalNotReadyDuration_2 = "";

     /**
      *暫時離席_code3
      **/
     protected String totalNotReadyDuration_3 = "";

     /**
      *暫時離席_code4
      **/
     protected String totalNotReadyDuration_4 = "";

     /**
      *暫時離席_code5
      **/
     protected String totalNotReadyDuration_5 = "";

     /**
      *暫時離席_code6
      **/
     protected String totalNotReadyDuration_6 = "";

     /**
      *暫時離席_code7
      **/
     protected String totalNotReadyDuration_7 = "";

     /**
      *暫時離席_code8
      **/
     protected String totalNotReadyDuration_8 = "";

     /**
      *暫時離席_code9
      **/
     protected String totalNotReadyDuration_9 = "";

     /**
      *暫時離席_code10
      **/
     protected String totalNotReadyDuration_10 = "";

     /**
      *暫時離席_code0(int)
      **/
     protected int intTotalNotReadyDuration_0;

     /**
      *暫時離席_code1(int)
      **/
     protected int intTotalNotReadyDuration_1;

     /**
      *暫時離席_code2(int)
      **/
     protected int intTotalNotReadyDuration_2;

     /**
      *暫時離席_code3(int)
      **/
     protected int intTotalNotReadyDuration_3;

     /**
      *暫時離席_code4(int)
      **/
     protected int intTotalNotReadyDuration_4;

     /**
      *暫時離席_code5(int)
      **/
     protected int intTotalNotReadyDuration_5;

     /**
      *暫時離席_code6(int)
      **/
     protected int intTotalNotReadyDuration_6;

     /**
      *暫時離席_code7(int)
      **/
     protected int intTotalNotReadyDuration_7;

     /**
      *暫時離席_code8(int)
      **/
     protected int intTotalNotReadyDuration_8;

     /**
      *暫時離席_code9(int)
      **/
     protected int intTotalNotReadyDuration_9;

     /**
      *暫時離席_code10(int)
      **/
     protected int intTotalNotReadyDuration_10;

     public AgentEfficientDataObject()
     {
     
     }
     
     
     public void setDataDate(String dataDate)
     {
        this.dataDate=dataDate;
     }
     
     public void setStartQueryDate(Calendar startQueryDate)
     {
        this.startQueryDate=startQueryDate;
     }
     
     public void setEndQueryDate(Calendar endQueryDate)
     {
        this.endQueryDate=endQueryDate;
     }
    // setWeekDay()是用來設定某日期屬於的那週的起始和結束日期，比方說2009/12/08是12月的第二週，起始日為2009/12/06，結束日為2009/12/12
    public void setWeekDay()
    {
        
        int fYear=0;;
        int fMon=0;
        int fDay=0;
        int eYear=0;
        int eMon=0;
        int eDay=0;
        int fWeek=0;
        
        int monArray[]=new int[13];
        monArray[0]=0;
        monArray[1]=31;
        monArray[2]=monArray[1]+29;
        monArray[3]=monArray[2]+31;
        monArray[4]=monArray[3]+30;
        monArray[5]=monArray[4]+31;
        monArray[6]=monArray[5]+30;
        monArray[7]=monArray[6]+31;
        monArray[8]=monArray[7]+31;
        monArray[9]=monArray[8]+30;
        monArray[10]=monArray[9]+31;
        monArray[11]=monArray[10]+30;
        monArray[12]=monArray[11]+31;
        //當weekFlag=1時，年就要加1
        int weekFlag=0;
        
        
        // 因為一年的最後一週如果為53週時，53週會和明年的第一週是一樣的，53週和第一週是重覆的週，日期計算會有問題，所以統一計為明年第一週
       
        if(this.weekNum==53)
        {
           
            this.weekNum=1;
            int tmpYear=Integer.parseInt(this.beginTime)+1;
            this.beginTime=""+tmpYear;
            weekFlag=1;
        }
        
        // 這是判斷是否由52週到第一週，如果查詢的日期剛好就在某年第一週，那年不用加1，如果是由52週到第一週，則年需要加1
        if(this.weekNum==52)
        {
            tmpFirstWeekNum=52;
        }
        
        if(tmpFirstWeekNum>this.weekNum)
        {
            weekFlag=1;
            tmpFirstWeekNum=0;
        }
        
        
       
       
       
       if(this.weekNum==1)
       {
          
         
           Calendar tmpData = Calendar.getInstance();
           Calendar tmpData2 = Calendar.getInstance();
           tmpData2.set(Calendar.YEAR,Integer.parseInt(this.beginTime));
           tmpData2.set(Calendar.WEEK_OF_YEAR,this.weekNum);
           tmpData2.set(Calendar.DAY_OF_WEEK, tmpData.getFirstDayOfWeek()+1);
           
           //這是用來判斷  例如像2009年第一週，這週第一天是2008/12/28  最後一天是2009/1/3日，當一週的第一和最後一天在不同年度時，年要加1
           int year1=tmpData2.get(Calendar.YEAR);
           tmpData2.add(Calendar.DAY_OF_WEEK, 6);
          
           int year2=tmpData2.get(Calendar.YEAR);
           if(year1!=year2)
           {
               weekFlag=1;
           }
           if(this.DBflag==0)
           {
               
               tmpData.set(Calendar.YEAR,Integer.parseInt(this.beginTime));
           }
           else
           {
               if(weekFlag==1)
               {
                   //由53週變第一週，年份要加一
                   
                   tmpData.set(Calendar.YEAR,Integer.parseInt(this.beginTime)+1);
                   weekFlag=0;
                   
               }
               else
               {
                   tmpData.set(Calendar.YEAR,Integer.parseInt(this.beginTime));
                  // tmpData.set(Calendar.YEAR,Integer.parseInt(this.beginTime));
                   
               }
               
  
           }
           
           //第一週有時起始日和結束日會亂掉，所以先加一週，再減7天回來
           
           this.weekNum=weekNum+1;
           tmpData.set(Calendar.WEEK_OF_YEAR,this.weekNum);
           tmpData.set(Calendar.DAY_OF_WEEK, tmpData.getFirstDayOfWeek()+1);
           
           tmpData.add(Calendar.DAY_OF_WEEK,-7);
          
           
           fYear=tmpData.get(Calendar.YEAR);
           fMon=tmpData.get(Calendar.MONTH)+1;
           fDay=tmpData.get(Calendar.DATE);
           this.sortNum=fYear*365+monArray[fMon-1]+fDay;
           fWeek=tmpData.get(Calendar.WEEK_OF_MONTH);
           this.weekOfMon=fWeek;
           
           tmpData.add(Calendar.DAY_OF_WEEK, 6);
           
           eYear=tmpData.get(Calendar.YEAR);
           eMon=tmpData.get(Calendar.MONTH)+1;
           eDay=tmpData.get(Calendar.DATE);

           this.startYear=fYear;
           this.startMon=fMon;
           this.startDay=fDay;
          

           this.endYear=eYear;
           this.endMon=eMon;
           this.endDay=eDay;
       }
       else
       {
           
            Calendar tmpData = Calendar.getInstance();
            tmpData.set(Calendar.YEAR,Integer.parseInt(this.beginTime));
           
            tmpData.set(Calendar.WEEK_OF_YEAR,this.weekNum);
            tmpData.set(Calendar.DAY_OF_WEEK, tmpData.getFirstDayOfWeek()+1);

           
            fYear=tmpData.get(Calendar.YEAR);
            fMon=tmpData.get(Calendar.MONTH)+1;
            fDay=tmpData.get(Calendar.DATE);
        
            this.sortNum=fYear*365+monArray[fMon-1]+fDay;

            fWeek=tmpData.get(Calendar.WEEK_OF_MONTH);
            this.weekOfMon=fWeek;
            
            tmpData.add(Calendar.DAY_OF_WEEK, 6);
            eYear=tmpData.get(Calendar.YEAR);
            eMon=tmpData.get(Calendar.MONTH)+1;
            eDay=tmpData.get(Calendar.DATE);
            
            
           this.startYear=fYear;
           this.startMon=fMon;
           this.startDay=fDay;
         
           this.endYear=eYear;
           this.endMon=eMon;
           this.endDay=eDay;

       }

    }
     
      public void setBeetWeekDays()
      {
          String strStartDay="";
          String StrendDay="";
          String strStartMon="";
          
          if(this.startDay<10)
          {
              strStartDay="0"+this.startDay;
          }
          else
          {
              strStartDay=""+this.startDay;                
          }
          
          if(this.endDay<10)
          {
              StrendDay="0"+this.endDay;
          }
          else
          {
              StrendDay=""+this.endDay;                
          }
          
          if(this.startMon<10)
          {
              strStartMon="0"+this.startMon;;
          }
          else
          {
              strStartMon=""+this.startMon;;                
          }
          
          this.dataDate=this.startYear+"年"+strStartMon+"月 第"+this.weekOfMon+"週 "+strStartDay+"~"+StrendDay+"日";
      }
     
     
     /**
      * 設定資料是否由資料庫來的
      *
      * @param DBflag 資料是否由資料庫來的 0為資料庫
      **/
      public void setDBflag(int DBflag)
      {
          this.DBflag = DBflag;
      }
     
     /**
      * 設定開始日期年
      *
      * @param startYear 開始日期年
      **/
      public void setStartYear(int startYear)
      {
          this.startYear = startYear;
      }

     /**
      * 設定開始日期月
      *
      * @param startMon 開始日期月
      **/
      public void setStartMon(int startMon)
      {
          this.startMon = startMon;
      }

     /**
      * 設定開始日期日
      *
      * @param startDay 開始日期日
      **/
      public void setStartDay(int startDay)
      {
          this.startDay = startDay;
      }

     /**
      * 設定結束日期年
      *
      * @param endYear 結束日期年
      **/
      public void setEndYear(int endYear)
      {
          this.endYear = endYear;
      }

     /**
      * 設定結束日期月
      *
      * @param endMon 結束日期月
      **/
      public void setEndMon(int endMon)
      {
          this.endMon = endMon;
      }

     /**
      * 設定結束日期日
      *
      * @param endDay 結束日期日
      **/
      public void setEndDay(int endDay)
      {
          this.endDay = endDay;
      }
      
     /**
      * 設定月的第幾週
      *
      * @param weekOfMon 月的第幾週
      **/
      public void setWeekOfMon(int weekOfMon)
      {
          this.weekOfMon = weekOfMon;
      }

     /**
      * 設定屬於第幾週
      *
      * @param weekNum 屬於第幾週
      **/
      public void setWeekNum(int weekNum)
      {
          this.weekNum = weekNum;
      }

     /**
      * 設定資料時間
      *
      * @param beginTime 資料時間
      **/
      public void setBeginTime(String beginTime)
      {
          if (beginTime == null)
          {
              this.beginTime = "";
          }
          else
          {
              this.beginTime = beginTime.trim();
          }
      }

     /**
      * 設定排序用
      *
      * @param sortNum 排序用
      **/
      public void setSortNum(Integer sortNum)
      {
          this.sortNum = sortNum;
      }

     /**
      * 設定人員姓名
      *
      * @param agentName 人員姓名
      **/
      public void setAgentName(String agentName)
      {
          if (agentName == null)
          {
              this.agentName = "";
          }
          else
          {
              this.agentName = agentName.trim();
          }
      }

     /**
      * 設定等待時間
      *
      * @param totalLoginReadyIdleDuration 等待時間
      **/
      public void setTotalLoginReadyIdleDuration(String totalLoginReadyIdleDuration)
      {
          if (totalLoginReadyIdleDuration == null||"".equals(totalLoginReadyIdleDuration))
          {
              this.totalLoginReadyIdleDuration = "0";
          }
          else
          {
              this.totalLoginReadyIdleDuration = totalLoginReadyIdleDuration.trim();
              
          }
      }

     /**
      * 設定離席時間
      *
      * @param totalLoginNotReadyIdleDuration 離席時間
      **/
      public void setTotalLoginNotReadyIdleDuration(String totalLoginNotReadyIdleDuration)
      {
          if (totalLoginNotReadyIdleDuration == null||"".equals(totalLoginNotReadyIdleDuration))
          {
              this.totalLoginNotReadyIdleDuration = "0";
          }
          else
          {
              this.totalLoginNotReadyIdleDuration = totalLoginNotReadyIdleDuration.trim();
             
          }
      }

     /**
      * 設定文書作業
      *
      * @param totalLoginPcpIdleDuration 文書作業
      **/
      public void setTotalLoginPcpIdleDuration(String totalLoginPcpIdleDuration)
      {
          if (totalLoginPcpIdleDuration == null||"".equals(totalLoginPcpIdleDuration))
          {
              this.totalLoginPcpIdleDuration = "";
          }
          else
          {
              this.totalLoginPcpIdleDuration = totalLoginPcpIdleDuration.trim();
              
          }
      }

     /**
      * 設定談話時間
      *
      * @param totalDcpInboundCallsDuration 談話時間
      **/
      public void setTotalDcpInboundCallsDuration(String totalDcpInboundCallsDuration)
      {
          if (totalDcpInboundCallsDuration == null||"".equals(totalDcpInboundCallsDuration))
          {
              this.totalDcpInboundCallsDuration = "0";
          }
          else
          {
              this.totalDcpInboundCallsDuration = totalDcpInboundCallsDuration.trim();
              
          }
      }

     /**
      * 設定總登入時間
      *
      * @param totalLoginDuration 總登入時間
      **/
      public void setTotalLoginDuration(String totalLoginDuration)
      {
          if (totalLoginDuration == null||"".equals(totalLoginDuration))
          {
              this.totalLoginDuration = "";
          }
          else
          {
              this.totalLoginDuration = totalLoginDuration.trim();
          }
      }

     /**
      * 設定總受理通數
      *
      * @param nbrDcpInboundCallsAnswered 總受理通數
      **/
      public void setNbrDcpInboundCallsAnswered(String nbrDcpInboundCallsAnswered)
      {
          if (nbrDcpInboundCallsAnswered == null||"".equals(nbrDcpInboundCallsAnswered))
          {
              this.nbrDcpInboundCallsAnswered = "";
          }
          else
          {
              this.nbrDcpInboundCallsAnswered = nbrDcpInboundCallsAnswered.trim();
          }
      }

     /**
      * 設定平均每通秒數
      *
      * @param averageEveryCallSec 平均每通秒數
      **/
      public void setAverageEveryCallSec(double averageEveryCallSec)
      {
              this.averageEveryCallSec = averageEveryCallSec;  
      }
      
     /**
      * 設定平均每通秒數
      *
      * @param strAverageEveryCallSec 平均每通秒數
      **/
      public void setStrAverageEveryCallSec(String strAverageEveryCallSec)
      {
              this.strAverageEveryCallSec = strAverageEveryCallSec;  
      }
      

      /**
       * 設定資料時間
       *
       * @param firstLoginTime 資料時間
       **/
       public void setFirstLoginTime(String firstLoginTime)
       {
           if (firstLoginTime == null)
           {
               this.firstLoginTime = "";
           }
           else
           {
               this.firstLoginTime = firstLoginTime.trim();
           }
       }
      
       /**
        * 設定資料時間
        *
        * @param lastLogoutTime 資料時間
        **/
        public void setLastLogoutTime(String lastLogoutTime)
        {
            if (lastLogoutTime == null)
            {
                this.lastLogoutTime = "";
            }
            else
            {
                this.lastLogoutTime = lastLogoutTime.trim();
            }
        }
      
     /**
      * 取得開始日期年
      *
      * @return startYear 開始日期年
      **/
      public int getStartYear()
      {
          return this.startYear;
      }

     /**
      * 取得開始日期月
      *
      * @return startMon 開始日期月
      **/
      public int getStartMon()
      {
          return this.startMon;
      }

     /**
      * 取得開始日期日
      *
      * @return startDay 開始日期日
      **/
      public int getStartDay()
      {
          return this.startDay;
      }

     /**
      * 取得結束日期年
      *
      * @return endYear 結束日期年
      **/
      public int getEndYear()
      {
          return this.endYear;
      }

     /**
      * 取得結束日期月
      *
      * @return endMon 結束日期月
      **/
      public int getEndMon()
      {
          return this.endMon;
      }

     /**
      * 取得結束日期日
      *
      * @return endDay 結束日期日
      **/
      public int getEndDay()
      {
          return this.endDay;
      }
      
     /**
      * 取得月的第幾週
      *
      * @return weekOfMon 月的第幾週
      **/
      public int getWeekOfMon()
      {
          return this.weekOfMon;
      }

     /**
      * 取得屬於第幾週
      *
      * @return weekNum 屬於第幾週
      **/
      public int getWeekNum()
      {
          return this.weekNum;
      }

     /**
      * 取得資料時間
      *
      * @return beginTime 資料時間
      **/
      public String getBeginTime()
      {
          return this.beginTime;
      }

     /**
      * 取得排序用
      *
      * @return sortNum 排序用
      **/
      public Integer getSortNum()
      {
          return this.sortNum;
      }

     /**
      * 取得人員姓名
      *
      * @return agentName 人員姓名
      **/
      public String getAgentName()
      {
          return this.agentName;
      }

     /**
      * 取得等待時間
      *
      * @return totalLoginReadyIdleDuration 等待時間
      **/
      public String getTotalLoginReadyIdleDuration()
      {
          return this.totalLoginReadyIdleDuration;
      }

     /**
      * 取得離席時間
      *
      * @return totalLoginNotReadyIdleDuration 離席時間
      **/
      public String getTotalLoginNotReadyIdleDuration()
      {
          return this.totalLoginNotReadyIdleDuration;
      }

     /**
      * 取得文書作業
      *
      * @return totalLoginPcpIdleDuration 文書作業
      **/
      public String getTotalLoginPcpIdleDuration()
      {
          return this.totalLoginPcpIdleDuration;
      }

     /**
      * 取得談話時間
      *
      * @return totalDcpInboundCallsDuration 談話時間
      **/
      public String getTotalDcpInboundCallsDuration()
      {
          return this.totalDcpInboundCallsDuration;
      }

     /**
      * 取得總登入時間
      *
      * @return totalLoginDuration 總登入時間
      **/
      public String getTotalLoginDuration()
      {
          return this.totalLoginDuration;
      }

     /**
      * 取得總受理通數
      *
      * @return nbrDcpInboundCallsAnswered 總受理通數
      **/
      public String getNbrDcpInboundCallsAnswered()
      {
          return this.nbrDcpInboundCallsAnswered;
      }

     /**
      * 取得平均每通秒數
      *
      * @return averageEveryCallSec 平均每通秒數
      **/
      public double getAverageEveryCallSec()
      {
          return this.averageEveryCallSec;
      }
      
     /**
      * 取得平均每通秒數
      *
      * @return strAverageEveryCallSec 平均每通秒數
      **/
      public String getStrAverageEveryCallSec()
      {
          return this.strAverageEveryCallSec;
      }
      
     /**
      * 取得資料是否由資料庫來的
      *
      * @param DBflag 資料是否由資料庫來的 0為資料庫
      **/
      public int getDBflag()
      {
          return this.DBflag;
      }
      
     /**
      * 取得週顯示日期
      *
      * @return averageEveryCallSec 週顯示日期
      **/
      public String getBeetWeekDays()
      {
          String strStartDay="";
          String StrendDay="";
          String strStartMon="";
         
          if(this.startDay<10)
          {
              strStartDay="0"+this.startDay;
          }
          else
          {
              strStartDay=""+this.startDay;                
          }
          
          if(this.endDay<10)
          {
              StrendDay="0"+this.endDay;
          }
          else
          {
              StrendDay=""+this.endDay;                
          }
          
          if(this.startMon<10)
          {
              strStartMon="0"+this.startMon;;
          }
          else
          {
              strStartMon=""+this.startMon;;                
          }
          
          this.dataDate=this.startYear+"年"+strStartMon+"月 第"+this.weekOfMon+"週 "+this.startDay+"~"+this.endDay+"日";
          return  this.dataDate;
      }
      
      
        public String getDataDate()
        {
            return this.dataDate;
        }
        
        public Calendar getStartQueryDate()
        {
            return this.startQueryDate;
        }
        
        public Calendar getEndQueryDate()
        {
            return this.endQueryDate;
        }
      
    
    
     /**
      * 設定等待時間
      *
      * @param intTotalLoginReadyIdleDuration 等待時間
      **/
      public void setIntTotalLoginReadyIdleDuration(int intTotalLoginReadyIdleDuration)
      {
          this.intTotalLoginReadyIdleDuration = intTotalLoginReadyIdleDuration;
      }

     /**
      * 設定離席時間
      *
      * @param intTotalLoginNotReadyIdleDuration 離席時間
      **/
      public void setIntTotalLoginNotReadyIdleDuration(int intTotalLoginNotReadyIdleDuration)
      {
          this.intTotalLoginNotReadyIdleDuration = intTotalLoginNotReadyIdleDuration;
      }

     /**
      * 設定文書作業
      *
      * @param intTotalLoginPcpIdleDuration 文書作業
      **/
      public void setIntTotalLoginPcpIdleDuration(int intTotalLoginPcpIdleDuration)
      {
          this.intTotalLoginPcpIdleDuration = intTotalLoginPcpIdleDuration;
      }

     /**
      * 設定談話時間
      *
      * @param intTotalDcpInboundCallsDuration 談話時間
      **/
      public void setIntTotalDcpInboundCallsDuration(int intTotalDcpInboundCallsDuration)
      {
          this.intTotalDcpInboundCallsDuration = intTotalDcpInboundCallsDuration;
      }

     /**
      * 設定總登入時間
      *
      * @param intTotalLoginDuration 總登入時間
      **/
      public void setIntTotalLoginDuration(int intTotalLoginDuration)
      {
          this.intTotalLoginDuration = intTotalLoginDuration;
      }

     /**
      * 設定總受理通數
      *
      * @param intTnbrDcpInboundCallsAnswered 總受理通數
      **/
      public void setIntTnbrDcpInboundCallsAnswered(int intTnbrDcpInboundCallsAnswered)
      {
          this.intTnbrDcpInboundCallsAnswered = intTnbrDcpInboundCallsAnswered;
      }
      
      
      /**
      * 設定總外撥時間
      *
      * @param intTotalLoginDuration 總外撥時間
      **/
      public void setIntTotalPrivateOutBoundDuration(int intTotalPrivateOutBoundDuration)
      {
         this.intTotalPrivateOutBoundDuration = intTotalPrivateOutBoundDuration;
      }

      /**
      * 取得總外撥時間
      *
      * @return intTotalLoginReadyIdleDuration 總外撥時間
      **/
      public int getIntTotalPrivateOutBoundDuration()
      {
    	  return this.intTotalPrivateOutBoundDuration;
      }


     /**
      * 取得等待時間
      *
      * @return intTotalLoginReadyIdleDuration 等待時間
      **/
      public int getIntTotalLoginReadyIdleDuration()
      {
          return this.intTotalLoginReadyIdleDuration;
      }

     /**
      * 取得離席時間
      *
      * @return intTotalLoginNotReadyIdleDuration 離席時間
      **/
      public int getIntTotalLoginNotReadyIdleDuration()
      {
          return this.intTotalLoginNotReadyIdleDuration;
      }

     /**
      * 取得文書作業
      *
      * @return intTotalLoginPcpIdleDuration 文書作業
      **/
      public int getIntTotalLoginPcpIdleDuration()
      {
          return this.intTotalLoginPcpIdleDuration;
      }

     /**
      * 取得談話時間
      *
      * @return intTotalDcpInboundCallsDuration 談話時間
      **/
      public int getIntTotalDcpInboundCallsDuration()
      {
          return this.intTotalDcpInboundCallsDuration;
      }

     /**
      * 取得總登入時間
      *
      * @return intTotalLoginDuration 總登入時間
      **/
      public int getIntTotalLoginDuration()
      {
          return this.intTotalLoginDuration;
      }

     /**
      * 取得總受理通數
      *
      * @return intTnbrDcpInboundCallsAnswered 總受理通數
      **/
      public int getIntTnbrDcpInboundCallsAnswered()
      {
          return this.intTnbrDcpInboundCallsAnswered;
      }
    
     //這是用來排序用的
        public int compareTo(AgentEfficientDataObject arg0) 
        {
            return this.getSortNum().compareTo(arg0.getSortNum());
        }
        
        
        /**
        * 設定總外撥時間
        *
        * @param intTotalLoginDuration 總外撥時間
        **/
        public void setTotalPrivateOutBoundDuration(String totalPrivateOutBoundDuration)
        {
           this.totalPrivateOutBoundDuration = totalPrivateOutBoundDuration;
        }

        /**
        * 取得總外撥時間
        *
        * @return intTotalLoginReadyIdleDuration 總外撥時間
        **/
        public String getTotalPrivateOutBoundDuration()
        {
        	return this.totalPrivateOutBoundDuration;
        }
        
        
        /**
         * 設定總保留時間
         *
         * @param String 設定總保留時間
         **/
        public void setTotalHoldDuration(String totalHoldDuration)
        {
        	if(totalHoldDuration==null)
        	{
        		this.totalHoldDuration="";
        	}
        	else
        	{
        		this.totalHoldDuration=totalHoldDuration;
        	}	
        }
        
        /**
         * 取得總保留時間
         *
         * @return totalHoldDuration 總保留時間
         **/
        public String getTotalHoldDuration()
        {
        	return this.totalHoldDuration;
        }
        
        
        /**
         * 設定總保留時間
         *
         * @param int 設定總保留時間
         **/
        public void setIntTotalHoldDuration(int intTotalHoldDuration)
        {
        	this.intTotalHoldDuration=intTotalHoldDuration;
        }
        
        /**
         * 取得總保留時間
         *
         * @return intTotalHoldDuration 總保留時間
         **/
        public int getIntTotalHoldDuration()
        {
        	return this.intTotalHoldDuration;
        }
        
        /**
    	 * 設定有效值機時間
    	 *
    	 * @param validWorkTime 有效值機時間
    	 **/
    	public void setValidWorkTime(String validWorkTime)
    	{
    	    if (validWorkTime == null)
    	    {
    	        this.validWorkTime = "";
    	    }
    	    else
    	    {
    	        this.validWorkTime = validWorkTime.trim();
    	    }
    	} //method setValidWorkTime

    	/**
    	 * 取得有效值機時間
    	 *
    	 * @return validWorkTime 有效值機時間
    	 **/
    	public String getValidWorkTime()
    	{
    	    return this.validWorkTime;
    	} //method getValidWorkTime

    	/**
    	 * 設定值機率
    	 *
    	 * @param workPercent 值機率
    	 **/
    	public void setWorkPercent(String workPercent)
    	{
    	    if (workPercent == null)
    	    {
    	        this.workPercent = "";
    	    }
    	    else
    	    {
    	        this.workPercent = workPercent.trim();
    	    }
    	} //method setWorkPercent

    	/**
    	 * 取得值機率
    	 *
    	 * @return workPercent 值機率
    	 **/
    	public String getWorkPercent()
    	{
    	    return this.workPercent;
    	} //method getWorkPercent

    	/**
    	 * 設定利用率
    	 *
    	 * @param usePercent 利用率
    	 **/
    	public void setUsePercent(String usePercent)
    	{
    	    if (usePercent == null)
    	    {
    	        this.usePercent = "";
    	    }
    	    else
    	    {
    	        this.usePercent = usePercent.trim();
    	    }
    	} //method setUsePercent

    	/**
    	 * 取得利用率
    	 *
    	 * @return usePercent 利用率
    	 **/
    	public String getUsePercent()
    	{
    	    return this.usePercent;
    	} //method getUsePercent

    	/**
    	 * 設定平均處理時間
    	 *
    	 * @param AHT 平均處理時間
    	 **/
    	public void setAHT(String AHT)
    	{
    	    if (AHT == null)
    	    {
    	        this.AHT = "";
    	    }
    	    else
    	    {
    	        this.AHT = AHT.trim();
    	    }
    	} //method setAHT

    	/**
    	 * 取得平均處理時間
    	 *
    	 * @return AHT 平均處理時間
    	 **/
    	public String getAHT()
    	{
    	    return this.AHT;
    	} //method getAHT

    	/**
    	 * 設定平均文書處理時間
    	 *
    	 * @param ACW 平均文書處理時間
    	 **/
    	public void setACW(String ACW)
    	{
    	    if (ACW == null)
    	    {
    	        this.ACW = "";
    	    }
    	    else
    	    {
    	        this.ACW = ACW.trim();
    	    }
    	} //method setACW

    	/**
    	 * 取得平均文書處理時間
    	 *
    	 * @return ACW 平均文書處理時間
    	 **/
    	public String getACW()
    	{
    	    return this.ACW;
    	} //method getACW

    	/**
    	 * 設定總進線數
    	 *
    	 * @param callLogAmount 總進線數
    	 **/
    	public void setCallLogAmount(String callLogAmount)
    	{
    	    if (callLogAmount == null)
    	    {
    	        this.callLogAmount = "";
    	    }
    	    else
    	    {
    	        this.callLogAmount = callLogAmount.trim();
    	    }
    	} //method setCallLogAmount

    	/**
    	 * 取得總進線數
    	 *
    	 * @return callLogAmount 總進線數
    	 **/
    	public String getCallLogAmount()
    	{
    	    return this.callLogAmount;
    	} //method getCallLogAmount

    	/**
    	 * 設定無效電話
    	 *
    	 * @param notValidPhone 無效電話
    	 **/
    	public void setNotValidPhone(String notValidPhone)
    	{
    	    if (notValidPhone == null)
    	    {
    	        this.notValidPhone = "";
    	    }
    	    else
    	    {
    	        this.notValidPhone = notValidPhone.trim();
    	    }
    	} //method setNotValidPhone

    	/**
    	 * 取得無效電話
    	 *
    	 * @return notValidPhone 無效電話
    	 **/
    	public String getNotValidPhone()
    	{
    	    return this.notValidPhone;
    	} //method getNotValidPhone

    	/**
    	 * 設定進線數(不包無效電話)
    	 *
    	 * @param inbound 進線數(不包無效電話)
    	 **/
    	public void setInbound(int inbound)
    	{
    	    this.inbound = inbound;
    	} //method setInbound

    	/**
    	 * 取得進線數(不包無效電話)
    	 *
    	 * @return inbound 進線數(不包無效電話)
    	 **/
    	public int getInbound()
    	{
    	    return this.inbound;
    	} //method getInbound

    	/**
    	 * 設定有效值機時間
    	 *
    	 * @param intValidWorkTime 有效值機時間
    	 **/
    	public void setIntValidWorkTime(int intValidWorkTime)
    	{
    	    this.intValidWorkTime = intValidWorkTime;
    	} //method setIntValidWorkTime

    	/**
    	 * 取得有效值機時間
    	 *
    	 * @return intValidWorkTime 有效值機時間
    	 **/
    	public int getIntValidWorkTime()
    	{
    	    return this.intValidWorkTime;
    	} //method getIntValidWorkTime

    	/**
    	 * 設定值機率
    	 *
    	 * @param doubleWorkPercent 值機率
    	 **/
    	public void setDoubleWorkPercent(double doubleWorkPercent)
    	{
    	    this.doubleWorkPercent = doubleWorkPercent;
    	} //method setDoubleWorkPercent

    	/**
    	 * 取得值機率
    	 *
    	 * @return doubleWorkPercent 值機率
    	 **/
    	public double getDoubleWorkPercent()
    	{
    	    return this.doubleWorkPercent;
    	} //method getDoubleWorkPercent

    	/**
    	 * 設定利用率
    	 *
    	 * @param doubleUsePercent 利用率
    	 **/
    	public void setDoubleUsePercent(double doubleUsePercent)
    	{
    	    this.doubleUsePercent = doubleUsePercent;
    	} //method setDoubleUsePercent

    	/**
    	 * 取得利用率
    	 *
    	 * @return doubleUsePercent 利用率
    	 **/
    	public double getDoubleUsePercent()
    	{
    	    return this.doubleUsePercent;
    	} //method getDoubleUsePercent

    	/**
    	 * 設定平均處理時間
    	 *
    	 * @param intAHT 平均處理時間
    	 **/
    	public void setIntAHT(int intAHT)
    	{
    	    this.intAHT = intAHT;
    	} //method setIntAHT

    	/**
    	 * 取得平均處理時間
    	 *
    	 * @return intAHT 平均處理時間
    	 **/
    	public int getIntAHT()
    	{
    	    return this.intAHT;
    	} //method getIntAHT

    	/**
    	 * 設定平均文書處理時間
    	 *
    	 * @param intACW 平均文書處理時間
    	 **/
    	public void setIntACW(int intACW)
    	{
    	    this.intACW = intACW;
    	} //method setIntACW

    	/**
    	 * 取得平均文書處理時間
    	 *
    	 * @return intACW 平均文書處理時間
    	 **/
    	public int getIntACW()
    	{
    	    return this.intACW;
    	} //method getIntACW

    	/**
    	 * 設定總進線數
    	 *
    	 * @param intCallLogAmount 總進線數
    	 **/
    	public void setIntCallLogAmount(int intCallLogAmount)
    	{
    	    this.intCallLogAmount = intCallLogAmount;
    	} //method setIntCallLogAmount

    	/**
    	 * 取得總進線數
    	 *
    	 * @return intCallLogAmount 總進線數
    	 **/
    	public int getIntCallLogAmount()
    	{
    	    return this.intCallLogAmount;
    	} //method getIntCallLogAmount

    	/**
    	 * 設定無效電話
    	 *
    	 * @param intNotValidPhone 無效電話
    	 **/
    	public void setIntNotValidPhone(int intNotValidPhone)
    	{
    	    this.intNotValidPhone = intNotValidPhone;
    	} //method setIntNotValidPhone

    	/**
    	 * 取得無效電話
    	 *
    	 * @return intNotValidPhone 無效電話
    	 **/
    	public int getIntNotValidPhone()
    	{
    	    return this.intNotValidPhone;
    	} //method getIntNotValidPhone

    	/**
    	 * 設定進線數(不包無效電話)
    	 *
    	 * @param intInbound 進線數(不包無效電話)
    	 **/
    	public void setIntInbound(int intInbound)
    	{
    	    this.intInbound = intInbound;
    	} //method setIntInbound

    	/**
    	 * 取得進線數(不包無效電話)
    	 *
    	 * @return intInbound 進線數(不包無效電話)
    	 **/
    	public int getIntInbound()
    	{
    	    return this.intInbound;
    	} //method getIntInbound
    	
    	
    	public void setUserId(String userId)
    	{
    	    this.userId = userId;
    	} //method setUserId

    
    	public String getUserId()
    	{
    	    return this.userId;
    	}
    	
    	/**
    	 * 取得最早登入時間
    	 *
    	 * @return getFirstLoginTime 登入時間
    	 **/
    	public String getFirstLoginTime()
    	{
    	    return this.firstLoginTime;
    	} //method getFirstLoginTime
    	
    	/**
    	 * 取得最後登出時間
    	 *
    	 * @return getLastLogoutTime 登出時間
    	 **/
    	public String getLastLogoutTime()
    	{
    	    return this.lastLogoutTime;
    	} //method getLastLogoutTime


     /**
      * 設定暫時離席_code0
      *
      * @param intTotalNotReadyDuration_0 暫時離席_code0
      **/
     public void setIntTotalNotReadyDuration_0(int intTotalNotReadyDuration_0)
     {
         this.intTotalNotReadyDuration_0 = intTotalNotReadyDuration_0;
     }

     /**
      * 取得暫時離席_code0
      *
      * @return intTotalNotReadyDuration_0 暫時離席_code0
      **/
     public int getIntTotalNotReadyDuration_0()
     {
         return this.intTotalNotReadyDuration_0;
     }

     /**
      * 設定暫時離席_code1
      *
      * @param intTotalNotReadyDuration_1 暫時離席_code1
      **/
     public void setIntTotalNotReadyDuration_1(int intTotalNotReadyDuration_1)
     {
         this.intTotalNotReadyDuration_1 = intTotalNotReadyDuration_1;
     }

     /**
      * 取得暫時離席_code1
      *
      * @return intTotalNotReadyDuration_1 暫時離席_code1
      **/
     public int getIntTotalNotReadyDuration_1()
     {
         return this.intTotalNotReadyDuration_1;
     }

     /**
      * 設定暫時離席_code2
      *
      * @param intTotalNotReadyDuration_2 暫時離席_code2
      **/
     public void setIntTotalNotReadyDuration_2(int intTotalNotReadyDuration_2)
     {
         this.intTotalNotReadyDuration_2 = intTotalNotReadyDuration_2;
     }

     /**
      * 取得暫時離席_code2
      *
      * @return intTotalNotReadyDuration_2 暫時離席_code2
      **/
     public int getIntTotalNotReadyDuration_2()
     {
         return this.intTotalNotReadyDuration_2;
     }

     /**
      * 設定暫時離席_code3
      *
      * @param intTotalNotReadyDuration_3 暫時離席_code3
      **/
     public void setIntTotalNotReadyDuration_3(int intTotalNotReadyDuration_3)
     {
         this.intTotalNotReadyDuration_3 = intTotalNotReadyDuration_3;
     }

     /**
      * 取得暫時離席_code3
      *
      * @return intTotalNotReadyDuration_3 暫時離席_code3
      **/
     public int getIntTotalNotReadyDuration_3()
     {
         return this.intTotalNotReadyDuration_3;
     }

     /**
      * 設定暫時離席_code4
      *
      * @param intTotalNotReadyDuration_4 暫時離席_code4
      **/
     public void setIntTotalNotReadyDuration_4(int intTotalNotReadyDuration_4)
     {
         this.intTotalNotReadyDuration_4 = intTotalNotReadyDuration_4;
     }

     /**
      * 取得暫時離席_code4
      *
      * @return intTotalNotReadyDuration_4 暫時離席_code4
      **/
     public int getIntTotalNotReadyDuration_4()
     {
         return this.intTotalNotReadyDuration_4;
     }

     /**
      * 設定暫時離席_code5
      *
      * @param intTotalNotReadyDuration_5 暫時離席_code5
      **/
     public void setIntTotalNotReadyDuration_5(int intTotalNotReadyDuration_5)
     {
         this.intTotalNotReadyDuration_5 = intTotalNotReadyDuration_5;
     }

     /**
      * 取得暫時離席_code5
      *
      * @return intTotalNotReadyDuration_5 暫時離席_code5
      **/
     public int getIntTotalNotReadyDuration_5()
     {
         return this.intTotalNotReadyDuration_5;
     }

     /**
      * 設定暫時離席_code6
      *
      * @param intTotalNotReadyDuration_6 暫時離席_code6
      **/
     public void setIntTotalNotReadyDuration_6(int intTotalNotReadyDuration_6)
     {
         this.intTotalNotReadyDuration_6 = intTotalNotReadyDuration_6;
     }

     /**
      * 取得暫時離席_code6
      *
      * @return intTotalNotReadyDuration_6 暫時離席_code6
      **/
     public int getIntTotalNotReadyDuration_6()
     {
         return this.intTotalNotReadyDuration_6;
     }

     /**
      * 設定暫時離席_code7
      *
      * @param intTotalNotReadyDuration_7 暫時離席_code7
      **/
     public void setIntTotalNotReadyDuration_7(int intTotalNotReadyDuration_7)
     {
         this.intTotalNotReadyDuration_7 = intTotalNotReadyDuration_7;
     }

     /**
      * 取得暫時離席_code7
      *
      * @return intTotalNotReadyDuration_7 暫時離席_code7
      **/
     public int getIntTotalNotReadyDuration_7()
     {
         return this.intTotalNotReadyDuration_7;
     }

     /**
      * 設定暫時離席_code8
      *
      * @param intTotalNotReadyDuration_8 暫時離席_code8
      **/
     public void setIntTotalNotReadyDuration_8(int intTotalNotReadyDuration_8)
     {
         this.intTotalNotReadyDuration_8 = intTotalNotReadyDuration_8;
     }

     /**
      * 取得暫時離席_code8
      *
      * @return intTotalNotReadyDuration_8 暫時離席_code8
      **/
     public int getIntTotalNotReadyDuration_8()
     {
         return this.intTotalNotReadyDuration_8;
     }

     /**
      * 設定暫時離席_code9
      *
      * @param intTotalNotReadyDuration_9 暫時離席_code9
      **/
     public void setIntTotalNotReadyDuration_9(int intTotalNotReadyDuration_9)
     {
         this.intTotalNotReadyDuration_9 = intTotalNotReadyDuration_9;
     }

     /**
      * 取得暫時離席_code9
      *
      * @return intTotalNotReadyDuration_9 暫時離席_code9
      **/
     public int getIntTotalNotReadyDuration_9()
     {
         return this.intTotalNotReadyDuration_9;
     }

     /**
      * 設定暫時離席_code10
      *
      * @param intTotalNotReadyDuration_10 暫時離席_code10
      **/
     public void setIntTotalNotReadyDuration_10(int intTotalNotReadyDuration_10)
     {
         this.intTotalNotReadyDuration_10 = intTotalNotReadyDuration_10;
     }

     /**
      * 取得暫時離席_code10
      *
      * @return intTotalNotReadyDuration_10 暫時離席_code10
      **/
     public int getIntTotalNotReadyDuration_10()
     {
         return this.intTotalNotReadyDuration_10;
     }

     /**
      * 設定暫時離席_code0
      *
      * @param totalNotReadyDuration_0 暫時離席_code0
      **/
     public void setTotalNotReadyDuration_0(String totalNotReadyDuration_0)
     {
         this.totalNotReadyDuration_0 = totalNotReadyDuration_0;
     }

     /**
      * 取得暫時離席_code0
      *
      * @return totalNotReadyDuration_0 暫時離席_code0
      **/
     public String getTotalNotReadyDuration_0()
     {
         return this.totalNotReadyDuration_0;
     }

     /**
      * 設定暫時離席_code1
      *
      * @param totalNotReadyDuration_1 暫時離席_code1
      **/
     public void setTotalNotReadyDuration_1(String totalNotReadyDuration_1)
     {
         this.totalNotReadyDuration_1 = totalNotReadyDuration_1;
     }

     /**
      * 取得暫時離席_code1
      *
      * @return totalNotReadyDuration_1 暫時離席_code1
      **/
     public String getTotalNotReadyDuration_1()
     {
         return this.totalNotReadyDuration_1;
     }

     /**
      * 設定暫時離席_code2
      *
      * @param totalNotReadyDuration_2 暫時離席_code2
      **/
     public void setTotalNotReadyDuration_2(String totalNotReadyDuration_2)
     {
         this.totalNotReadyDuration_2 = totalNotReadyDuration_2;
     }

     /**
      * 取得暫時離席_code2
      *
      * @return totalNotReadyDuration_2 暫時離席_code2
      **/
     public String getTotalNotReadyDuration_2()
     {
         return this.totalNotReadyDuration_2;
     }

     /**
      * 設定暫時離席_code3
      *
      * @param totalNotReadyDuration_3 暫時離席_code3
      **/
     public void setTotalNotReadyDuration_3(String totalNotReadyDuration_3)
     {
         this.totalNotReadyDuration_3 = totalNotReadyDuration_3;
     }

     /**
      * 取得暫時離席_code3
      *
      * @return totalNotReadyDuration_3 暫時離席_code3
      **/
     public String getTotalNotReadyDuration_3()
     {
         return this.totalNotReadyDuration_3;
     }

     /**
      * 設定暫時離席_code4
      *
      * @param totalNotReadyDuration_4 暫時離席_code4
      **/
     public void setTotalNotReadyDuration_4(String totalNotReadyDuration_4)
     {
         this.totalNotReadyDuration_4 = totalNotReadyDuration_4;
     }

     /**
      * 取得暫時離席_code4
      *
      * @return totalNotReadyDuration_4 暫時離席_code4
      **/
     public String getTotalNotReadyDuration_4()
     {
         return this.totalNotReadyDuration_4;
     }

     /**
      * 設定暫時離席_code5
      *
      * @param totalNotReadyDuration_5 暫時離席_code5
      **/
     public void setTotalNotReadyDuration_5(String totalNotReadyDuration_5)
     {
         this.totalNotReadyDuration_5 = totalNotReadyDuration_5;
     }

     /**
      * 取得暫時離席_code5
      *
      * @return totalNotReadyDuration_5 暫時離席_code5
      **/
     public String getTotalNotReadyDuration_5()
     {
         return this.totalNotReadyDuration_5;
     }

     /**
      * 設定暫時離席_code6
      *
      * @param totalNotReadyDuration_6 暫時離席_code6
      **/
     public void setTotalNotReadyDuration_6(String totalNotReadyDuration_6)
     {
         this.totalNotReadyDuration_6 = totalNotReadyDuration_6;
     }

     /**
      * 取得暫時離席_code6
      *
      * @return totalNotReadyDuration_6 暫時離席_code6
      **/
     public String getTotalNotReadyDuration_6()
     {
         return this.totalNotReadyDuration_6;
     }

     /**
      * 設定暫時離席_code7
      *
      * @param totalNotReadyDuration_7 暫時離席_code7
      **/
     public void setTotalNotReadyDuration_7(String totalNotReadyDuration_7)
     {
         this.totalNotReadyDuration_7 = totalNotReadyDuration_7;
     }

     /**
      * 取得暫時離席_code7
      *
      * @return totalNotReadyDuration_7 暫時離席_code7
      **/
     public String getTotalNotReadyDuration_7()
     {
         return this.totalNotReadyDuration_7;
     }

     /**
      * 設定暫時離席_code8
      *
      * @param totalNotReadyDuration_8 暫時離席_code8
      **/
     public void setTotalNotReadyDuration_8(String totalNotReadyDuration_8)
     {
         this.totalNotReadyDuration_8 = totalNotReadyDuration_8;
     }

     /**
      * 取得暫時離席_code8
      *
      * @return totalNotReadyDuration_8 暫時離席_code8
      **/
     public String getTotalNotReadyDuration_8()
     {
         return this.totalNotReadyDuration_8;
     }

     /**
      * 設定暫時離席_code9
      *
      * @param totalNotReadyDuration_9 暫時離席_code9
      **/
     public void setTotalNotReadyDuration_9(String totalNotReadyDuration_9)
     {
         this.totalNotReadyDuration_9 = totalNotReadyDuration_9;
     }

     /**
      * 取得暫時離席_code9
      *
      * @return totalNotReadyDuration_9 暫時離席_code9
      **/
     public String getTotalNotReadyDuration_9()
     {
         return this.totalNotReadyDuration_9;
     }

     /**
      * 設定暫時離席_code10
      *
      * @param totalNotReadyDuration_10 暫時離席_code10
      **/
     public void setTotalNotReadyDuration_10(String totalNotReadyDuration_10)
     {
         this.totalNotReadyDuration_10 = totalNotReadyDuration_10;
     }

     public String getTotalNotReadyDuration_10()
     {
         return this.totalNotReadyDuration_10;
     }
 }
