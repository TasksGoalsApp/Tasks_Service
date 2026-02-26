package com.example.Task.Service.domain.tasksRequestsResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTaskResponse {
    private long taskId;
}
