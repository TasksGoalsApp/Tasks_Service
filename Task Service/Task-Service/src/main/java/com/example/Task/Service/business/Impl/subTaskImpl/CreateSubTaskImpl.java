package com.example.Task.Service.business.Impl.subTaskImpl;

import com.example.Task.Service.business.ICreateSubTask;
import com.example.Task.Service.exception.ResourceNotFoundException;
import com.example.Task.Service.domain.subtasksRequestsResponse.CreateSubTaskRequest;
import com.example.Task.Service.domain.subtasksRequestsResponse.CreateSubTaskResponse;
import com.example.Task.Service.repository.SubTasksRepository;
import com.example.Task.Service.repository.SubTaskEntity;
import com.example.Task.Service.repository.TaskEntity;
import com.example.Task.Service.repository.TasksRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateSubTaskImpl implements ICreateSubTask {
    private final SubTasksRepository subTasksRepository;
    private final TasksRepository tasksRepository;

    @Transactional
    @Override
    public CreateSubTaskResponse createSubTask(CreateSubTaskRequest request, Long userId, Long taskId) {
        SubTaskEntity subTaskEntity = savedSubTasks(request, userId, taskId);

        return CreateSubTaskResponse.builder()
                .subtaskId(subTaskEntity.getSubTask_id())
                .build();
    }

    private SubTaskEntity savedSubTasks(CreateSubTaskRequest request, Long userId, Long taskId){

        TaskEntity taskEntity = tasksRepository.findByTaskIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task id not found"));

        SubTaskEntity subTaskEntity = SubTaskEntity.builder()
                .subTask_completed(false)
                .task(taskEntity)
                .subTask_title(request.getSubTask_title())
                .completedDate(null)
                .build();

        return subTasksRepository.save(subTaskEntity);
    }

}
