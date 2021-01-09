package com.fsq.pobop.data;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @TypeConverter
    public String dateToString(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return localDateTime.format(format);
        } else {
            return "";
        }
    }

    @TypeConverter
    public LocalDateTime stringToDate(String date) {
        if (!date.equals("")) {
            return LocalDateTime.parse(date, format);
        } else {
            return null;
        }
    }
}
