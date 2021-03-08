package com.catherine.libnetwork.cache;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    //存进数据库转化
    @TypeConverter
    public static Long date2Long(Date date) {
        return date.getTime();
    }
//数据库取出来转化
    @TypeConverter
    public static Date long2Date(Long data) {
        return new Date(data);
    }
}
