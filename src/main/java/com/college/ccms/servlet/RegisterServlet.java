package com.college.ccms.servlet;

import com.college.ccms.dao.UserDao;
import com.college.ccms.model.Role;
import com.college.ccms.model.User;
import com.college.ccms.util.AppConstants;
import com.college.ccms.util.Flashes;
import com.college.ccms.util.PasswordUtil;
import com.college.ccms.util.Sessions;
import com.college.ccms.util.Text;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {

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
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String error = validate(request);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setFullName(Text.trim(request.getParameter("fullName")));
        user.setUsername(Text.trim(request.getParameter("username")).toLowerCase());
        user.setEmail(Text.trim(request.getParameter("email")).toLowerCase());
        user.setRollNumber(Text.trim(request.getParameter("rollNumber")));
        user.setDepartment(Text.trim(request.getParameter("department")));
        user.setYearOfStudy(Text.trim(request.getParameter("yearOfStudy")));
        user.setPhone(Text.trimToNull(request.getParameter("phone")));
        user.setPassword(PasswordUtil.hash(request.getParameter("password")));
        user.setRole(Role.STUDENT);

        try {
            userDao.save(user);
        } catch (RuntimeException ex) {
            request.setAttribute("error", "Could not create the account. Try a different username, email, or roll number.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        Flashes.success(request, "Account created. Sign in with your username.");
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private String validate(HttpServletRequest request) {
        String fullName = Text.trim(request.getParameter("fullName"));
        String username = Text.trim(request.getParameter("username")).toLowerCase();
        String email = Text.trim(request.getParameter("email")).toLowerCase();
        String rollNumber = Text.trim(request.getParameter("rollNumber"));
        String department = Text.trim(request.getParameter("department"));
        String year = Text.trim(request.getParameter("yearOfStudy"));
        String phone = Text.trim(request.getParameter("phone"));
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirmPassword");

        if (fullName.length() < 3 || fullName.length() > 100) {
            return "Enter your full name (3 to 100 characters).";
        }
        if (!username.matches("[a-z0-9._]{4,30}")) {
            return "Username must be 4 to 30 characters and use only letters, digits, dots, or underscores.";
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") || email.length() > 120) {
            return "Enter a valid email address.";
        }
        if (rollNumber.length() < 3 || rollNumber.length() > 20) {
            return "Enter a roll number (3 to 20 characters).";
        }
        if (!AppConstants.DEPARTMENTS.contains(department)) {
            return "Select your department.";
        }
        if (!AppConstants.YEARS.contains(year)) {
            return "Select your year of study.";
        }
        if (!phone.isEmpty() && !phone.matches("\\d{10}")) {
            return "Enter a 10-digit mobile number, or leave the phone field empty.";
        }
        if (password == null || password.length() < 6 || password.length() > 72) {
            return "Password must be 6 to 72 characters.";
        }
        if (!password.equals(confirm)) {
            return "Password and confirmation do not match.";
        }
        if (userDao.usernameTaken(username)) {
            return "That username is already taken.";
        }
        if (userDao.emailTaken(email)) {
            return "That email is already registered.";
        }
        if (userDao.rollTaken(rollNumber)) {
            return "That roll number is already registered.";
        }
        return null;
    }
}
