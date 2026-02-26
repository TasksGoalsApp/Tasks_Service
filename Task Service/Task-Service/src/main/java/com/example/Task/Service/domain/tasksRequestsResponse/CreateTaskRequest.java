package com.example.Task.Service.domain.tasksRequestsResponse;

import com.example.Task.Service.domain.TaskType;
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
    private long userId;
    @NotNull
    private LocalDate task_day;

    @NotNull
    private LocalTime start_time;

    @NotNull
    private LocalTime end_time;

    private boolean weekly;



}
