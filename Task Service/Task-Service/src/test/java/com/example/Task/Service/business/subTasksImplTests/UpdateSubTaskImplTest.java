package com.example.Task.Service.business.subTasksImplTests;
import com.example.Task.Service.business.Impl.subTaskImpl.UpdateSubTaskImpl;
import com.example.Task.Service.domain.subtasksRequestsResponse.UpdateSubTaskRequest;
import com.example.Task.Service.domain.subtasksRequestsResponse.UpdateSubTaskResponse;
import com.example.Task.Service.repository.SubTaskEntity;
import com.example.Task.Service.repository.TaskEntity;
import com.example.Task.Service.exception.ResourceNotFoundException;
import com.example.Task.Service.repository.SubTasksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateSubTaskImplTest {
    @Mock
    private SubTasksRepository subTasksRepository;

    private UpdateSubTaskImpl updateSubTask;

    @BeforeEach
    void setUp() {
        updateSubTask = new UpdateSubTaskImpl(subTasksRepository);
    }

    @Test
    void updateSubTask_shouldUpdateOwnedSubTaskAsCompleted() {
        Long taskId = 10L;
        Long subTaskId = 100L;
        Long userId = 20L;

        UpdateSubTaskRequest request = UpdateSubTaskRequest.builder()
                .subTask_id(subTaskId)
                .subTask_title("Updated subtask")
                .subTask_completed(true)
                .build();

        TaskEntity task = TaskEntity.builder()
                .taskId(taskId)
                .userId(userId)
                .build();

        SubTaskEntity existingSubTask = SubTaskEntity.builder()
                .subTask_id(subTaskId)
                .task(task)
                .subTask_title("Old subtask")
                .subTask_completed(false)
                .completedDate(null)
                .build();

        when(subTasksRepository.findBySubTaskIdAndTask_UserId(subTaskId, userId)).thenReturn(Optional.of(existingSubTask));

        when(subTasksRepository.save(any(SubTaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateSubTaskResponse response = updateSubTask.updateSubTask(request, userId);

        ArgumentCaptor<SubTaskEntity> captor = ArgumentCaptor.forClass(SubTaskEntity.class);

        verify(subTasksRepository).save(captor.capture());

        SubTaskEntity savedSubTask = captor.getValue();

        assertEquals(subTaskId, savedSubTask.getSubTask_id());
        assertEquals("Updated subtask", savedSubTask.getSubTask_title());
        assertTrue(savedSubTask.isSubTask_completed());
        assertEquals(subTaskId, response.getSubTask_id());
    }

    @Test
    void updateSubTask_shouldClearCompletionDateWhenMarkedIncomplete() {
        Long taskId = 10L;
        Long subTaskId = 100L;
        Long userId = 20L;

        UpdateSubTaskRequest request = UpdateSubTaskRequest.builder()
                .subTask_id(subTaskId)
                .subTask_title("Reopened subtask")
                .subTask_completed(false)
                .build();

        TaskEntity task = TaskEntity.builder()
                .taskId(taskId)
                .userId(userId)
                .build();

        SubTaskEntity existingSubTask = SubTaskEntity.builder()
                .subTask_id(subTaskId)
                .task(task)
                .subTask_title("Completed subtask")
                .subTask_completed(true)
                .completedDate(LocalDate.of(2026, 7, 20))
                .build();

        when(subTasksRepository.findBySubTaskIdAndTask_UserId(subTaskId, userId)).thenReturn(Optional.of(existingSubTask));

        when(subTasksRepository.save(any(SubTaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        updateSubTask.updateSubTask(request, userId);

        ArgumentCaptor<SubTaskEntity> captor =
                ArgumentCaptor.forClass(SubTaskEntity.class);

        verify(subTasksRepository).save(captor.capture());

        SubTaskEntity savedSubTask = captor.getValue();

        assertEquals("Reopened subtask", savedSubTask.getSubTask_title());
        assertFalse(savedSubTask.isSubTask_completed());
        assertNull(savedSubTask.getCompletedDate());
    }

    @Test
    void updateSubTask_shouldThrowWhenSubTaskDoesNotBelongToUser() {
        Long taskId = 10L;
        Long subTaskId = 100L;
        Long userId = 20L;

        UpdateSubTaskRequest request = UpdateSubTaskRequest.builder()
                .subTask_id(subTaskId)
                .subTask_title("Unauthorized update")
                .subTask_completed(true)
                .build();

        when(subTasksRepository.findBySubTaskIdAndTask_UserId(subTaskId, userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> updateSubTask.updateSubTask(request, userId));

        assertEquals("SubTask id not found", exception.getMessage());

        verify(subTasksRepository, never()).save(any(SubTaskEntity.class));
    }

    @Test
    void updateSubTask_shouldPropagateRepositoryException() {
        Long taskId = 10L;
        Long subTaskId = 100L;
        Long userId = 20L;

        UpdateSubTaskRequest request = UpdateSubTaskRequest.builder()
                .subTask_id(subTaskId)
                .subTask_title("Repository failure")
                .subTask_completed(true)
                .build();

        TaskEntity task = TaskEntity.builder()
                .taskId(taskId)
                .userId(userId)
                .build();

        SubTaskEntity existingSubTask = SubTaskEntity.builder()
                .subTask_id(subTaskId)
                .task(task)
                .subTask_title("Old title")
                .subTask_completed(false)
                .build();

        when(subTasksRepository.findBySubTaskIdAndTask_UserId(subTaskId, userId)).thenReturn(Optional.of(existingSubTask));

        when(subTasksRepository.save(any(SubTaskEntity.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> updateSubTask.updateSubTask(request, userId));

        assertEquals("Database error", exception.getMessage());
    }
}
