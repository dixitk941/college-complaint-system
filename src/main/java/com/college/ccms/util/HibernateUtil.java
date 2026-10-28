package com.college.ccms.util;

import com.college.ccms.model.Complaint;
import com.college.ccms.model.ComplaintRemark;
import com.college.ccms.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Builds one SessionFactory for the whole web application and opens Sessions from it.
 *
 * SessionFactory is expensive and thread-safe. Create it once at startup and close it
 * when the server stops. A Session is one short unit of work: open it, begin a
 * transaction, commit or roll back, then close it. Do not store a Session in the
 * HttpSession. The HttpSession only remembers who is logged in.
 */
public final class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static void buildSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            return;
        }
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Complaint.class);
        configuration.addAnnotatedClass(ComplaintRemark.class);

        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        try {
            sessionFactory = configuration.buildSessionFactory(registry);
        } catch (RuntimeException ex) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw ex;
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            buildSessionFactory();
        }
        return sessionFactory;
    }

    /** A new Session for one request. The caller must close it. */
    public static Session openSession() {
        return getSessionFactory().openSession();
    }

    /**
     * Session bound to the current thread. Call this only after beginTransaction().
     * This project uses openSession() so the close() call stays visible in HibernateTransaction.
     */
    public static Session getCurrentSession() {
        return getSessionFactory().getCurrentSession();
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
