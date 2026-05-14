package com.example.Task.Service.business.Impl.tasksImpl;

import com.example.Task.Service.business.ICreateTask;
import com.example.Task.Service.domain.TaskStatus;
import com.example.Task.Service.domain.tasksRequestsResponse.CreateTaskRequest;
import com.example.Task.Service.domain.tasksRequestsResponse.CreateTaskResponse;
import com.example.Task.Service.repository.TaskEntity;
import com.example.Task.Service.repository.TasksRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CreateTaskImpl implements ICreateTask {

    private final TasksRepository tasksRepository;
    private final TaskScheduleValidator taskScheduleValidator;

    @Transactional
    @Override
    public CreateTaskResponse createTask(CreateTaskRequest request, long userId) {
        taskScheduleValidator.validate(
                request.getScheduleType(),
                request.getStartDate(),
                request.getEndDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        TaskEntity taskEntity = saveTask(request, userId);

        return CreateTaskResponse.builder()
                .taskId(taskEntity.getTaskId())
                .build();
    }

    private TaskEntity saveTask(CreateTaskRequest request, long userId) {
        TaskEntity taskEntity = TaskEntity.builder()
                .taskPriority(request.getTaskPriority())
                .taskDescription(request.getDescription())
                .taskTitle(request.getTitle())
                .taskStatus(TaskStatus.TODO)
                .userId(userId)
                .scheduleType(request.getScheduleType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        return tasksRepository.save(taskEntity);
    }
}