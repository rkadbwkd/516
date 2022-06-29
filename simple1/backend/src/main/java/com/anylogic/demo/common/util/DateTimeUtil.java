/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateTimeUtil {

    public static Date parse(String time) {
        return parse("yyyy-MM-dd HH:mm:ss.SSS", time);
    }

    public static Date parse(String pattern, String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDate(int year, int month, int date, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, date, hour, minute, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDate(int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    public static Calendar getCalendar(int field, int amount) {
        return getCalendar(new Date(), field, amount);
    }
    
    public static Calendar getCalendar(Date date, int field, int amount) {
        Calendar cal = Calendar.getInstance();
        
        cal.setTime(date);
        cal.add(field, amount);
        
        return cal;
    }
    
    public static String MiliToString(String MiliSecondDate) {
        
        long mili = Long.parseLong(MiliSecondDate);
        Date dateMili = new Date(mili);
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        
        return date.format(dateMili);
        
    }

}

