package com.fsq.pobop.data;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @TypeConverter
    public String dateToString(LocalDate localDate) {
        if (localDate != null) {
            return localDate.format(format);
        } else {
            return "";
        }
    }

    @TypeConverter
    public LocalDate stringToDate(String date) {
        if (!date.equals("")) {
            return LocalDate.parse(date, format);
        } else {
            return null;
        }
    }
}
