package com.demo.javademo.utils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public  class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static int getYearFromDateString(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return date.getYear();
    }

    public static int getYearFromDateStringOffset(String dateString) {
        OffsetDateTime dateTime = OffsetDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return dateTime.getYear();
    }
}
