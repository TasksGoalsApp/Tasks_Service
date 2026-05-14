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
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("/tasks/subtasks")
@RestController
public class SubTaskController {

    private final ICreateSubTask  createSubTask;
    private final IDeleteSubTask deleteSubTask;
    private final IUpdateSubTask updateSubTask;
    private final IGetSubTasksByTaskId getSubTasksByTaskId;

    @PostMapping("/create")
    public ResponseEntity<CreateSubTaskResponse> createSubTask(@RequestBody @Valid CreateSubTaskRequest createSubTaskRequest) {
        CreateSubTaskResponse response = createSubTask.createSubTask(createSubTaskRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/delete")
    public ResponseEntity<UpdateSubTaskResponse> updateSubTask(@RequestBody @Valid UpdateSubTaskRequest request) {
        return ResponseEntity.ok(updateSubTask.updateSubTask(request));
    }

    @DeleteMapping("/{subTaskId}")
    public ResponseEntity<Void> deleteSubTask(@PathVariable Long subTaskId) {
        deleteSubTask.deleteSubTask(subTaskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<GetSubTasksByTaskIdResponse> getSubTasksByTaskId(@PathVariable Long taskId) {
        GetSubTasksByTaskIdResponse response = getSubTasksByTaskId.getSubTasksByTaskId(taskId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
