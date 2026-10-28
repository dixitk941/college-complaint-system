package com.college.ccms.util;

public final class Text {

    private Text() {
    }

    public static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    public static String trimToNull(String value) {
        String trimmed = trim(value);
        return trimmed.isEmpty() ? null : trimmed;
    }
}
