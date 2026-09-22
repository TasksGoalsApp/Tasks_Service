package com.example.Task.Service.controller;

import com.example.Task.Service.business.ICreateSubTask;
import com.example.Task.Service.business.IDeleteSubTask;
import com.example.Task.Service.business.IGetSubTasksByTaskId;
import com.example.Task.Service.business.IUpdateSubTask;
import com.example.Task.Service.domain.subtasksRequestsResponse.*;
import jakarta.validation.Valid;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import com.example.Task.Service.security.JwtContract;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping("/tasks/subtasks")
@RestController
@RolesAllowed("CUSTOMER")
public class SubTaskController {

    private final ICreateSubTask  createSubTask;
    private final IDeleteSubTask deleteSubTask;
    private final IUpdateSubTask updateSubTask;
    private final IGetSubTasksByTaskId getSubTasksByTaskId;

    @PostMapping("/create")
    public ResponseEntity<CreateSubTaskResponse> createSubTask(@RequestBody @Valid CreateSubTaskRequest createSubTaskRequest, @AuthenticationPrincipal Jwt jwt) {
        CreateSubTaskResponse response = createSubTask.createSubTask(createSubTaskRequest, JwtContract.userId(jwt));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/delete")
    public ResponseEntity<UpdateSubTaskResponse> updateSubTask(@RequestBody @Valid UpdateSubTaskRequest request, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(updateSubTask.updateSubTask(request, JwtContract.userId(jwt)));
    }

    @DeleteMapping("/{subTaskId}")
    public ResponseEntity<Void> deleteSubTask(@PathVariable Long subTaskId, @AuthenticationPrincipal Jwt jwt) {
        deleteSubTask.deleteSubTask(subTaskId, JwtContract.userId(jwt));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<GetSubTasksByTaskIdResponse> getSubTasksByTaskId(@PathVariable Long taskId, @AuthenticationPrincipal Jwt jwt) {
        GetSubTasksByTaskIdResponse response = getSubTasksByTaskId.getSubTasksByTaskId(taskId, JwtContract.userId(jwt));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
