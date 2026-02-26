package com.example.Task.Service.business;

import com.example.Task.Service.domain.SubTasks;
import com.example.Task.Service.domain.Tasks;
import com.example.Task.Service.repository.SubTasksEntity;
import com.example.Task.Service.repository.TasksEntity;

public class SubTaskConverter {

private SubTaskConverter() {}

    public static SubTasks convert(SubTasksEntity subTasksEntity) {
    return SubTasks.builder()
            .subTask_id(subTasksEntity.getSubTask_id())
            .subTask_title(subTasksEntity.getSubTask_title())
            .subTask_completed(subTasksEntity.isSubTask_completed())
            .tasks(convertTask(subTasksEntity.getTask()))
            .completedDate(subTasksEntity.getCompletedDate())
            .build();

    }

    public static Tasks convertTask(TasksEntity tasksEntity) {
    return Tasks.builder().task_id(tasksEntity.getTask_id()).build();

    }

}
