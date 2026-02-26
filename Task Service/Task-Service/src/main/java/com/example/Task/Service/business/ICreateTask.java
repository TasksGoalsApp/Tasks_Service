package com.example.Task.Service.business;

import com.example.Task.Service.domain.tasksRequestsResponse.CreateTaskRequest;
import com.example.Task.Service.domain.tasksRequestsResponse.CreateTaskResponse;

public interface ICreateTask {
    CreateTaskResponse createTask(CreateTaskRequest request);
}
