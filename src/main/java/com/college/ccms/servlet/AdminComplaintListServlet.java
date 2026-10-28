package com.college.ccms.servlet;

import com.college.ccms.dao.ComplaintDao;
import com.college.ccms.model.ComplaintCategory;
import com.college.ccms.model.ComplaintStatus;
import com.college.ccms.util.Enums;
import com.college.ccms.util.Text;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminComplaintListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDao complaintDao = new ComplaintDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ComplaintStatus status = Enums.status(request.getParameter("status"));
        ComplaintCategory category = Enums.category(request.getParameter("category"));
        String keyword = Text.trim(request.getParameter("q"));
        if (keyword.length() > 80) {
            keyword = keyword.substring(0, 80);
        }
        request.setAttribute("complaints", complaintDao.search(null, status, category, keyword));
        request.setAttribute("query", keyword);
        request.setAttribute("selectedStatus", status == null ? "" : status.name());
        request.setAttribute("selectedCategory", category == null ? "" : category.name());
        request.getRequestDispatcher("/WEB-INF/jsp/admin/complaints.jsp").forward(request, response);
    }
}
