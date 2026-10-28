package com.college.ccms.util;

import com.college.ccms.dao.ComplaintDao;
import com.college.ccms.dao.UserDao;
import com.college.ccms.model.Complaint;
import com.college.ccms.model.ComplaintCategory;
import com.college.ccms.model.ComplaintStatus;
import com.college.ccms.model.Priority;
import com.college.ccms.model.Role;
import com.college.ccms.model.User;

/** Creates the lab administrator, one student, and three sample complaints. */
public class DataSeeder {

    public void seed() {
        UserDao userDao = new UserDao();
        ComplaintDao complaintDao = new ComplaintDao();
        User admin = ensureAdmin(userDao);
        User student = ensureStudent(userDao);
        if (complaintDao.countAll() > 0) {
            return;
        }

        Complaint wifi = complaint(
                "Wi-Fi keeps dropping in Programming Lab 3",
                "The wireless network in Programming Lab 3 disconnects every few minutes during lab hours. "
                        + "Systems cannot stay connected to the compiler server, and the class has lost work twice this week.",
                ComplaintCategory.LABORATORY,
                Priority.HIGH,
                "CSE Block, Programming Lab 3");
        complaintDao.save(wifi, student.getId());

        Complaint cooler = complaint(
                "Library water cooler on the second floor is not working",
                "The water cooler outside the reference section on the second floor of the central library "
                        + "has been out of service since Monday. Students in the evening shift have no drinking water on that floor.",
                ComplaintCategory.LIBRARY,
                Priority.MEDIUM,
                "Central Library, Second Floor");
        complaintDao.save(cooler, student.getId());
        complaintDao.addAction(cooler.getId(), admin.getId(),
                "Estate office has been informed. A technician is scheduled for Thursday.",
                ComplaintStatus.IN_PROGRESS, null);

        Complaint exam = complaint(
                "Exam seating list was posted on the wrong notice board",
                "The seating arrangement for the internal assessment was placed on the old notice board near the canteen "
                        + "instead of the examination cell board. Several students reached the wrong room.",
                ComplaintCategory.EXAMINATION,
                Priority.LOW,
                "Examination Cell");
        complaintDao.save(exam, student.getId());
        complaintDao.addAction(exam.getId(), admin.getId(),
                "The examination cell moved the notice to the main board and will use that board for the rest of this term.",
                ComplaintStatus.RESOLVED, null);
    }

    private User ensureAdmin(UserDao userDao) {
        User admin = userDao.findByUsername("admin");
        if (admin != null) {
            return admin;
        }
        admin = new User();
        admin.setUsername("admin");
        admin.setPassword(PasswordUtil.hash("admin123"));
        admin.setFullName("System Administrator");
        admin.setEmail("admin@college.local");
        admin.setDepartment("Administration");
        admin.setRole(Role.ADMIN);
        userDao.save(admin);
        return admin;
    }

    private User ensureStudent(UserDao userDao) {
        User student = userDao.findByUsername("student");
        if (student != null) {
            return student;
        }
        student = new User();
        student.setUsername("student");
        student.setPassword(PasswordUtil.hash("student123"));
        student.setFullName("Ananya Rao");
        student.setEmail("ananya@college.local");
        student.setRollNumber("CSE2023001");
        student.setDepartment("Computer Science and Engineering");
        student.setYearOfStudy("Second Year");
        student.setPhone("9876543210");
        student.setRole(Role.STUDENT);
        userDao.save(student);
        return student;
    }

    private Complaint complaint(String title, String description, ComplaintCategory category,
                                Priority priority, String location) {
        Complaint complaint = new Complaint();
        complaint.setTitle(title);
        complaint.setDescription(description);
        complaint.setCategory(category);
        complaint.setPriority(priority);
        complaint.setLocation(location);
        return complaint;
    }
}
