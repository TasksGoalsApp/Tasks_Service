package com.example.Task.Service.controller;

import com.example.Task.Service.business.ICreateTask;
import com.example.Task.Service.business.IGetAllTasksByUser;
import com.example.Task.Service.business.IUpdateTask;
import com.example.Task.Service.domain.tasksRequestsResponse.*;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/tasks")
@RestController
public class TaskController {
    @Autowired
    private ICreateTask createTask;
    @Autowired
    private IUpdateTask updateTask;
    @Autowired
    private IGetAllTasksByUser getAllTasksByUser;


    @PostMapping
    @PermitAll
    public ResponseEntity<CreateTaskResponse> createTask(@RequestBody @Valid CreateTaskRequest createTaskRequest) {
        CreateTaskResponse createTaskResponse = createTask.createTask(createTaskRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createTaskResponse);
    }

    @PutMapping
    public ResponseEntity<UpdateTaskResponse> updateTask(@RequestBody @Valid UpdateTaskRequest updateTaskRequest) {
        UpdateTaskResponse updateTaskResponse = updateTask.updateTask(updateTaskRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateTaskResponse);

    }
    @GetMapping("/user/{userId}")
    @PermitAll
    public ResponseEntity<GetAllTaskByUserResponse> showTasks(@RequestBody GetTasksByUserRequest request) {
        GetAllTaskByUserResponse tasks = getAllTasksByUser.getAllTaskByUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }



}
