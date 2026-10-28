package com.college.ccms.servlet;

import com.college.ccms.dao.UserDao;
import com.college.ccms.model.User;
import com.college.ccms.util.PasswordUtil;
import com.college.ccms.util.Sessions;
import com.college.ccms.util.Text;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Signs a person in. The account is stored in the HttpSession under "loggedUser".
 * That object is not a Hibernate Session.
 */
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User current = Sessions.currentUser(request);
        if (current != null) {
            Sessions.redirectHome(request, response, current);
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = Text.trim(request.getParameter("username")).toLowerCase();
        String password = request.getParameter("password");
        User user = userDao.findByUsername(username);
        if (user == null || password == null || !PasswordUtil.matches(password, user.getPassword())) {
            request.setAttribute("error", "Those credentials do not match an account.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        user.setPassword(null);
        HttpSession session = request.getSession();
        session.setAttribute(Sessions.LOGGED_USER, user);
        Sessions.redirectHome(request, response, user);
    }
}
