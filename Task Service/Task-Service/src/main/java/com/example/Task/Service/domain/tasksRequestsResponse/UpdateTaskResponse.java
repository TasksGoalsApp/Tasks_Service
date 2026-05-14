package com.example.Task.Service.domain.tasksRequestsResponse;

import com.example.Task.Service.domain.TaskPriority;
import com.example.Task.Service.domain.TaskScheduleType;
import com.example.Task.Service.domain.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class UpdateTaskResponse {
    private long task_id;
    private long userId;
    private String taskTitle;
    private String taskDescription;
    private TaskStatus taskStatus;
    private TaskPriority taskPriority;
    private TaskScheduleType scheduleType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
}