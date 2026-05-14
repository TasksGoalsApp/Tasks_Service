package com.example.Task.Service.business;

import com.example.Task.Service.domain.SubTask;
import com.example.Task.Service.domain.Task;
import com.example.Task.Service.repository.TaskEntity;

import java.util.List;

public class TasksConvertor {

    private TasksConvertor() {
    }

    public static Task convert(TaskEntity taskEntity) {
        List<SubTask> subTaskList = null;

        if (taskEntity.getSubTasksList() != null) {
            subTaskList = taskEntity.getSubTasksList()
                    .stream()
                    .map(SubTaskConverter::convert)
                    .toList();
        }

        return Task.builder()
                .taskId(taskEntity.getTaskId())
                .userId(taskEntity.getUserId())
                .taskTitle(taskEntity.getTaskTitle())
                .taskDescription(taskEntity.getTaskDescription())
                .taskStatus(taskEntity.getTaskStatus())
                .taskPriority(taskEntity.getTaskPriority())
                .scheduleType(taskEntity.getScheduleType())
                .startDate(taskEntity.getStartDate())
                .endDate(taskEntity.getEndDate())
                .startTime(taskEntity.getStartTime())
                .endTime(taskEntity.getEndTime())
                .subTaskList(subTaskList)
                .build();
    }
}