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
    @NotNull
    private long subTask_id;
    @NotBlank
    private String subTask_title;
    @NotBlank
    private boolean subTask_completed;
    @NotBlank
    private LocalDate completedDate;



}
