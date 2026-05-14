package com.example.Task.Service.business.Impl.subTaskImpl;

import com.example.Task.Service.business.IUpdateSubTask;
import com.example.Task.Service.exception.ResourceNotFoundException;
import com.example.Task.Service.domain.subtasksRequestsResponse.UpdateSubTaskRequest;
import com.example.Task.Service.domain.subtasksRequestsResponse.UpdateSubTaskResponse;
import com.example.Task.Service.repository.SubTaskEntity;
import com.example.Task.Service.repository.SubTasksRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateSubTaskImpl implements IUpdateSubTask {
    private final SubTasksRepository subTasksRepository;

    @Transactional
    @Override
    public UpdateSubTaskResponse updateSubTask(UpdateSubTaskRequest updateSubTaskRequest) {
        SubTaskEntity subTaskEntity = subTasksRepository.findById(updateSubTaskRequest.getSubTask_id())
                .orElseThrow(() -> new ResourceNotFoundException("SubTask id not found"));

        subTaskEntity.setSubTask_completed(updateSubTaskRequest.isSubTask_completed());
        subTaskEntity.setSubTask_title(updateSubTaskRequest.getSubTask_title());
        subTaskEntity.setCompletedDate(updateSubTaskRequest.getCompletedDate());

        SubTaskEntity savedSubTaskEntity = subTasksRepository.save(subTaskEntity);
        return UpdateSubTaskResponse.builder()
                .subTask_title(savedSubTaskEntity.getSubTask_title())
                .completedDate(savedSubTaskEntity.getCompletedDate())
                .subTask_completed(savedSubTaskEntity.isSubTask_completed())
                .subTask_id(savedSubTaskEntity.getSubTask_id())
                .build();

    }
}

