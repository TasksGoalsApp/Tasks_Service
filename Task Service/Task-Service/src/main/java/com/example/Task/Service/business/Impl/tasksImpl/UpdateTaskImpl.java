package com.example.Task.Service.business.Impl.tasksImpl;

import com.example.Task.Service.business.IUpdateTask;
import com.example.Task.Service.business.Impl.ResourceNotFoundException;
import com.example.Task.Service.domain.tasksRequestsResponse.UpdateTaskRequest;
import com.example.Task.Service.domain.tasksRequestsResponse.UpdateTaskResponse;
import com.example.Task.Service.repository.TasksEntity;
import com.example.Task.Service.repository.TasksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UpdateTaskImpl implements IUpdateTask {
    private final TasksRepository tasksRepository;

    @Override
    public UpdateTaskResponse updateTask(UpdateTaskRequest updateTaskRequest) {

        TasksEntity tasksEntity = tasksRepository.findById(updateTaskRequest.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found!"));

        if(updateTaskRequest.getTask_day().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Task day cannot be in the past");
        }
        if (updateTaskRequest.getStart_time() == null ||updateTaskRequest.getEnd_time() == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }

        if (!updateTaskRequest.getStart_time().isBefore(updateTaskRequest.getEnd_time())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        tasksEntity.setTask_title(updateTaskRequest.getTask_title());
        tasksEntity.setTask_description(updateTaskRequest.getTask_description());
        tasksEntity.setTask_status(updateTaskRequest.getTask_status());
        tasksEntity.setTask_priority(updateTaskRequest.getTask_priority());
        tasksEntity.setTask_day(updateTaskRequest.getTask_day());
        tasksEntity.setStart_time(updateTaskRequest.getStart_time());
        tasksEntity.setEnd_time(updateTaskRequest.getEnd_time());
        tasksEntity.setWeekly(updateTaskRequest.isWeekly());
        TasksEntity savedTask = tasksRepository.save(tasksEntity);

        return UpdateTaskResponse.builder()
                .task_id(savedTask.getTask_id())
                .user_id(savedTask.getUser_id())
                .task_title(savedTask.getTask_title())
                .task_description(savedTask.getTask_description())
                .task_status(savedTask.getTask_status())
                .task_priority(savedTask.getTask_priority())
                .task_day(savedTask.getTask_day())
                .start_time(savedTask.getStart_time())
                .end_time(savedTask.getEnd_time())
                .weekly(savedTask.isWeekly())
                .build();
    }
}
