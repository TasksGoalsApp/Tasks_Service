package com.example.Task.Service.business;

import com.example.Task.Service.domain.tasksRequestsResponse.GetAllTaskByUserResponse;
import com.example.Task.Service.domain.tasksRequestsResponse.GetTasksByUserRequest;


public interface IGetAllTasksByUser {

GetAllTaskByUserResponse getAllTaskByUser(long userId);
}
