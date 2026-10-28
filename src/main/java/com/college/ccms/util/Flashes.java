package com.college.ccms.util;

import javax.servlet.http.HttpServletRequest;

/** Messages that survive one redirect, then disappear. */
public final class Flashes {

    public static final String SUCCESS = "flash";
    public static final String ERROR = "flashError";

    private Flashes() {
    }

    public static void success(HttpServletRequest request, String message) {
        request.getSession().setAttribute(SUCCESS, message);
    }

    public static void error(HttpServletRequest request, String message) {
        request.getSession().setAttribute(ERROR, message);
    }
}
