package com.college.ccms.servlet;

import com.college.ccms.dao.ComplaintDao;
import com.college.ccms.model.User;
import com.college.ccms.util.Flashes;
import com.college.ccms.util.Ids;
import com.college.ccms.util.Sessions;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WithdrawComplaintServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDao complaintDao = new ComplaintDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = Sessions.currentUser(request);
        Long id = Ids.parse(request.getParameter("id"));
        if (id == null) {
            Flashes.error(request, "That complaint could not be found.");
            response.sendRedirect(request.getContextPath() + "/student/complaints");
            return;
        }
        if (complaintDao.withdraw(id, user.getId())) {
            Flashes.success(request, "Complaint withdrawn.");
        } else {
            Flashes.error(request, "Only an open complaint can be withdrawn.");
        }
        response.sendRedirect(request.getContextPath() + "/complaint?id=" + id);
    }
}
