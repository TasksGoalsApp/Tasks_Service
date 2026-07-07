package com.example.Task.Service.controller;

import com.example.Task.Service.business.ICreateTask;
import com.example.Task.Service.business.IGetAllTasksByUser;
import com.example.Task.Service.business.IUpdateTask;
import com.example.Task.Service.domain.tasksRequestsResponse.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("/tasks")
@RestController
public class TaskController {
    @Autowired
    private ICreateTask createTask;
    @Autowired
    private IUpdateTask updateTask;
    @Autowired
    private IGetAllTasksByUser getAllTasksByUser;

    @RolesAllowed({"Customer"})
    @PostMapping("/create")
    public ResponseEntity<CreateTaskResponse> createTask(@RequestBody @Valid CreateTaskRequest createTaskRequest, @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("id");
        CreateTaskResponse createTaskResponse = createTask.createTask(createTaskRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createTaskResponse);
    }

    @RolesAllowed({"Customer"})
    @PutMapping
    public ResponseEntity<UpdateTaskResponse> updateTask(@RequestBody @Valid UpdateTaskRequest updateTaskRequest) {
        UpdateTaskResponse updateTaskResponse = updateTask.updateTask(updateTaskRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateTaskResponse);

    }

    @RolesAllowed({"Customer"})
    @GetMapping("/user")
    public ResponseEntity<GetAllTaskByUserResponse> showTasks(@AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("id");
        GetAllTaskByUserResponse tasks = getAllTasksByUser.getAllTaskByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }



}
