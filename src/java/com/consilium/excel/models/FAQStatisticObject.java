/****************************************************************************
 *
 * Copyright (c) 2012 ESound Tech. All Rights Reserved.
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
 *     File name:       FAQStatisticObject.java
 *
 *     History:
 *     Date               Author            Comments
 *     -----------------------------------------------------------------------
 *     JUL 4, 2012       mars           Initial Release
 *****************************************************************************/
package com.consilium.excel.models;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
  * <code>FAQStatisticObject</code>
  * FAQ�έp����
  **/
public class FAQStatisticObject
{
	
	/**
	 * ����
	 **/
	private double  score =0;
	
	private double multiple = 0.2;
	
	/**
	 * ���N�X
	 **/
	private String unitCode = "";

	/**
	 * ���W��
	 **/
	private String unitName = "";

	/**
	 * �U��Ǵ��ѱ`���ݵ��`�p
	 **/
	private int leftAmount;

	/**
	 * 1996�f�y�ƸܳN����
	 **/
	private int rightAmount;
        
         /**
          * �U��Ǵ��ѱ`���ݵ��ӽФU�[�`�p
          **/
         private int leftSAmount;

         /**
          * �U��Ǵ��ѱ`���ݵ��U�[�`�p
          **/
         private int leftDAmount;
         
         /**
          * �U��Ǵ��ѱ`���ݵ��R���`�p
          **/
         private int leftDeleteAmount;
         

         /**
          * �U��Ǵ��ѱ`���ݵ��i���`�p(30�ѫ�L��)
          **/
         private int leftEAmount;

         /**
          * �U��Ǵ��ѱ`���ݵ��i���`�p(�w�L��)
          **/
         private int leftOAmount;

         /**
          * 1996�f�y�ƸܳN����
          **/
         private int rightSAmount;

         /**
          * 1996�f�y�ƸܳN����(�U�[)
          **/
         private int rightDAmount;
         
         /**
          * 1996�f�y�ƸܳN����(�R��)
          **/
         private int rightDeleteAmount;

         /**
          * 1996�f�y�ƸܳN����(30�ѫ�L��)
          **/
         private int rightEAmount;

         /**
          * 1996�f�y�ƸܳN����(�w�L��)
          **/
         private int rightOAmount;
         
        /**
         * 1996�f�y�ƸܳN��������ĳ���ƶq
         **/
        private int opinionAmount;
        
        /**
          * ���ID
          **/
         private String sectionCode = "";
    
        /**
         * ��ǦW��
         **/
        private String sectionName = "";

	/**
	 * �]�w���N�X
	 *
	 * @param unitCode ���N�X
	 **/
	public void setUnitCode(String unitCode)
	{
	    if (unitCode == null)
	    {
	        this.unitCode = "";
	    }
	    else
	    {
	        this.unitCode = unitCode.trim();
	    }
	} //method setUnitCode

	/**
	 * ���o���N�X
	 *
	 * @return unitCode ���N�X
	 **/
	public String getUnitCode()
	{
	    return this.unitCode;
	} //method getUnitCode

	/**
	 * �]�w���W��
	 *
	 * @param unitName ���W��
	 **/
	public void setUnitName(String unitName)
	{
	    if (unitName == null)
	    {
	        this.unitName = "";
	    }
	    else
	    {
	        this.unitName = unitName.trim();
	    }
	} //method setUnitName

	/**
	 * ���o���W��
	 *
	 * @return unitName ���W��
	 **/
	public String getUnitName()
	{
	    return this.unitName;
	} //method getUnitName

	/**
	 * �]�w�U��Ǵ��ѱ`���ݵ��`�p
	 *
	 * @param leftAmount �U��Ǵ��ѱ`���ݵ��`�p
	 **/
	public void setLeftAmount(int leftAmount)
	{
	    this.leftAmount = leftAmount;
        this.setScore();//�]�w����
	} //method setLeftAmount

	/**
	 * ���o�U��Ǵ��ѱ`���ݵ��`�p
	 *
	 * @return leftAmount �U��Ǵ��ѱ`���ݵ��`�p
	 **/
	public int getLeftAmount()
	{
	    return this.leftAmount;
	} //method getLeftAmount

	/**
	 * �]�w1996�f�y�ƸܳN����
	 *
	 * @param rightAmount 1996�f�y�ƸܳN����
	 **/
	public void setRightAmount(int rightAmount)
	{
	    this.rightAmount = rightAmount;
	} //method setRightAmount

	/**
	 * ���o1996�f�y�ƸܳN����
	 *
	 * @return rightAmount 1996�f�y�ƸܳN����
	 **/
	public int getRightAmount()
	{
	    return this.rightAmount;
	} //method getRightAmount
        
	 /**
          * �]�w�U��Ǵ��ѱ`���ݵ��ӽФU�[�`�p
          *
          * @param leftSAmount �U��Ǵ��ѱ`���ݵ��ӽФU�[�`�p
          **/
         public void setLeftSAmount(int leftSAmount)
         {
             this.leftSAmount = leftSAmount;
         } //method setLeftSAmount

         /**
          * ���o�U��Ǵ��ѱ`���ݵ��ӽФU�[�`�p
          *
          * @return leftSAmount �U��Ǵ��ѱ`���ݵ��ӽФU�[�`�p
          **/
         public int getLeftSAmount()
         {
             return this.leftSAmount;
         } //method getLeftSAmount

         /**
          * �]�w�U��Ǵ��ѱ`���ݵ��U�[�`�p
          *
          * @param leftDAmount �U��Ǵ��ѱ`���ݵ��U�[�`�p
          **/
         public void setLeftDAmount(int leftDAmount)
         {
             this.leftDAmount = leftDAmount;
         } //method setLeftDAmount

         /**
          * ���o�U��Ǵ��ѱ`���ݵ��U�[�`�p
          *
          * @return leftDAmount �U��Ǵ��ѱ`���ݵ��U�[�`�p
          **/
         public int getLeftDAmount()
         {
             return this.leftDAmount;
         } //method getLeftDAmount

         /**
          * �]�w�U��Ǵ��ѱ`���ݵ��i���`�p(30�ѫ�L��)
          *
          * @param leftEAmount �U��Ǵ��ѱ`���ݵ��i���`�p(30�ѫ�L��)
          **/
         public void setLeftEAmount(int leftEAmount)
         {
             this.leftEAmount = leftEAmount;
         } //method setLeftEAmount

         /**
          * ���o�U��Ǵ��ѱ`���ݵ��i���`�p(30�ѫ�L��)
          *
          * @return leftEAmount �U��Ǵ��ѱ`���ݵ��i���`�p(30�ѫ�L��)
          **/
         public int getLeftEAmount()
         {
             return this.leftEAmount;
         } //method getLeftEAmount

         /**
          * �]�w�U��Ǵ��ѱ`���ݵ��i���`�p(�w�L��)
          *
          * @param leftOAmount �U��Ǵ��ѱ`���ݵ��i���`�p(�w�L��)
          **/
         public void setLeftOAmount(int leftOAmount)
         {
             this.leftOAmount = leftOAmount;
         } //method setLeftOAmount

         /**
          * ���o�U��Ǵ��ѱ`���ݵ��i���`�p(�w�L��)
          *
          * @return leftOAmount �U��Ǵ��ѱ`���ݵ��i���`�p(�w�L��)
          **/
         public int getLeftOAmount()
         {
             return this.leftOAmount;
         } //method getLeftOAmount

         /**
          * �]�w1996�f�y�ƸܳN����
          *
          * @param rightSAmount 1996�f�y�ƸܳN����
          **/
         public void setRightSAmount(int rightSAmount)
         {
             this.rightSAmount = rightSAmount;
         } //method setRightSAmount

         /**
          * ���o1996�f�y�ƸܳN����
          *
          * @return rightSAmount 1996�f�y�ƸܳN����
          **/
         public int getRightSAmount()
         {
             return this.rightSAmount;
         } //method getRightSAmount

         /**
          * �]�w1996�f�y�ƸܳN����
          *
          * @param rightDAmount 1996�f�y�ƸܳN����
          **/
         public void setRightDAmount(int rightDAmount)
         {
             this.rightDAmount = rightDAmount;
         } //method setRightDAmount

         /**
          * ���o1996�f�y�ƸܳN����
          *
          * @return rightDAmount 1996�f�y�ƸܳN����
          **/
         public int getRightDAmount()
         {
             return this.rightDAmount;
         } //method getRightDAmount

         /**
          * �]�w1996�f�y�ƸܳN����(30�ѫ�L��)
          *
          * @param rightEAmount 1996�f�y�ƸܳN����(30�ѫ�L��)
          **/
         public void setRightEAmount(int rightEAmount)
         {
             this.rightEAmount = rightEAmount;
         } //method setRightEAmount

         /**
          * ���o1996�f�y�ƸܳN����(30�ѫ�L��)
          *
          * @return rightEAmount 1996�f�y�ƸܳN����(30�ѫ�L��)
          **/
         public int getRightEAmount()
         {
             return this.rightEAmount;
         } //method getRightEAmount

         /**
          * �]�w1996�f�y�ƸܳN����(�w�L��)
          *
          * @param rightOAmount 1996�f�y�ƸܳN����(�w�L��)
          **/
         public void setRightOAmount(int rightOAmount)
         {
             this.rightOAmount = rightOAmount;
         } //method setRightOAmount

         /**
          * ���o1996�f�y�ƸܳN����(�w�L��)
          *
          * @return rightOAmount 1996�f�y�ƸܳN����(�w�L��)
          **/
         public int getRightOAmount()
         {
             return this.rightOAmount;
         } //method getRightOAmount
         
          /**
           * �]�w1996�f�y�ƸܳN��������ĳ���ƶq
           *
           * @param opinionAmount 1996�f�y�ƸܳN��������ĳ���ƶq
           **/
          public void setOpinionAmount(int opinionAmount)
          {
              this.opinionAmount = opinionAmount;
          } //method setOpinionAmount

          /**
           * ���o1996�f�y�ƸܳN��������ĳ���ƶq
           *
           * @return opinionAmount 1996�f�y�ƸܳN��������ĳ���ƶq
           **/
          public int getOpinionAmount()
          {
              return this.opinionAmount;
          } //method getOpinionAmount
          
            /**
             * �]�w���ID
             *
             * @param sectionCode ���ID
             **/
            public void setSectionCode(String sectionCode)
            {
                if (sectionCode == null)
                {
                    this.sectionCode = "";
                }
                else
                {
                    this.sectionCode = sectionCode.trim();
                }
            } //method setSectionCode
    
            /**
             * ���o���ID
             *
             * @return sectionCode ���ID
             **/
            public String getSectionCode()
            {
                return this.sectionCode;
            } //method getSectionCode

           /**
            * �]�w��ǦW��
            *
            * @param sectionName ��ǦW��
            **/
           public void setSectionName(String sectionName)
           {
               if (sectionName == null)
               {
                   this.sectionName = "";
               }
               else
               {
                   this.sectionName = sectionName.trim();
               }
           } //method setSectionName

           /**
            * ���o��ǦW��
            *
            * @return sectionName ��ǦW��
            **/
           public String getSectionName()
           {
               return this.sectionName;
           } //method getSectionName
 
           /**
            * �]�w�U��Ǵ��ѱ`���ݵ��R���`�p
            *
            * @param leftDeleteAmount �U��Ǵ��ѱ`���ݵ��R���`�p
            **/
           public void setLeftDeleteAmount(int leftDeleteAmount)
           {
               this.leftDeleteAmount=leftDeleteAmount;
           } //method setLeftDeleteAmount

           /**
            * ���o�U��Ǵ��ѱ`���ݵ��R���`�p
            *
            * @return leftDeleteAmount �U��Ǵ��ѱ`���ݵ��R���`�p
            **/
           public int getLeftDeleteAmount()
           {
               return this.leftDeleteAmount;
           } //method getLeftDeleteAmount
           
           /**
            * �]�w�U��Ǵ��ѱ`���ݵ��R���`�p
            *
            * @param rightDeleteAmount �U��Ǵ��ѱ`���ݵ��R���`�p
            **/
           public void setRightDeleteAmount(int rightDeleteAmount)
           {
               this.rightDeleteAmount=rightDeleteAmount;
           } //method setRightDeleteAmount

           /**
            * ���o�U��Ǵ��ѱ`���ݵ��R���`�p
            *
            * @return rightDeleteAmount �U��Ǵ��ѱ`���ݵ��R���`�p
            **/
           public int getRightDeleteAmount()
           {
               return this.rightDeleteAmount;
           } //method getRightDeleteAmount
           
           
           public void setScore()
           {
        	   //�s�WFAQ�C1�h�[0.2���F�̦h�[3��
        	   String tmpValue="0";
        	   if(this.getLeftAmount()>=15)
               {
        		   tmpValue ="3";
               }
               else
               {
            	   DecimalFormat df=new DecimalFormat("#.#");
            	   score=(this.getLeftAmount()*multiple);
            	   tmpValue=df.format(score);
            	   
               }
        	   
        	  
        	   this.score=Double.parseDouble(tmpValue);
        	   
           }
           
           public double getScore()
           {
        	  return this.score;
           }
           
    
}
