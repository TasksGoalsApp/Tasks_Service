package com.example.Task.Service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    private long taskId;
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
    private List<SubTask> subTaskList;
}