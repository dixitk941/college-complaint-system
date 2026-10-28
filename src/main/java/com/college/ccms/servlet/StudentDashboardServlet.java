package com.college.ccms.servlet;

import com.college.ccms.dao.ComplaintDao;
import com.college.ccms.model.ComplaintStatus;
import com.college.ccms.model.User;
import com.college.ccms.util.Sessions;
import com.college.ccms.web.StatusCount;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StudentDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDao complaintDao = new ComplaintDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = Sessions.currentUser(request);
        Map<ComplaintStatus, Long> counts = complaintDao.countByStatus(user.getId());
        request.setAttribute("statusCounts", StatusCount.list(counts));
        request.setAttribute("totalComplaints", Long.valueOf(total(counts)));
        request.setAttribute("recentComplaints", complaintDao.recent(user.getId(), 5));
        request.getRequestDispatcher("/WEB-INF/jsp/student/dashboard.jsp").forward(request, response);
    }

    private long total(Map<ComplaintStatus, Long> counts) {
        long total = 0L;
        for (Long value : counts.values()) {
            total += value.longValue();
        }
        return total;
    }
}
