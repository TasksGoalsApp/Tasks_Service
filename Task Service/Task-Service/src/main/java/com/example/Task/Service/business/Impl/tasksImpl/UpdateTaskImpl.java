package com.example.Task.Service.business.Impl.tasksImpl;

import com.example.Task.Service.business.IUpdateTask;
import com.example.Task.Service.business.Impl.ResourceNotFoundException;
import com.example.Task.Service.domain.tasksRequestsResponse.UpdateTaskRequest;
import com.example.Task.Service.domain.tasksRequestsResponse.UpdateTaskResponse;
import com.example.Task.Service.repository.TasksEntity;
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
    public UpdateTaskResponse updateTask(UpdateTaskRequest updateTaskRequest) {
        TasksEntity tasksEntity = tasksRepository.findById(updateTaskRequest.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found!"));

        taskScheduleValidator.validate(
                updateTaskRequest.getSchedule_type(),
                updateTaskRequest.getStart_date(),
                updateTaskRequest.getEnd_date(),
                updateTaskRequest.getStart_time(),
                updateTaskRequest.getEnd_time()
        );

        tasksEntity.setTask_title(updateTaskRequest.getTask_title());
        tasksEntity.setTask_description(updateTaskRequest.getTask_description());
        tasksEntity.setTask_status(updateTaskRequest.getTask_status());
        tasksEntity.setTask_priority(updateTaskRequest.getTask_priority());
        tasksEntity.setSchedule_type(updateTaskRequest.getSchedule_type());
        tasksEntity.setStart_date(updateTaskRequest.getStart_date());
        tasksEntity.setEnd_date(updateTaskRequest.getEnd_date());
        tasksEntity.setStart_time(updateTaskRequest.getStart_time());
        tasksEntity.setEnd_time(updateTaskRequest.getEnd_time());

        TasksEntity savedTask = tasksRepository.save(tasksEntity);

        return UpdateTaskResponse.builder()
                .task_id(savedTask.getTask_id())
                .user_id(savedTask.getUser_id())
                .task_title(savedTask.getTask_title())
                .task_description(savedTask.getTask_description())
                .task_status(savedTask.getTask_status())
                .task_priority(savedTask.getTask_priority())
                .schedule_type(savedTask.getSchedule_type())
                .start_date(savedTask.getStart_date())
                .end_date(savedTask.getEnd_date())
                .start_time(savedTask.getStart_time())
                .end_time(savedTask.getEnd_time())
                .build();
    }
}