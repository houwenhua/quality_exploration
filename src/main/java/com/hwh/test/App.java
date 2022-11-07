package com.hwh.test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Date date = DateUtil.parse("2022-08-10T00:00:00.000Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String format = DateUtil.format(date, "yyyy-MM-dd");
        Date parse = DateUtil.parse(format, "yyyy-MM-dd");
        java.sql.Date date1 = new java.sql.Date(parse.getTime());

        System.out.println( "Hello World! 侯文华" + date1);
    }
}
