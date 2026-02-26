package com.example.Task.Service.domain.subtasksRequestsResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSubTaskResponse {
    private long subTask_id;
    private String subTask_title;
    private boolean subTask_completed;
    private LocalDate completedDate;

}
