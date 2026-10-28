package com.college.ccms.servlet;

import com.college.ccms.dao.ComplaintDao;
import com.college.ccms.model.ComplaintStatus;
import com.college.ccms.model.User;
import com.college.ccms.util.Enums;
import com.college.ccms.util.Sessions;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyComplaintsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDao complaintDao = new ComplaintDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = Sessions.currentUser(request);
        ComplaintStatus status = Enums.status(request.getParameter("status"));
        request.setAttribute("complaints", complaintDao.search(user.getId(), status, null, null));
        request.setAttribute("selectedStatus", status == null ? "" : status.name());
        request.getRequestDispatcher("/WEB-INF/jsp/student/complaints.jsp").forward(request, response);
    }
}
