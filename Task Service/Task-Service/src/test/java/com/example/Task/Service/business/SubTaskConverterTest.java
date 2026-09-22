package com.example.Task.Service.business;

import com.example.Task.Service.domain.SubTask;
import com.example.Task.Service.domain.Task;
import com.example.Task.Service.repository.SubTaskEntity;
import com.example.Task.Service.repository.TaskEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubTaskConverterTest {

    @Test
    void convert_shouldMapAllSubTaskFields() {
        TaskEntity taskEntity = TaskEntity.builder()
                .taskId(10L)
                .userId(20L)
                .taskTitle("Parent task")
                .build();

        SubTaskEntity subTaskEntity = SubTaskEntity.builder()
                .subTask_id(100L)
                .subTask_title("Complete converter test")
                .subTask_completed(true)
                .completedDate(LocalDate.of(2026, 7, 29))
                .task(taskEntity)
                .build();

        SubTask result = SubTaskConverter.convert(subTaskEntity);

        assertNotNull(result);
        assertEquals(100L, result.getSubTask_id());
        assertEquals(
                "Complete converter test",
                result.getSubTask_title()
        );
        assertTrue(result.isSubTask_completed());
        assertEquals(
                LocalDate.of(2026, 7, 29),
                result.getCompletedDate()
        );

        assertNotNull(result.getTask());
        assertEquals(10L, result.getTask().getTaskId());
    }

    @Test
    void convert_shouldMapIncompleteSubTaskWithNullCompletionDate() {
        TaskEntity taskEntity = TaskEntity.builder()
                .taskId(10L)
                .build();

        SubTaskEntity subTaskEntity = SubTaskEntity.builder()
                .subTask_id(100L)
                .subTask_title("Incomplete subtask")
                .subTask_completed(false)
                .completedDate(null)
                .task(taskEntity)
                .build();

        SubTask result = SubTaskConverter.convert(subTaskEntity);

        assertFalse(result.isSubTask_completed());
        assertNull(result.getCompletedDate());
        assertEquals(10L, result.getTask().getTaskId());
    }

    @Test
    void convertTask_shouldMapOnlyTaskId() {
        TaskEntity taskEntity = TaskEntity.builder()
                .taskId(10L)
                .userId(20L)
                .taskTitle("Parent task")
                .taskDescription("Description")
                .build();

        Task result = SubTaskConverter.convertTask(taskEntity);

        assertNotNull(result);
        assertEquals(10L, result.getTaskId());

        /*
         * convertTask deliberately maps only the task ID to avoid
         * recursively converting the task and all of its subtasks.
         */
        assertNotNull(result.getUserId());
        assertNull(result.getTaskTitle());
        assertNull(result.getTaskDescription());
        assertNull(result.getSubTaskList());
    }

}
