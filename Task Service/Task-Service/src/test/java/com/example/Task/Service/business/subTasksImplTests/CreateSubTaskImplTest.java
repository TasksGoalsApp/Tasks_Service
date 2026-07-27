package com.example.Task.Service.business.subTasksImplTests;

import com.example.Task.Service.business.Impl.subTaskImpl.CreateSubTaskImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.Task.Service.domain.subtasksRequestsResponse.CreateSubTaskRequest;
import com.example.Task.Service.domain.subtasksRequestsResponse.CreateSubTaskResponse;
import com.example.Task.Service.repository.SubTaskEntity;
import com.example.Task.Service.repository.TaskEntity;
import com.example.Task.Service.exception.ResourceNotFoundException;
import com.example.Task.Service.repository.SubTasksRepository;
import com.example.Task.Service.repository.TasksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateSubTaskImplTest {

    @Mock
    private SubTasksRepository subTasksRepository;

    @Mock
    private TasksRepository tasksRepository;

    private CreateSubTaskImpl createSubTask;

    @BeforeEach
    void setUp() {
        createSubTask = new CreateSubTaskImpl(subTasksRepository, tasksRepository);
    }

    @Test
    void createSubTask_shouldCreateSubTaskForOwnedTask() {
        Long taskId = 10L;
        Long userId = 20L;

        CreateSubTaskRequest request = CreateSubTaskRequest.builder()
                .subTask_title("Write unit tests")
                .build();

        TaskEntity task = TaskEntity.builder()
                .taskId(taskId)
                .userId(userId)
                .taskTitle("Improve Task Service")
                .build();

        when(tasksRepository.findByTaskIdAndUserId(taskId, userId)).thenReturn(Optional.of(task));

        when(subTasksRepository.save(any(SubTaskEntity.class))).thenAnswer(invocation -> {
                    SubTaskEntity entity = invocation.getArgument(0);
                    entity.setSubTask_id(100L);
                    return entity;
                });

        CreateSubTaskResponse response = createSubTask.createSubTask( request, userId, taskId);

        assertEquals(100L, response.getSubtaskId());

        verify(tasksRepository).findByTaskIdAndUserId(taskId, userId);

        ArgumentCaptor<SubTaskEntity> captor = ArgumentCaptor.forClass(SubTaskEntity.class);

        verify(subTasksRepository).save(captor.capture());

        SubTaskEntity savedSubTask = captor.getValue();

        assertEquals(task, savedSubTask.getTask());
        assertEquals(request.getSubTask_title(), savedSubTask.getSubTask_title());
        assertFalse(savedSubTask.isSubTask_completed());
        assertNull(savedSubTask.getCompletedDate());
    }

    @Test
    void createSubTask_shouldThrowWhenTaskDoesNotBelongToUser() {
        Long taskId = 10L;
        Long userId = 20L;

        CreateSubTaskRequest request = CreateSubTaskRequest.builder()
                .subTask_title("Unauthorized subtask")
                .build();

        when(tasksRepository.findByTaskIdAndUserId(taskId, userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> createSubTask.createSubTask(request, userId,taskId));

        assertEquals("Task id not found", exception.getMessage());

        verify(tasksRepository).findByTaskIdAndUserId(taskId, userId);

        verify(subTasksRepository, never()).save(any(SubTaskEntity.class));
    }

    @Test
    void createSubTask_shouldPropagateRepositoryException() {
        Long taskId = 10L;
        Long userId = 20L;

        CreateSubTaskRequest request = CreateSubTaskRequest.builder()
                .subTask_title("Repository error test")
                .build();

        TaskEntity task = TaskEntity.builder()
                .taskId(taskId)
                .userId(userId)
                .build();

        when(tasksRepository.findByTaskIdAndUserId(taskId, userId)).thenReturn(Optional.of(task));

        when(subTasksRepository.save(any(SubTaskEntity.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> createSubTask.createSubTask(request, userId,taskId));

        assertEquals("Database error", exception.getMessage());

        verify(tasksRepository).findByTaskIdAndUserId(taskId, userId);

        verify(subTasksRepository).save(any(SubTaskEntity.class));
    }

    @Test
    void createSubTask_shouldUseIncompleteDefaultValues() {
        Long taskId = 10L;
        Long userId = 20L;

        CreateSubTaskRequest request = CreateSubTaskRequest.builder()
                .subTask_title("Default values test")
                .build();

        TaskEntity task = TaskEntity.builder()
                .taskId(taskId)
                .userId(userId)
                .build();

        when(tasksRepository.findByTaskIdAndUserId(taskId, userId)).thenReturn(Optional.of(task));

        when(subTasksRepository.save(any(SubTaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        createSubTask.createSubTask(request, userId, taskId);

        ArgumentCaptor<SubTaskEntity> captor = ArgumentCaptor.forClass(SubTaskEntity.class);

        verify(subTasksRepository).save(captor.capture());

        SubTaskEntity savedSubTask = captor.getValue();

        assertEquals(task, savedSubTask.getTask());
        assertEquals("Default values test", savedSubTask.getSubTask_title());
        assertFalse(savedSubTask.isSubTask_completed());
        assertNull(savedSubTask.getCompletedDate());
    }

}
