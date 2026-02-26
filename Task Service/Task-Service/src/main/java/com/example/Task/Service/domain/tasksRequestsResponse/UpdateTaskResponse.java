package com.example.Task.Service.domain.tasksRequestsResponse;

import com.example.Task.Service.domain.TaskPriority;
import com.example.Task.Service.domain.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class UpdateTaskResponse {
    private long task_id;
    private long user_id;
    private String task_title;
    private String task_description;
    private TaskStatus task_status;
    private TaskPriority task_priority;
    private LocalDate task_day;
    private LocalTime start_time;
    private LocalTime end_time;
    private boolean weekly;
}
