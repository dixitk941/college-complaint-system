package com.college.ccms.dao;

import com.college.ccms.model.Role;
import com.college.ccms.model.User;
import com.college.ccms.util.HibernateTransaction;
import java.time.LocalDateTime;

public class UserDao {

    public void save(final User user) {
        HibernateTransaction.run(session -> {
            if (user.getCreatedAt() == null) {
                user.setCreatedAt(LocalDateTime.now());
            }
            session.save(user);
            return null;
        });
    }

    public User findByUsername(final String username) {
        return HibernateTransaction.run(session -> session
                .createQuery("from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult());
    }

    public boolean usernameTaken(final String username) {
        return count("u.username = :value", username) > 0;
    }

    public boolean emailTaken(final String email) {
        return count("u.email = :value", email) > 0;
    }

    public boolean rollTaken(final String rollNumber) {
        return count("u.rollNumber = :value", rollNumber) > 0;
    }

    public long countByRole(final Role role) {
        Long count = HibernateTransaction.run(session -> session
                .createQuery("select count(u.id) from User u where u.role = :role", Long.class)
                .setParameter("role", role)
                .uniqueResult());
        return count == null ? 0L : count.longValue();
    }

    private long count(final String clause, final String value) {
        Long count = HibernateTransaction.run(session -> session
                .createQuery("select count(u.id) from User u where " + clause, Long.class)
                .setParameter("value", value)
                .uniqueResult());
        return count == null ? 0L : count.longValue();
    }
}
