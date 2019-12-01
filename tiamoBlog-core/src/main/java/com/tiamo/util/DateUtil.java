package com.tiamo.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author wangjian
 * @version 1.0
 * @see com.tiamo.util.DateUtil
 * @since JDK1.8
 */
public class DateUtil {

    public static  final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static  final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static  final String DAY_YYYYMMdd = "yyyy-MM-dd";


    public static String formatToStr(Date date, String format) {
        if (StringUtils.isBlank(format)) {
            format = DEFAULT_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Date formatToDate(String dateStr, String format) throws ParseException {
        if (StringUtils.isBlank(format)) {
            format = DEFAULT_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(dateStr);
    }

}
