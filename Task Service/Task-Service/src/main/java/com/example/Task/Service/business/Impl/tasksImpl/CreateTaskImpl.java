package com.example.Task.Service.business.Impl.tasksImpl;

import com.example.Task.Service.business.ICreateTask;
import com.example.Task.Service.domain.tasksRequestsResponse.CreateTaskRequest;
import com.example.Task.Service.domain.tasksRequestsResponse.CreateTaskResponse;
import com.example.Task.Service.domain.TaskPriority;
import com.example.Task.Service.domain.TaskStatus;
import com.example.Task.Service.repository.TasksRepository;
import com.example.Task.Service.repository.TasksEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@AllArgsConstructor
@Service
public class CreateTaskImpl implements ICreateTask {
    private final TasksRepository tasksRepository;

    @Transactional
    @Override
    public CreateTaskResponse createTask(CreateTaskRequest request) {
        if(request.getTask_day().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Task day cannot be in the past");
        }
        if (request.getStart_time() == null ||request.getEnd_time() == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }

        if (!request.getStart_time().isBefore(request.getEnd_time())) {
         throw new IllegalArgumentException("Start time must be before end time");
        }
        TasksEntity tasksEntity = saveTask(request);

        return CreateTaskResponse.builder().taskId(tasksEntity.getTask_id()).build();

    }

    private TasksEntity saveTask(CreateTaskRequest request) {
        TasksEntity tasksEntity = TasksEntity.builder()
                .task_priority(TaskPriority.MEDIUM)
                .task_description(request.getDescription())
                .task_title(request.getTitle())
                .task_status(TaskStatus.TODO)
                .user_id(request.getUserId())
                .task_day(request.getTask_day())
                .start_time(request.getStart_time())
                .end_time(request.getEnd_time())
                .weekly(request.isWeekly())
                .build();


        return tasksRepository.save(tasksEntity);
    }

}
