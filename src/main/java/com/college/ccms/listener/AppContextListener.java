package com.college.ccms.listener;

import com.college.ccms.model.ComplaintCategory;
import com.college.ccms.model.ComplaintStatus;
import com.college.ccms.model.Priority;
import com.college.ccms.util.AppConstants;
import com.college.ccms.util.DataSeeder;
import com.college.ccms.util.HibernateUtil;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        context.setAttribute("collegeName", AppConstants.COLLEGE_NAME);
        context.setAttribute("appName", AppConstants.APP_NAME);
        context.setAttribute("departments", AppConstants.DEPARTMENTS);
        context.setAttribute("years", AppConstants.YEARS);
        context.setAttribute("categories", ComplaintCategory.values());
        context.setAttribute("priorities", Priority.values());
        context.setAttribute("statuses", ComplaintStatus.values());
        try {
            HibernateUtil.buildSessionFactory();
            new DataSeeder().seed();
            context.log("Hibernate SessionFactory is ready.");
        } catch (RuntimeException ex) {
            context.log("Could not open MySQL. Check hibernate.cfg.xml.", ex);
            throw new IllegalStateException(
                    "Database startup failed. Start MySQL and check src/main/resources/hibernate.cfg.xml.", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        HibernateUtil.shutdown();
    }
}
