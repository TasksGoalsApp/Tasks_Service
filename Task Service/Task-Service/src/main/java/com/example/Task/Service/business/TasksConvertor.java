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
                .taskId(taskEntity.getTask_id())
                .userId(taskEntity.getUserId())
                .taskTitle(taskEntity.getTask_title())
                .taskDescription(taskEntity.getTask_description())
                .taskStatus(taskEntity.getTask_status())
                .taskPriority(taskEntity.getTask_priority())
                .scheduleType(taskEntity.getSchedule_type())
                .startDate(taskEntity.getStart_date())
                .endDate(taskEntity.getEnd_date())
                .startTime(taskEntity.getStart_time())
                .endTime(taskEntity.getEnd_time())
                .subTaskList(subTaskList)
                .build();
    }
}