package com.college.ccms.util;

import com.college.ccms.model.User;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Helpers for the servlet HttpSession. This is the login session kept by the browser.
 * It is not a Hibernate Session.
 */
public final class Sessions {

    public static final String LOGGED_USER = "loggedUser";

    private Sessions() {
    }

    public static User currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(LOGGED_USER);
        if (value instanceof User) {
            return (User) value;
        }
        return null;
    }

    public static void redirectHome(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {
        String context = request.getContextPath();
        if (user.isAdmin()) {
            response.sendRedirect(context + "/admin/dashboard");
        } else {
            response.sendRedirect(context + "/student/dashboard");
        }
    }
}
