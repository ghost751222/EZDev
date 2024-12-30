/****************************************************************************
 *
 * Copyright (c) 2009 ESound Tech. All Rights Reserved.
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
 *     File name:       CalendarUtil.java
 *
 *     History:
 *     Date             Author          Comments
 *     ----------------------------------------------------------------------
 *     DEC 03, 2009     SHENG-HAN WU    Initial Release
 *****************************************************************************/
package com.consilium.excel.models.talk;

import java.text.NumberFormat ;

import java.time.Duration;
import java.util.Calendar ;
import java.util.GregorianCalendar ;
/**
  * <code>CalendarUtil</code>
  * ����U���Ƥu��
  * @author      SHENG-HAN WU
  * @version     1.0
  **/
public class CalendarUtil
{
    /**
     * ���o�ҿ�J������Ӥ몺�ĴX�g
     * @param year
     * @param mon
     * @param day
     * @return
     */
    public static int process ( int year , int mon , int day )
    {
        Calendar c = new GregorianCalendar () ;
        c.set ( year , mon - 1 , 1 ) ;
        int comp = 7 - c.get ( Calendar.DAY_OF_WEEK ) + 2 ;
        int i = 1 ;
        if ( day > comp )
        {
            do
            {
                i++ ;
                comp += 7 ;
            }
            while ( day > comp ) ;
        }
        return i ;
    }

    /**
     * �榡�Ʈɶ�.
     * 
     * @param sec ���
     * 
     * @return �榡�ƹL���ɶ�(HH:MM:SS).
     **/
    public static String formatDate ( long sec )
    {
        Duration duration = Duration.ofSeconds(sec);
        long seconds = duration.getSeconds();
        long HH = seconds / 3600;
        long MM = (seconds % 3600) / 60;
        long SS = seconds % 60;
        return String.format("%02d:%02d:%02d", HH, MM, SS);
//        int ss , mm , hh ;
//        int _sec = (int) sec;
//        NumberFormat nf = NumberFormat.getInstance () ;
//        nf.setMinimumIntegerDigits ( 2 ) ;
//
//        ss = _sec % 60 ;
//        mm = ( _sec / 60 ) % 60 ;
//        hh = _sec / 3600 ;
//
//        return nf.format ( hh ) + ":" + nf.format ( mm ) + ":" + nf.format ( ss ) ;
    }
}
