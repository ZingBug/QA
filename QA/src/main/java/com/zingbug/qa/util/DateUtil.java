package com.zingbug.qa.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ZingBug on 2019/6/30.
 */
public class DateUtil {

    public static Date getAfterDate(Date date,int num,int type)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type,num);
        return calendar.getTime();
    }

    public static Date getCurrentDate()
    {
        return new Date();
    }

    public static Long getCurrentTime()
    {
        return getCurrentDate().getTime();
    }
}
