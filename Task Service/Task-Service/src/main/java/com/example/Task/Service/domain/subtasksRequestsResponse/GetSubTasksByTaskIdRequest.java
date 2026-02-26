package com.example.Task.Service.domain.subtasksRequestsResponse;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetSubTasksByTaskIdRequest {
    @NotNull
    private long taskId;

}
