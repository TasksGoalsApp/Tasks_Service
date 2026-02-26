package com.example.Task.Service.business.Impl.subTaskImpl;

import com.example.Task.Service.business.ICreateSubTask;
import com.example.Task.Service.business.Impl.ResourceNotFoundException;
import com.example.Task.Service.domain.subtasksRequestsResponse.CreateSubTaskRequest;
import com.example.Task.Service.domain.subtasksRequestsResponse.CreateSubTaskResponse;
import com.example.Task.Service.repository.SubTasksRepository;
import com.example.Task.Service.repository.SubTasksEntity;
import com.example.Task.Service.repository.TasksEntity;
import com.example.Task.Service.repository.TasksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateSubTaskImpl implements ICreateSubTask {
    private final SubTasksRepository subTasksRepository;
    private final TasksRepository tasksRepository;


    @Override
    public CreateSubTaskResponse createSubTask(CreateSubTaskRequest request) {
        TasksEntity tasksEntity = tasksRepository.findById(request.getTasks_id())
                .orElseThrow(() -> new ResourceNotFoundException("Task id not found"));

        SubTasksEntity subTasksEntity = savedSubTasks(request);

        return CreateSubTaskResponse.builder()
                .subtaskId(subTasksEntity.getSubTask_id())
                .build();
    }

    private SubTasksEntity savedSubTasks(CreateSubTaskRequest request){
        SubTasksEntity subTasksEntity = SubTasksEntity.builder()
                .subTask_completed(false)
                .task(TasksEntity.builder().task_id(request.getTasks_id()).build())
                .subTask_title(request.getSubTask_title())
                .completedDate(request.getCompletedDate())
                .build();

        return subTasksRepository.save(subTasksEntity);
    }

}
