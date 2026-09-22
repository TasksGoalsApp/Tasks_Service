package com.example.Task.Service.business;

import com.example.Task.Service.domain.subtasksRequestsResponse.GetSubTasksByTaskIdRequest;
import com.example.Task.Service.domain.subtasksRequestsResponse.GetSubTasksByTaskIdResponse;

public interface IGetSubTasksByTaskId {
    GetSubTasksByTaskIdResponse getSubTasksByTaskId(long taskId, Long userId);
}
