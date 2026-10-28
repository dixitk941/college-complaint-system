package com.college.ccms.model;

public enum ComplaintCategory {
    ACADEMIC("Academic"),
    EXAMINATION("Examination"),
    HOSTEL("Hostel"),
    LIBRARY("Library"),
    LABORATORY("Laboratory"),
    CANTEEN("Canteen"),
    TRANSPORT("Transport"),
    INFRASTRUCTURE("Infrastructure"),
    OTHER("Other");

    private final String label;

    ComplaintCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
