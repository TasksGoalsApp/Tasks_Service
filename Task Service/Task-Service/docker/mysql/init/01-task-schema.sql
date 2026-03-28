CREATE DATABASE IF NOT EXISTS task_service_db;
USE task_service_db;

DROP TABLE IF EXISTS sub_tasks;
DROP TABLE IF EXISTS tasks;

CREATE TABLE IF NOT EXISTS tasks (
                                     task_id BIGINT NOT NULL AUTO_INCREMENT,
                                     user_id BIGINT NOT NULL,
                                     title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    task_status VARCHAR(32) NOT NULL DEFAULT 'TODO',
    task_priority VARCHAR(32) NOT NULL DEFAULT 'MEDIUM',
    schedule_type VARCHAR(32) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    start_time TIME NULL,
    end_time TIME NULL,

    PRIMARY KEY (task_id),

    INDEX idx_tasks_user_id (user_id),
    INDEX idx_tasks_start_date (start_date),
    INDEX idx_tasks_end_date (end_date),
    INDEX idx_tasks_schedule_type (schedule_type),

    CONSTRAINT chk_task_status
    CHECK (task_status IN ('TODO', 'IN_PROGRESS', 'DONE', 'BLOCKED', 'CANCELED')),

    CONSTRAINT chk_task_priority
    CHECK (task_priority IN ('LOW', 'MEDIUM', 'HIGH', 'URGENT')),

    CONSTRAINT chk_schedule_type
    CHECK (schedule_type IN ('FIXED_TIME', 'ALL_DAY', 'WEEK_RANGE')),

    CONSTRAINT chk_task_dates
    CHECK (end_date >= start_date),

    CONSTRAINT chk_task_time_order
    CHECK (
              start_time IS NULL
              OR end_time IS NULL
              OR start_time < end_time
          )
    );

CREATE TABLE IF NOT EXISTS sub_tasks (
                                         subtask_id BIGINT NOT NULL AUTO_INCREMENT,
                                         task_id BIGINT NOT NULL,
                                         subtask_title VARCHAR(50) NOT NULL,
    subtask_completed BOOLEAN NOT NULL DEFAULT FALSE,
    completedDate DATE NULL,

    PRIMARY KEY (subtask_id),

    INDEX idx_sub_tasks_task_id (task_id),

    CONSTRAINT fk_subtask_task
    FOREIGN KEY (task_id)
    REFERENCES tasks(task_id)
    ON DELETE CASCADE
    );