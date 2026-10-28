package com.college.ccms.filter;

import com.college.ccms.model.User;
import com.college.ccms.util.Flashes;
import com.college.ccms.util.Sessions;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Blocks student and administrator pages unless the HttpSession has the right user.
 */
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        User user = Sessions.currentUser(httpRequest);
        String context = httpRequest.getContextPath();
        String uri = httpRequest.getRequestURI();

        if (user == null) {
            Flashes.error(httpRequest, "Sign in to continue.");
            httpResponse.sendRedirect(context + "/login");
            return;
        }
        if (uri.startsWith(context + "/admin") && !user.isAdmin()) {
            Flashes.error(httpRequest, "That page is for administrators.");
            httpResponse.sendRedirect(context + "/student/dashboard");
            return;
        }
        if (uri.startsWith(context + "/student") && !user.isStudent()) {
            httpResponse.sendRedirect(context + "/admin/dashboard");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
