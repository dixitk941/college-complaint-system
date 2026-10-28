package com.college.ccms.servlet;

import com.college.ccms.dao.ComplaintDao;
import com.college.ccms.model.Complaint;
import com.college.ccms.model.ComplaintStatus;
import com.college.ccms.model.User;
import com.college.ccms.util.Flashes;
import com.college.ccms.util.Ids;
import com.college.ccms.util.Sessions;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ComplaintDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDao complaintDao = new ComplaintDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = Sessions.currentUser(request);
        Long id = Ids.parse(request.getParameter("id"));
        Complaint complaint = id == null ? null : complaintDao.findById(id);
        if (!canView(user, complaint)) {
            Flashes.error(request, "That complaint could not be found.");
            redirectToList(request, response, user);
            return;
        }
        request.setAttribute("complaint", complaint);
        request.setAttribute("adminStatuses", Arrays.asList(
                ComplaintStatus.OPEN,
                ComplaintStatus.IN_PROGRESS,
                ComplaintStatus.RESOLVED,
                ComplaintStatus.REJECTED));
        request.getRequestDispatcher("/WEB-INF/jsp/complaint-detail.jsp").forward(request, response);
    }

    private boolean canView(User user, Complaint complaint) {
        if (user == null || complaint == null || complaint.getStudent() == null) {
            return false;
        }
        if (user.isAdmin()) {
            return true;
        }
        return user.getId().equals(complaint.getStudent().getId());
    }

    private void redirectToList(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {
        String context = request.getContextPath();
        if (user != null && user.isAdmin()) {
            response.sendRedirect(context + "/admin/complaints");
        } else {
            response.sendRedirect(context + "/student/complaints");
        }
    }
}
