package com.consilium.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class StringUtil {

    public static String formatChineseDate(Timestamp timestamp) {
        String returnValue = "";
        try {
            String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(timestamp);
            //Integer yyyy = Integer.parseInt(s.substring(0, 4));
            //Integer chineseDate = yyyy - 1911;
            //returnValue  =    chineseDate.toString()  + s.substring(4);
            returnValue = s;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnValue;
    }
}
