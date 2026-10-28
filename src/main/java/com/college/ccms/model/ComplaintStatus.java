package com.college.ccms.model;

public enum ComplaintStatus {
    OPEN("Open"),
    IN_PROGRESS("In progress"),
    RESOLVED("Resolved"),
    REJECTED("Rejected"),
    WITHDRAWN("Withdrawn");

    private final String label;

    ComplaintStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
