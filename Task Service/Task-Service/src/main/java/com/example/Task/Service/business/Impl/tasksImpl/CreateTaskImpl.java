package com.example.Task.Service.business.Impl.tasksImpl;

import com.example.Task.Service.business.ICreateTask;
import com.example.Task.Service.domain.TaskStatus;
import com.example.Task.Service.domain.tasksRequestsResponse.CreateTaskRequest;
import com.example.Task.Service.domain.tasksRequestsResponse.CreateTaskResponse;
import com.example.Task.Service.repository.TasksEntity;
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
    public CreateTaskResponse createTask(CreateTaskRequest request) {
        taskScheduleValidator.validate(
                request.getSchedule_type(),
                request.getStart_date(),
                request.getEnd_date(),
                request.getStart_time(),
                request.getEnd_time()
        );

        TasksEntity tasksEntity = saveTask(request);

        return CreateTaskResponse.builder()
                .taskId(tasksEntity.getTask_id())
                .build();
    }

    private TasksEntity saveTask(CreateTaskRequest request) {
        TasksEntity tasksEntity = TasksEntity.builder()
                .task_priority(request.getTask_priority())
                .task_description(request.getDescription())
                .task_title(request.getTitle())
                .task_status(TaskStatus.TODO)
                .userId(request.getUserId())
                .schedule_type(request.getSchedule_type())
                .start_date(request.getStart_date())
                .end_date(request.getEnd_date())
                .start_time(request.getStart_time())
                .end_time(request.getEnd_time())
                .build();

        return tasksRepository.save(tasksEntity);
    }
}