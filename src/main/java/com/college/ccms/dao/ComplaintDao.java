package com.college.ccms.dao;

import com.college.ccms.model.Complaint;
import com.college.ccms.model.ComplaintCategory;
import com.college.ccms.model.ComplaintRemark;
import com.college.ccms.model.ComplaintStatus;
import com.college.ccms.model.Priority;
import com.college.ccms.model.User;
import com.college.ccms.util.HibernateTransaction;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.hibernate.query.Query;

public class ComplaintDao {

    public void save(final Complaint complaint, final Long studentId) {
        HibernateTransaction.run(session -> {
            User student = session.get(User.class, studentId);
            if (student == null) {
                throw new IllegalArgumentException("Student account was not found.");
            }
            LocalDateTime now = LocalDateTime.now();
            complaint.setStudent(student);
            complaint.setStatus(ComplaintStatus.OPEN);
            complaint.setCreatedAt(now);
            complaint.setUpdatedAt(now);
            complaint.setTicketNo(temporaryTicket());
            session.save(complaint);
            session.flush();
            complaint.setTicketNo(String.format("CMS-%d-%04d", now.getYear(), complaint.getId().longValue()));
            return null;
        });
    }

    public Complaint findById(final Long id) {
        return HibernateTransaction.run(session -> {
            List<Complaint> rows = session.createQuery(
                    "select distinct c from Complaint c "
                            + "join fetch c.student "
                            + "left join fetch c.remarks r "
                            + "left join fetch r.author "
                            + "where c.id = :id", Complaint.class)
                    .setParameter("id", id)
                    .list();
            if (rows.isEmpty()) {
                return null;
            }
            return rows.get(0);
        });
    }

    public List<Complaint> search(final Long studentId, final ComplaintStatus status,
                                  final ComplaintCategory category, final String keyword) {
        return HibernateTransaction.run(session -> {
            StringBuilder hql = new StringBuilder(
                    "select distinct c from Complaint c join fetch c.student where 1 = 1");
            if (studentId != null) {
                hql.append(" and c.student.id = :studentId");
            }
            if (status != null) {
                hql.append(" and c.status = :status");
            }
            if (category != null) {
                hql.append(" and c.category = :category");
            }
            boolean hasKeyword = keyword != null && keyword.trim().length() > 0;
            if (hasKeyword) {
                hql.append(" and (lower(c.title) like :kw or lower(c.ticketNo) like :kw ");
                hql.append("or lower(c.student.fullName) like :kw or lower(c.student.rollNumber) like :kw)");
            }
            hql.append(" order by c.createdAt desc, c.id desc");

            Query<Complaint> query = session.createQuery(hql.toString(), Complaint.class);
            if (studentId != null) {
                query.setParameter("studentId", studentId);
            }
            if (status != null) {
                query.setParameter("status", status);
            }
            if (category != null) {
                query.setParameter("category", category);
            }
            if (hasKeyword) {
                query.setParameter("kw", "%" + keyword.trim().toLowerCase() + "%");
            }
            return query.list();
        });
    }

    public List<Complaint> recent(final Long studentId, final int limit) {
        return HibernateTransaction.run(session -> {
            String hql = "select distinct c from Complaint c join fetch c.student";
            if (studentId != null) {
                hql += " where c.student.id = :studentId";
            }
            hql += " order by c.createdAt desc, c.id desc";
            Query<Complaint> query = session.createQuery(hql, Complaint.class);
            if (studentId != null) {
                query.setParameter("studentId", studentId);
            }
            query.setMaxResults(limit);
            return query.list();
        });
    }

    public Map<ComplaintStatus, Long> countByStatus(final Long studentId) {
        return HibernateTransaction.run(session -> {
            String hql = "select c.status, count(c.id) from Complaint c";
            if (studentId != null) {
                hql += " where c.student.id = :studentId";
            }
            hql += " group by c.status";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            if (studentId != null) {
                query.setParameter("studentId", studentId);
            }
            Map<ComplaintStatus, Long> counts = new EnumMap<ComplaintStatus, Long>(ComplaintStatus.class);
            for (ComplaintStatus status : ComplaintStatus.values()) {
                counts.put(status, Long.valueOf(0L));
            }
            for (Object[] row : query.list()) {
                counts.put((ComplaintStatus) row[0], (Long) row[1]);
            }
            return counts;
        });
    }

    public long countAll() {
        Long count = HibernateTransaction.run(session -> session
                .createQuery("select count(c.id) from Complaint c", Long.class)
                .uniqueResult());
        return count == null ? 0L : count.longValue();
    }

    public boolean updateOpen(final Long complaintId, final Long studentId, final String title,
                              final String description, final ComplaintCategory category,
                              final Priority priority, final String location) {
        Boolean updated = HibernateTransaction.run(session -> {
            Complaint complaint = ownedOpen(session, complaintId, studentId);
            if (complaint == null) {
                return Boolean.FALSE;
            }
            complaint.setTitle(title);
            complaint.setDescription(description);
            complaint.setCategory(category);
            complaint.setPriority(priority);
            complaint.setLocation(location);
            complaint.setUpdatedAt(LocalDateTime.now());
            return Boolean.TRUE;
        });
        return Boolean.TRUE.equals(updated);
    }

    public boolean withdraw(final Long complaintId, final Long studentId) {
        Boolean updated = HibernateTransaction.run(session -> {
            Complaint complaint = ownedOpen(session, complaintId, studentId);
            if (complaint == null) {
                return Boolean.FALSE;
            }
            User student = session.get(User.class, studentId);
            addRemark(complaint, student, "Complaint withdrawn by the student.",
                    complaint.getStatus(), ComplaintStatus.WITHDRAWN);
            complaint.setStatus(ComplaintStatus.WITHDRAWN);
            complaint.setUpdatedAt(LocalDateTime.now());
            return Boolean.TRUE;
        });
        return Boolean.TRUE.equals(updated);
    }

    public boolean addAction(final Long complaintId, final Long authorId, final String message,
                             final ComplaintStatus newStatus, final Priority newPriority) {
        Boolean updated = HibernateTransaction.run(session -> {
            Complaint complaint = session.get(Complaint.class, complaintId);
            User author = session.get(User.class, authorId);
            if (complaint == null || author == null) {
                return Boolean.FALSE;
            }
            ComplaintStatus oldStatus = complaint.getStatus();
            if (newStatus != null) {
                complaint.setStatus(newStatus);
            }
            if (newPriority != null) {
                complaint.setPriority(newPriority);
            }
            complaint.setUpdatedAt(LocalDateTime.now());
            addRemark(complaint, author, message, oldStatus, complaint.getStatus());
            return Boolean.TRUE;
        });
        return Boolean.TRUE.equals(updated);
    }

    private Complaint ownedOpen(org.hibernate.Session session, Long complaintId, Long studentId) {
        Complaint complaint = session.get(Complaint.class, complaintId);
        if (complaint == null || complaint.getStudent() == null) {
            return null;
        }
        if (!studentId.equals(complaint.getStudent().getId())) {
            return null;
        }
        if (complaint.getStatus() != ComplaintStatus.OPEN) {
            return null;
        }
        return complaint;
    }

    private void addRemark(Complaint complaint, User author, String message,
                           ComplaintStatus oldStatus, ComplaintStatus newStatus) {
        ComplaintRemark remark = new ComplaintRemark();
        remark.setComplaint(complaint);
        remark.setAuthor(author);
        remark.setMessage(message);
        remark.setOldStatus(oldStatus);
        remark.setNewStatus(newStatus);
        remark.setCreatedAt(LocalDateTime.now());
        complaint.getRemarks().add(remark);
    }

    private String temporaryTicket() {
        return "N" + UUID.randomUUID().toString().replace("-", "").substring(0, 18);
    }
}
