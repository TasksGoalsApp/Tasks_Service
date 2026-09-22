package com.example.Task.Service.domain.subtasksRequestsResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSubTaskRequest {
    @NotNull(message = "Subtask ID is required")
    private long subTask_id;

    @NotBlank(message = "Subtask title is required")
    private String subTask_title;

    @NotNull(message = "Subtask completion status is required")
    private boolean subTask_completed;

    @NotBlank
    private LocalDate completedDate;



}
