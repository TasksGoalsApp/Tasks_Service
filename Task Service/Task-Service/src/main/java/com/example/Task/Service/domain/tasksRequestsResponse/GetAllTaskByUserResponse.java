package com.example.Task.Service.domain.tasksRequestsResponse;

import com.example.Task.Service.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllTaskByUserResponse {
    List<Task> tasks;
}
