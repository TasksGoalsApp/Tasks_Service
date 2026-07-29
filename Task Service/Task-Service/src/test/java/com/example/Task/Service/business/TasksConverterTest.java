package com.example.Task.Service.business;

import com.example.Task.Service.domain.SubTask;
import com.example.Task.Service.domain.Task;
import com.example.Task.Service.domain.TaskPriority;
import com.example.Task.Service.domain.TaskScheduleType;
import com.example.Task.Service.domain.TaskStatus;
import com.example.Task.Service.repository.SubTaskEntity;
import com.example.Task.Service.repository.TaskEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TasksConverterTest {

    @Test
    void convert_shouldMapAllTaskFieldsAndSubTasks() {
        TaskEntity taskEntity = TaskEntity.builder()
                .taskId(10L)
                .userId(20L)
                .taskTitle("Write unit tests")
                .taskDescription("Increase SonarQube coverage")
                .taskStatus(TaskStatus.IN_PROGRESS)
                .taskPriority(TaskPriority.HIGH)
                .scheduleType(TaskScheduleType.FIXED_TIME)
                .startDate(LocalDate.of(2026, 7, 30))
                .endDate(LocalDate.of(2026, 7, 30))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .build();

        SubTaskEntity subTaskEntity = SubTaskEntity.builder()
                .subTask_id(100L)
                .subTask_title("Test converter")
                .subTask_completed(true)
                .completedDate(LocalDate.of(2026, 7, 29))
                .task(taskEntity)
                .build();

        taskEntity.setSubTasksList(List.of(subTaskEntity));

        Task result = TasksConverter.convert(taskEntity);

        assertNotNull(result);
        assertEquals(10L, result.getTaskId());
        assertEquals(20L, result.getUserId());
        assertEquals("Write unit tests", result.getTaskTitle());
        assertEquals("Increase SonarQube coverage", result.getTaskDescription());
        assertEquals(TaskStatus.IN_PROGRESS, result.getTaskStatus());
        assertEquals(TaskPriority.HIGH, result.getTaskPriority());
        assertEquals(TaskScheduleType.FIXED_TIME, result.getScheduleType());
        assertEquals(LocalDate.of(2026, 7, 30), result.getStartDate());
        assertEquals(LocalDate.of(2026, 7, 30), result.getEndDate());
        assertEquals(LocalTime.of(10, 0), result.getStartTime());
        assertEquals(LocalTime.of(12, 0), result.getEndTime());

        assertNotNull(result.getSubTaskList());
        assertEquals(1, result.getSubTaskList().size());

        SubTask convertedSubTask = result.getSubTaskList().get(0);

        assertEquals(100L, convertedSubTask.getSubTask_id());
        assertEquals("Test converter", convertedSubTask.getSubTask_title());
        assertTrue(convertedSubTask.isSubTask_completed());
        assertEquals(LocalDate.of(2026, 7, 29), convertedSubTask.getCompletedDate());

        assertNotNull(convertedSubTask.getTask());
        assertEquals(10L, convertedSubTask.getTask().getTaskId());
    }

    @Test
    void convert_shouldReturnNullSubTaskListWhenEntityListIsNull() {
        TaskEntity taskEntity = TaskEntity.builder()
                .taskId(10L)
                .userId(20L)
                .taskTitle("Task")
                .subTasksList(null)
                .build();

        Task result = TasksConverter.convert(taskEntity);

        assertNotNull(result);
        assertNull(result.getSubTaskList());
    }

    @Test
    void convert_shouldReturnEmptySubTaskListWhenEntityListIsEmpty() {
        TaskEntity taskEntity = TaskEntity.builder()
                .taskId(10L)
                .userId(20L)
                .taskTitle("Task")
                .subTasksList(List.of())
                .build();

        Task result = TasksConverter.convert(taskEntity);

        assertNotNull(result.getSubTaskList());
        assertTrue(result.getSubTaskList().isEmpty());
    }

}
