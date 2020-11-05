package com.deyuan.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {
    //日期转字符串
    public static String data2String(Date date,String patt){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(patt);
        String format = simpleDateFormat.format(date);
        return format;
    }
    //patt为日期格式  xxxx-xx-xx  xx-xx-xx

    //字符串转日期
    public static Date String2Date(String str ,String patt){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(patt);
        try {
            Date date = simpleDateFormat.parse(str);
            return date;
        } catch (ParseException e) {
            throw new RuntimeException("日期格式转换异常"+e);
        }

    }
}
