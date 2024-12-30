/****************************************************************************
 *
 * Copyright (c) 2013 ESound Tech. All Rights Reserved.
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
 *     File name:       SatisfactionStatisticsObject.java
 *
 *     History:
 *     Date               Author                  Comments
 *     -----------------------------------------------------------------------
 *     Mar 18, 2013       Berton                  Initial Release
 *****************************************************************************/

package com.consilium.excel.models;

import java.util.Hashtable;

public class SatisfactionStatisticsObject
{
    /**
     * 存放統計值
     **/
    private Hashtable table = new Hashtable();

    public void setTable(Hashtable table)
    {
        if (table != null)
        {
            this.table = table;
        }
        else
        {
            table = new Hashtable();
        }
    }

    public Hashtable getTable()
    {
        return table;
    }

    public int getValue(String key)
    {
        if (key != null)
        {
            if (table.get(key) != null)
            {
                return Integer.parseInt(String.valueOf(table.get(key)));
            }
        }
        return 0;
    }
}
