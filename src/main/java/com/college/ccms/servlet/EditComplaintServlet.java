package com.college.ccms.servlet;

import com.college.ccms.dao.ComplaintDao;
import com.college.ccms.model.Complaint;
import com.college.ccms.model.ComplaintCategory;
import com.college.ccms.model.Priority;
import com.college.ccms.model.User;
import com.college.ccms.util.Enums;
import com.college.ccms.util.Flashes;
import com.college.ccms.util.Ids;
import com.college.ccms.util.Sessions;
import com.college.ccms.util.Text;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditComplaintServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDao complaintDao = new ComplaintDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = Sessions.currentUser(request);
        Complaint complaint = owned(user, request.getParameter("id"));
        if (complaint == null) {
            Flashes.error(request, "That complaint could not be found.");
            response.sendRedirect(request.getContextPath() + "/student/complaints");
            return;
        }
        if (!complaint.isOpen()) {
            Flashes.error(request, "Only an open complaint can be edited.");
            response.sendRedirect(request.getContextPath() + "/complaint?id=" + complaint.getId());
            return;
        }
        request.setAttribute("complaint", complaint);
        request.getRequestDispatcher("/WEB-INF/jsp/student/complaint-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = Sessions.currentUser(request);
        Long id = Ids.parse(request.getParameter("id"));
        Complaint complaint = id == null ? null : owned(user, id.toString());
        if (complaint == null) {
            Flashes.error(request, "That complaint could not be found.");
            response.sendRedirect(request.getContextPath() + "/student/complaints");
            return;
        }

        String title = Text.trim(request.getParameter("title"));
        String description = Text.trim(request.getParameter("description"));
        String location = Text.trimToNull(request.getParameter("location"));
        ComplaintCategory category = Enums.category(request.getParameter("category"));
        Priority priority = Enums.priority(request.getParameter("priority"));
        String error = ComplaintFormServlet.validate(title, description, location, category, priority);
        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("complaint", complaint);
            request.getRequestDispatcher("/WEB-INF/jsp/student/complaint-form.jsp").forward(request, response);
            return;
        }

        boolean updated = complaintDao.updateOpen(id, user.getId(), title, description, category, priority, location);
        if (!updated) {
            Flashes.error(request, "Only an open complaint can be edited.");
        } else {
            Flashes.success(request, "Complaint updated.");
        }
        response.sendRedirect(request.getContextPath() + "/complaint?id=" + id);
    }

    private Complaint owned(User user, String idRaw) {
        Long id = Ids.parse(idRaw);
        if (id == null) {
            return null;
        }
        Complaint complaint = complaintDao.findById(id);
        if (complaint == null || complaint.getStudent() == null) {
            return null;
        }
        if (!user.getId().equals(complaint.getStudent().getId())) {
            return null;
        }
        return complaint;
    }
}
