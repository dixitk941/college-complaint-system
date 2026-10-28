package com.college.ccms.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Opens one Hibernate Session, runs one transaction, then closes the Session.
 * commit() writes the work. rollback() undoes it when anything fails.
 */
public final class HibernateTransaction {

    private static final Logger LOG = Logger.getLogger(HibernateTransaction.class.getName());

    private HibernateTransaction() {
    }

    public static <T> T run(Work<T> work) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            T result = work.doInSession(session);
            transaction.commit();
            return result;
        } catch (RuntimeException ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            LOG.log(Level.SEVERE, "Hibernate transaction rolled back", ex);
            throw ex;
        } finally {
            session.close();
        }
    }

    public interface Work<T> {
        T doInSession(Session session);
    }
}
