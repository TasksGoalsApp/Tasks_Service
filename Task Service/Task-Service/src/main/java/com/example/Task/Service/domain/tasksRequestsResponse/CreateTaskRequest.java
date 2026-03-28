package com.example.Task.Service.domain.tasksRequestsResponse;

import com.example.Task.Service.domain.TaskPriority;
import com.example.Task.Service.domain.TaskScheduleType;
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
public class CreateTaskRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Long userId;

    @NotNull
    private LocalDate start_date;

    @NotNull
    private LocalDate end_date;

    private LocalTime start_time;
    private LocalTime end_time;

    @NotNull
    private TaskPriority task_priority;

    @NotNull
    private TaskScheduleType schedule_type;
}