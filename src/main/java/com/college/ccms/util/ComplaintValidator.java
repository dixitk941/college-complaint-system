package com.college.ccms.util;

public final class ComplaintValidator {

    private ComplaintValidator() {
    }

    public static String validate(String title, String description, String location) {
        if (title == null || title.length() < 5 || title.length() > 120) {
            return "Title must be between 5 and 120 characters.";
        }
        if (description == null || description.length() < 20 || description.length() > 2000) {
            return "Description must be between 20 and 2000 characters.";
        }
        if (location != null && location.length() > 120) {
            return "Location must be 120 characters or fewer.";
        }
        return null;
    }
}
