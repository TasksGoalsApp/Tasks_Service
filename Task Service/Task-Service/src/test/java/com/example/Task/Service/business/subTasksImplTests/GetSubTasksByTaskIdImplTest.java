package com.example.Task.Service.business.subTasksImplTests;

import com.example.Task.Service.business.Impl.subTaskImpl.GetSubTasksByTaskIdImpl;
import com.example.Task.Service.domain.subtasksRequestsResponse.GetSubTasksByTaskIdResponse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.Task.Service.repository.SubTaskEntity;
import com.example.Task.Service.repository.TaskEntity;
import com.example.Task.Service.exception.ResourceNotFoundException;
import com.example.Task.Service.repository.SubTasksRepository;
import com.example.Task.Service.repository.TasksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GetSubTasksByTaskIdImplTest {

    @Mock
    private SubTasksRepository subTasksRepository;

    @Mock
    private TasksRepository tasksRepository;

    private GetSubTasksByTaskIdImpl getSubTasks;

    @BeforeEach
    void setUp() {
        getSubTasks = new GetSubTasksByTaskIdImpl(subTasksRepository, tasksRepository);
    }

    @Test
    void getSubTasks_shouldReturnAllSubTasksForOwnedTask() {

        Long taskId = 10L;
        Long userId = 20L;

        TaskEntity task = TaskEntity.builder()
                .taskId(taskId)
                .userId(userId)
                .build();

        SubTaskEntity first = SubTaskEntity.builder()
                .subTask_id(1L)
                .task(task)
                .subTask_title("First")
                .subTask_completed(false)
                .build();

        SubTaskEntity second = SubTaskEntity.builder()
                .subTask_id(2L)
                .task(task)
                .subTask_title("Second")
                .subTask_completed(true)
                .completedDate(LocalDate.of(2026,7,20))
                .build();

        when(tasksRepository.findByTaskIdAndUserId(taskId,userId)).thenReturn(Optional.of(task));

        when(subTasksRepository.findByTask_TaskId(taskId)).thenReturn(List.of(first,second));

        //List<GetSubTaskResponse> response = getSubTasks.getSubTasks(taskId,userId);
        GetSubTasksByTaskIdResponse response = getSubTasks.getSubTasksByTaskId(taskId,userId);

        assertEquals(2,response.getSubTasks().size());

        assertEquals(1L,response.getSubTasks().get(0).getSubTask_id());
        assertEquals("First",response.getSubTasks().get(0).getSubTask_title());
        assertFalse(response.getSubTasks().get(0).isSubTask_completed());

        assertEquals(2L,response.getSubTasks().get(1).getSubTask_id());
        assertEquals("Second",response.getSubTasks().get(1).getSubTask_title());
        assertTrue(response.getSubTasks().get(1).isSubTask_completed());
        assertEquals(
                LocalDate.of(2026,7,20),
                response.getSubTasks().get(1).getCompletedDate()
        );

        verify(tasksRepository).findByTaskIdAndUserId(taskId,userId);

        verify(subTasksRepository).findByTask_TaskId(taskId);
    }

    @Test
    void getSubTasks_shouldReturnEmptyListWhenTaskHasNoSubTasks() {

        Long taskId = 10L;
        Long userId = 20L;

        TaskEntity task = TaskEntity.builder()
                .taskId(taskId)
                .userId(userId)
                .build();

        when(tasksRepository.findByTaskIdAndUserId(taskId,userId)).thenReturn(Optional.of(task));

        when(subTasksRepository.findByTask_TaskId(taskId)).thenReturn(List.of());

        GetSubTasksByTaskIdResponse response = getSubTasks.getSubTasksByTaskId(taskId,userId);

        assertTrue(response.getSubTasks().isEmpty());
    }

    @Test
    void getSubTasks_shouldThrowWhenTaskDoesNotBelongToUser() {

        Long taskId = 10L;
        Long userId = 20L;

        when(tasksRepository.findByTaskIdAndUserId(taskId,userId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> getSubTasks.getSubTasksByTaskId(taskId,userId)
                );

        assertEquals("Task not found",exception.getMessage());

        verify(subTasksRepository,never()).findByTask_TaskId(anyLong());
    }

    @Test
    void getSubTasks_shouldPropagateRepositoryException() {

        Long taskId = 10L;
        Long userId = 20L;

        TaskEntity task = TaskEntity.builder()
                .taskId(taskId)
                .userId(userId)
                .build();

        when(tasksRepository.findByTaskIdAndUserId(taskId,userId))
                .thenReturn(Optional.of(task));

        when(subTasksRepository.findByTask_TaskId(taskId))
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> getSubTasks.getSubTasksByTaskId(taskId,userId)
                );

        assertEquals("Database error",exception.getMessage());
    }

}
