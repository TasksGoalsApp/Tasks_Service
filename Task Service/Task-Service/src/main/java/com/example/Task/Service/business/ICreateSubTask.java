package com.example.Task.Service.business;

import com.example.Task.Service.domain.subtasksRequestsResponse.CreateSubTaskRequest;
import com.example.Task.Service.domain.subtasksRequestsResponse.CreateSubTaskResponse;

public interface ICreateSubTask {
CreateSubTaskResponse createSubTask(CreateSubTaskRequest request, long userId);

}
