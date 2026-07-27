package com.example.Task.Service.business;

import com.example.Task.Service.domain.tasksRequestsResponse.UpdateTaskRequest;
import com.example.Task.Service.domain.tasksRequestsResponse.UpdateTaskResponse;

public interface IUpdateTask {
    UpdateTaskResponse updateTask(UpdateTaskRequest updateTaskRequest, Long userId);
}