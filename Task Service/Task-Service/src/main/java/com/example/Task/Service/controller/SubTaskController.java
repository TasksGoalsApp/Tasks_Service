package com.example.Task.Service.controller;

import com.example.Task.Service.business.ICreateSubTask;
import com.example.Task.Service.business.IDeleteSubTask;
import com.example.Task.Service.business.IGetSubTasksByTaskId;
import com.example.Task.Service.business.IUpdateSubTask;
import com.example.Task.Service.domain.subtasksRequestsResponse.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("/tasks/{taskId}/subtasks")
@RestController
public class SubTaskController {

    private final ICreateSubTask  createSubTask;
    private final IDeleteSubTask deleteSubTask;
    private final IUpdateSubTask updateSubTask;
    private final IGetSubTasksByTaskId getSubTasksByTaskId;

    @PostMapping
    public ResponseEntity<CreateSubTaskResponse> createSubTask(@PathVariable Long taskId, @RequestBody @Valid CreateSubTaskRequest createSubTaskRequest, @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("id");
        CreateSubTaskResponse response = createSubTask.createSubTask(createSubTaskRequest, userId, taskId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/{subTaskId}")
    public ResponseEntity<UpdateSubTaskResponse> updateSubTask(@PathVariable Long taskId, @PathVariable Long subTaskId, @RequestBody @Valid UpdateSubTaskRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("id");
        request.setSubTask_id(subTaskId);
        return ResponseEntity.ok(updateSubTask.updateSubTask(request, userId));
    }

    @DeleteMapping("/{subTaskId}")
    public ResponseEntity<Void> deleteSubTask(@PathVariable Long taskId, @PathVariable Long subTaskId, @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("id");
        deleteSubTask.deleteSubTask(subTaskId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<GetSubTasksByTaskIdResponse> getSubTasksByTaskId(@PathVariable Long taskId, @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("id");
        GetSubTasksByTaskIdResponse response = getSubTasksByTaskId.getSubTasksByTaskId(taskId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
