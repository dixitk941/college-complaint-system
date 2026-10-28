package com.college.ccms.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class AppConstants {

    public static final String COLLEGE_NAME = "City College of Engineering";
    public static final String APP_NAME = "Complaint Management System";

    public static final List<String> DEPARTMENTS = Collections.unmodifiableList(Arrays.asList(
            "Computer Science and Engineering",
            "Information Technology",
            "Electronics and Communication",
            "Electrical Engineering",
            "Mechanical Engineering",
            "Civil Engineering",
            "Management Studies"
    ));

    public static final List<String> YEARS = Collections.unmodifiableList(Arrays.asList(
            "First Year",
            "Second Year",
            "Third Year",
            "Fourth Year"
    ));

    private AppConstants() {
    }
}
