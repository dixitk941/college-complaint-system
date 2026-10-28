package com.college.ccms.servlet;

import com.college.ccms.dao.ComplaintDao;
import com.college.ccms.model.Complaint;
import com.college.ccms.model.ComplaintStatus;
import com.college.ccms.model.Priority;
import com.college.ccms.model.User;
import com.college.ccms.util.Enums;
import com.college.ccms.util.Flashes;
import com.college.ccms.util.Ids;
import com.college.ccms.util.Sessions;
import com.college.ccms.util.Text;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminComplaintActionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDao complaintDao = new ComplaintDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = Sessions.currentUser(request);
        Long id = Ids.parse(request.getParameter("id"));
        String context = request.getContextPath();
        if (id == null) {
            Flashes.error(request, "That complaint could not be found.");
            response.sendRedirect(context + "/admin/complaints");
            return;
        }

        Complaint complaint = complaintDao.findById(id);
        if (complaint == null) {
            Flashes.error(request, "That complaint could not be found.");
            response.sendRedirect(context + "/admin/complaints");
            return;
        }
        if (complaint.isWithdrawn()) {
            Flashes.error(request, "A withdrawn complaint stays closed.");
            response.sendRedirect(context + "/complaint?id=" + id);
            return;
        }

        String remark = Text.trim(request.getParameter("remark"));
        ComplaintStatus status = Enums.status(request.getParameter("status"));
        Priority priority = Enums.priority(request.getParameter("priority"));
        if (remark.length() < 5 || remark.length() > 1000) {
            Flashes.error(request, "Write a remark of 5 to 1000 characters.");
            response.sendRedirect(context + "/complaint?id=" + id);
            return;
        }
        if (status == null || status == ComplaintStatus.WITHDRAWN) {
            Flashes.error(request, "Select a valid status.");
            response.sendRedirect(context + "/complaint?id=" + id);
            return;
        }

        complaintDao.addAction(id, user.getId(), remark, status, priority);
        Flashes.success(request, "Update saved on " + complaint.getTicketNo() + ".");
        response.sendRedirect(context + "/complaint?id=" + id);
    }
}
