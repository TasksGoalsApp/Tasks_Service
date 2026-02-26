package com.example.Task.Service.domain.tasksRequestsResponse;

import com.example.Task.Service.domain.TaskPriority;
import com.example.Task.Service.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTaskRequest {

    @NotNull
    private long taskId;
    @NotBlank
    private String task_title;
    @NotBlank
    private String task_description;
    @NotBlank
    private TaskStatus task_status;
    @NotBlank
    private TaskPriority task_priority;
    @NotNull
    private LocalDate task_day;

    @NotNull
    private LocalTime start_time;

    @NotNull
    private LocalTime end_time;

    private boolean weekly;
}
