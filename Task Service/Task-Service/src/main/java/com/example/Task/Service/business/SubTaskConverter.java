package com.example.Task.Service.business;

import com.example.Task.Service.domain.SubTask;
import com.example.Task.Service.domain.Task;
import com.example.Task.Service.repository.SubTaskEntity;
import com.example.Task.Service.repository.TaskEntity;

public class SubTaskConverter {

private SubTaskConverter() {}

    public static SubTask convert(SubTaskEntity subTaskEntity) {
    return SubTask.builder()
            .subTask_id(subTaskEntity.getSubTask_id())
            .subTask_title(subTaskEntity.getSubTask_title())
            .subTask_completed(subTaskEntity.isSubTask_completed())
            .task(convertTask(subTaskEntity.getTask()))
            .completedDate(subTaskEntity.getCompletedDate())
            .build();

    }

    public static Task convertTask(TaskEntity taskEntity) {
    return Task.builder().task_id(taskEntity.getTask_id()).build();

    }

}
