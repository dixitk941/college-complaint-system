package com.college.ccms.servlet;

import com.college.ccms.dao.ComplaintDao;
import com.college.ccms.model.Complaint;
import com.college.ccms.model.ComplaintCategory;
import com.college.ccms.model.Priority;
import com.college.ccms.model.User;
import com.college.ccms.util.ComplaintValidator;
import com.college.ccms.util.Enums;
import com.college.ccms.util.Flashes;
import com.college.ccms.util.Sessions;
import com.college.ccms.util.Text;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ComplaintFormServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDao complaintDao = new ComplaintDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/student/complaint-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = Sessions.currentUser(request);
        String title = Text.trim(request.getParameter("title"));
        String description = Text.trim(request.getParameter("description"));
        String location = Text.trimToNull(request.getParameter("location"));
        ComplaintCategory category = Enums.category(request.getParameter("category"));
        Priority priority = Enums.priority(request.getParameter("priority"));

        String error = validate(title, description, location, category, priority);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/jsp/student/complaint-form.jsp").forward(request, response);
            return;
        }

        Complaint complaint = new Complaint();
        complaint.setTitle(title);
        complaint.setDescription(description);
        complaint.setLocation(location);
        complaint.setCategory(category);
        complaint.setPriority(priority);
        complaintDao.save(complaint, user.getId());

        Flashes.success(request, "Complaint " + complaint.getTicketNo() + " has been filed.");
        response.sendRedirect(request.getContextPath() + "/complaint?id=" + complaint.getId());
    }

    static String validate(String title, String description, String location,
                           ComplaintCategory category, Priority priority) {
        if (category == null || priority == null) {
            return "Select a category and a priority.";
        }
        return ComplaintValidator.validate(title, description, location);
    }
}
