package com.example.Task.Service.business.Impl.subTaskImpl;

import com.example.Task.Service.business.IUpdateSubTask;
import com.example.Task.Service.business.Impl.ResourceNotFoundException;
import com.example.Task.Service.domain.subtasksRequestsResponse.UpdateSubTaskRequest;
import com.example.Task.Service.domain.subtasksRequestsResponse.UpdateSubTaskResponse;
import com.example.Task.Service.repository.SubTasksEntity;
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
        SubTasksEntity subTasksEntity = subTasksRepository.findById(updateSubTaskRequest.getSubTask_id())
                .orElseThrow(() -> new ResourceNotFoundException("SubTask id not found"));

        subTasksEntity.setSubTask_completed(updateSubTaskRequest.isSubTask_completed());
        subTasksEntity.setSubTask_title(updateSubTaskRequest.getSubTask_title());
        subTasksEntity.setCompletedDate(updateSubTaskRequest.getCompletedDate());

        SubTasksEntity savedSubTasksEntity = subTasksRepository.save(subTasksEntity);
        return UpdateSubTaskResponse.builder()
                .subTask_title(savedSubTasksEntity.getSubTask_title())
                .completedDate(savedSubTasksEntity.getCompletedDate())
                .subTask_completed(savedSubTasksEntity.isSubTask_completed())
                .subTask_id(savedSubTasksEntity.getSubTask_id())
                .build();

    }
}

