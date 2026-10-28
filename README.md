# College Complaint Management System

Advanced Internet Programming project. Students file campus complaints. An administrator updates the status and writes a remark on the same ticket.

The browser talks to **servlets**. Pages are **JSP**. Login is stored in the **HttpSession**. Database work uses one Hibernate **SessionFactory** and a short **Session** for each transaction.

## Lab accounts

Created automatically the first time the app connects to MySQL.

| Who | Username | Password |
| --- | --- | --- |
| Administrator | `admin` | `admin123` |
| Student | `student` | `student123` |

Passwords are stored as a SHA-256 hash. Three sample complaints are inserted when the complaints table is empty.

Change the college name in `src/main/java/com/college/ccms/util/AppConstants.java`.

## What you need

- JDK 8, 11, or 17
- MySQL 8 (XAMPP or a normal MySQL install)
- Eclipse IDE for Enterprise Java and Web Developers, with Apache Tomcat **9**

Use Tomcat 9. Tomcat 10 and 11 use `jakarta.servlet`, and this project uses `javax.servlet`.

## Database password

Open `src/main/resources/hibernate.cfg.xml`.

- XAMPP / WAMP: set `hibernate.connection.password` to empty
- Many lab PCs: user `root`, password `root` (this is the value already in the file)

`createDatabaseIfNotExist=true` creates the `college_cms` database. Hibernate `hbm2ddl.auto=update` creates the tables. `sql/schema.sql` is the same design, for the report or for creating the database by hand.

The server console prints each SQL statement (`hibernate.show_sql`). File a complaint during the viva and point at the `INSERT`.

## Run in Eclipse

1. Start MySQL.
2. File → Import → Maven → Existing Maven Projects → select this folder.
3. Wait until Maven finishes downloading Hibernate, the MySQL driver, and JSTL.
4. Right-click the project → Run As → Run on Server → Apache Tomcat 9.
5. Open the URL Eclipse shows. The context path is usually `/ccms`.

## Run without Eclipse

Install Maven, start MySQL, then from this folder:

```bash
mvn jetty:run
```

Open http://localhost:8080/ccms/

## Modules

- Register and sign in. The logged-in user is kept in the HttpSession.
- A student files a complaint, edits it while it is open, withdraws it, and reads remarks.
- An administrator filters the list, changes status and priority, and adds a remark.
- `AuthFilter` stops a student from opening the admin desk.
- A student can open only their own complaint. The check is in `ComplaintDetailServlet`.
- Complaints are not deleted. Withdrawn and rejected tickets stay in the database.

## Where the Hibernate concepts are

| Idea | File |
| --- | --- |
| SessionFactory, built once at startup | `util/HibernateUtil.java` |
| Session, beginTransaction, commit, rollback, close | `util/HibernateTransaction.java` |
| Startup and shutdown | `listener/AppContextListener.java` |
| Connection, dialect, `hbm2ddl` | `src/main/resources/hibernate.cfg.xml` |
| Tables as classes | `model/User.java`, `model/Complaint.java`, `model/ComplaintRemark.java` |
| HQL queries | `dao/UserDao.java`, `dao/ComplaintDao.java` |

`SessionFactory` is heavy and thread-safe, so there is one for the whole application. A `Session` is one unit of work and is not thread-safe, so each database call opens a session, commits or rolls back, and closes it in `finally`.

`getCurrentSession()` is also on `HibernateUtil`. It needs `hibernate.current_session_context_class=thread` and an open transaction. The DAOs use `openSession()` so the `close()` call is easy to see.

The HttpSession and the Hibernate Session are different. The HttpSession remembers who is logged in. The Hibernate Session talks to MySQL.

## Tables

- `users` — students and the administrator
- `complaints` — one row per ticket (`CMS-year-number`)
- `complaint_remarks` — each status change and note

A complaint belongs to one student. A complaint has many remarks.

## Viva points

1. Why is the SessionFactory created in a listener? So it is built once, when the web app starts, and closed when the server stops.
2. What does `transaction.rollback()` do? It cancels the unit of work if saving fails.
3. What does `hbm2ddl.auto=update` do? It creates or alters tables to match the entity classes. It does not drop them.
4. How does the detail page avoid `LazyInitializationException`? `findById` uses `join fetch` for the student, the remarks, and each remark's author before the session closes.
5. Where is authorization? `AuthFilter` checks the role. `ComplaintDetailServlet` checks that a student owns the ticket.
