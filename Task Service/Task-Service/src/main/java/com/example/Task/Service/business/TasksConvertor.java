package com.example.Task.Service.business;

import com.example.Task.Service.domain.SubTasks;
import com.example.Task.Service.domain.Tasks;
import com.example.Task.Service.repository.TasksEntity;

import java.util.List;

public class TasksConvertor {

    private TasksConvertor() {
    }

    public static Tasks convert(TasksEntity tasksEntity) {
        List<SubTasks> subTasksList = null;

        if (tasksEntity.getSubTasksList() != null) {
            subTasksList = tasksEntity.getSubTasksList()
                    .stream()
                    .map(SubTaskConverter::convert)
                    .toList();
        }

        return Tasks.builder()
                .task_id(tasksEntity.getTask_id())
                .user_id(tasksEntity.getUser_id())
                .task_title(tasksEntity.getTask_title())
                .task_description(tasksEntity.getTask_description())
                .task_status(tasksEntity.getTask_status())
                .task_priority(tasksEntity.getTask_priority())
                .schedule_type(tasksEntity.getSchedule_type())
                .start_date(tasksEntity.getStart_date())
                .end_date(tasksEntity.getEnd_date())
                .start_time(tasksEntity.getStart_time())
                .end_time(tasksEntity.getEnd_time())
                .subTasksList(subTasksList)
                .build();
    }
}