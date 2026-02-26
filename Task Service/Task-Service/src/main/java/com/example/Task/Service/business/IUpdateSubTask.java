package com.example.Task.Service.business;

import com.example.Task.Service.domain.subtasksRequestsResponse.UpdateSubTaskRequest;
import com.example.Task.Service.domain.subtasksRequestsResponse.UpdateSubTaskResponse;

public interface IUpdateSubTask {
    UpdateSubTaskResponse updateSubTask(UpdateSubTaskRequest updateSubTaskRequest);

}
