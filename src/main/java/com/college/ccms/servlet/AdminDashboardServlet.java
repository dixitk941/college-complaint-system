package com.college.ccms.servlet;

import com.college.ccms.dao.ComplaintDao;
import com.college.ccms.dao.UserDao;
import com.college.ccms.model.ComplaintStatus;
import com.college.ccms.model.Role;
import com.college.ccms.web.StatusCount;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDao complaintDao = new ComplaintDao();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<ComplaintStatus, Long> counts = complaintDao.countByStatus(null);
        long total = 0L;
        for (Long value : counts.values()) {
            total += value.longValue();
        }
        request.setAttribute("statusCounts", StatusCount.list(counts));
        request.setAttribute("totalComplaints", Long.valueOf(total));
        request.setAttribute("recentComplaints", complaintDao.recent(null, 8));
        request.setAttribute("studentCount", Long.valueOf(userDao.countByRole(Role.STUDENT)));
        request.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(request, response);
    }
}
