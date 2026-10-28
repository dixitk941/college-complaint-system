package com.college.ccms.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class DateTimes {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH);

    private DateTimes() {
    }

    public static String format(LocalDateTime value) {
        if (value == null) {
            return "";
        }
        return value.format(FORMAT);
    }
}
