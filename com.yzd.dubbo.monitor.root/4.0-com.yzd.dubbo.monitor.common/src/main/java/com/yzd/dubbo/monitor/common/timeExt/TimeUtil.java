package com.yzd.dubbo.monitor.common.timeExt;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zd.yao on 2017/6/28.
 */
public class TimeUtil {
    public static String getDateString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String str2 = dateFormat.format(date);
        return str2;
    }
    public static String getHourString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat( "HH");
        String str2 = formatter.format(date);
        return str2;
    }
    public static String getFullTimeString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        String str2 = formatter.format(date);
        return str2;
    }
    public static String getFullShortTimeString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMddHHmmss");
        String str2 = formatter.format(date);
        return str2;
    }
}
