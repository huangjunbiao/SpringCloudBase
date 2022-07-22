package com.huang.cloudbase.learn.utils;

import cn.hutool.core.date.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * 大部分方法在cn.hutool.core.date.DateUtil中，java.util.Date大部分方法已经废弃，Date最新由Instant替代。
 *
 * @author huangjunbiao_cdv
 */
public class DateAndTime {
    /**
     * 将文本类型时间解析为Instant类型
     *
     * @param text 文本
     * @return 时间
     */
    public static Instant StringToInstant(CharSequence text, DateTimeFormatter formatter) {
        return LocalDateTime.parse(text, formatter).atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * String类型的毫秒数转为date
     *
     * @param longString 毫秒数
     * @return 时间
     */
    public static Date StringToDate(String longString) {
        return new Date(Long.parseLong(longString));
//        return DateUtil.date(Long.parseLong(longString));
    }

    public void format() {
        System.out.println(new Date());
        String date = "Tue Jul 19 14:23:29 +0800 2022";
        System.out.println(DateUtil.parse(date, "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH));

        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        try {
            System.out.println(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
