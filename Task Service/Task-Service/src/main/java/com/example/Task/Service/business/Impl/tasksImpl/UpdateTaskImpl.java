package com.example.Task.Service.business.Impl.tasksImpl;

import com.example.Task.Service.business.IUpdateTask;
import com.example.Task.Service.exception.ResourceNotFoundException;
import com.example.Task.Service.domain.tasksRequestsResponse.UpdateTaskRequest;
import com.example.Task.Service.domain.tasksRequestsResponse.UpdateTaskResponse;
import com.example.Task.Service.repository.TaskEntity;
import com.example.Task.Service.repository.TasksRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateTaskImpl implements IUpdateTask {

    private final TasksRepository tasksRepository;
    private final TaskScheduleValidator taskScheduleValidator;
    @Transactional
    @Override
    public UpdateTaskResponse updateTask(UpdateTaskRequest updateTaskRequest, Long userId) {
        TaskEntity taskEntity = tasksRepository.findByTaskIdAndUserId(updateTaskRequest.getTaskId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskScheduleValidator.validate(
                updateTaskRequest.getScheduleType(),
                updateTaskRequest.getStartDate(),
                updateTaskRequest.getEndDate(),
                updateTaskRequest.getStartTime(),
                updateTaskRequest.getEndTime()
        );

        taskEntity.setTaskTitle(updateTaskRequest.getTaskTitle());
        taskEntity.setTaskDescription(updateTaskRequest.getTaskDescription());
        taskEntity.setTaskStatus(updateTaskRequest.getTaskStatus());
        taskEntity.setTaskPriority(updateTaskRequest.getTaskPriority());
        taskEntity.setScheduleType(updateTaskRequest.getScheduleType());
        taskEntity.setStartDate(updateTaskRequest.getStartDate());
        taskEntity.setEndDate(updateTaskRequest.getEndDate());
        taskEntity.setStartTime(updateTaskRequest.getStartTime());
        taskEntity.setEndTime(updateTaskRequest.getEndTime());

        TaskEntity savedTask = tasksRepository.save(taskEntity);

        return UpdateTaskResponse.builder()
                .task_id(savedTask.getTaskId())
                .userId(savedTask.getUserId())
                .taskTitle(savedTask.getTaskTitle())
                .taskDescription(savedTask.getTaskDescription())
                .taskStatus(savedTask.getTaskStatus())
                .taskPriority(savedTask.getTaskPriority())
                .scheduleType(savedTask.getScheduleType())
                .startDate(savedTask.getStartDate())
                .endDate(savedTask.getEndDate())
                .startTime(savedTask.getStartTime())
                .endTime(savedTask.getEndTime())
                .build();
    }
}