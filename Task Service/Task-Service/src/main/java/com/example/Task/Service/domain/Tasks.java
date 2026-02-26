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
public class Tasks {

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
private List<SubTasks> subTasksList;


}