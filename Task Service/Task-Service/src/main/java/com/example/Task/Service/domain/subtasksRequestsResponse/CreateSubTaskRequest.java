package com.example.Task.Service.domain.subtasksRequestsResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSubTaskRequest {
    @NotBlank(message = "Subtask title is required")
    private String subTask_title;

}
