-- Easiest path: start MySQL, then start the web app.
-- hibernate.hbm2ddl.auto=update creates the tables for you.
-- Run this file when you want the database created ahead of time,
-- or when you need the table design for the project report.

CREATE DATABASE IF NOT EXISTS college_cms
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE college_cms;

CREATE TABLE IF NOT EXISTS users (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    username      VARCHAR(30)  NOT NULL,
    password      VARCHAR(100) NOT NULL,
    full_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(120) NOT NULL,
    roll_number   VARCHAR(20)  NULL,
    department    VARCHAR(80)  NULL,
    year_of_study VARCHAR(20)  NULL,
    phone         VARCHAR(15)  NULL,
    `role`        VARCHAR(20)  NOT NULL,
    created_at    DATETIME     NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_username (username),
    UNIQUE KEY uk_users_email (email),
    UNIQUE KEY uk_users_roll (roll_number)
);

CREATE TABLE IF NOT EXISTS complaints (
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    ticket_no   VARCHAR(20)   NOT NULL,
    student_id  BIGINT        NOT NULL,
    title       VARCHAR(120)  NOT NULL,
    description VARCHAR(2000) NOT NULL,
    category    VARCHAR(40)   NOT NULL,
    priority    VARCHAR(20)   NOT NULL,
    status      VARCHAR(20)   NOT NULL,
    location    VARCHAR(120)  NULL,
    created_at  DATETIME      NULL,
    updated_at  DATETIME      NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_complaints_ticket (ticket_no),
    KEY idx_complaints_status (status),
    KEY idx_complaints_student (student_id),
    CONSTRAINT fk_complaints_student FOREIGN KEY (student_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS complaint_remarks (
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    complaint_id BIGINT        NOT NULL,
    author_id    BIGINT        NOT NULL,
    message      VARCHAR(1000) NOT NULL,
    old_status   VARCHAR(20)   NULL,
    new_status   VARCHAR(20)   NULL,
    created_at   DATETIME      NULL,
    PRIMARY KEY (id),
    KEY idx_remarks_complaint (complaint_id),
    CONSTRAINT fk_remarks_complaint FOREIGN KEY (complaint_id) REFERENCES complaints (id),
    CONSTRAINT fk_remarks_author FOREIGN KEY (author_id) REFERENCES users (id)
);
