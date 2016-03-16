package com.ljq.schedule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Larry
 * Date: 2015-04-01
 * Time: 10:52
 * Version: 1.0
 */

public class CronExpHelper {
    private Log log = LogFactory.getLog(CronExpHelper.class);

    private static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat expFmt = new SimpleDateFormat("m H d M");

    private static SimpleDateFormat yearFmt = new SimpleDateFormat("yyyy");

    /**
     * 将时间和日期格式化为日期对象
     * @param date
     * @param time 只包含时和分
     * @return
     * @throws java.text.ParseException
     */
    public static Date format(String date,String time) throws ParseException {
        String srcDateStr = date+" "+time+":00";
        return fmt.parse(srcDateStr);
    }

    /**
     * 将日期对象转换为时间表达式
     * @param date
     * @return
     */
    public static String date2Exp(Date date){
        String exp = "0 " + expFmt.format(date) + " ? " + yearFmt.format(date);
        return exp;
    }
}
