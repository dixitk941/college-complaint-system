package com.college.ccms.util;

import com.college.ccms.model.ComplaintCategory;
import com.college.ccms.model.ComplaintStatus;
import com.college.ccms.model.Priority;

public final class Enums {

    private Enums() {
    }

    public static ComplaintStatus status(String raw) {
        return parse(ComplaintStatus.class, raw);
    }

    public static ComplaintCategory category(String raw) {
        return parse(ComplaintCategory.class, raw);
    }

    public static Priority priority(String raw) {
        return parse(Priority.class, raw);
    }

    private static <T extends Enum<T>> T parse(Class<T> type, String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return null;
        }
        try {
            return Enum.valueOf(type, raw.trim());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
