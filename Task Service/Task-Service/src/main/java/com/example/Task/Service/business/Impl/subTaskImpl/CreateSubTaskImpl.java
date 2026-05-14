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
    public CreateSubTaskResponse createSubTask(CreateSubTaskRequest request) {
        SubTaskEntity subTaskEntity = savedSubTasks(request);

        return CreateSubTaskResponse.builder()
                .subtaskId(subTaskEntity.getSubTask_id())
                .build();
    }

    private SubTaskEntity savedSubTasks(CreateSubTaskRequest request){

        TaskEntity taskEntity = tasksRepository.findById(request.getTasks_id())
                .orElseThrow(() -> new ResourceNotFoundException("Task id not found"));

        SubTaskEntity subTaskEntity = SubTaskEntity.builder()
                .subTask_completed(false)
                .task(taskEntity)
                .subTask_title(request.getSubTask_title())
                .completedDate(request.getCompletedDate())
                .build();

        return subTasksRepository.save(subTaskEntity);
    }

}
