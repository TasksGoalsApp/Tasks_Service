package com.example.Task.Service.business.subTasksImplTests;

import com.example.Task.Service.business.Impl.subTaskImpl.DeleteSubTaskImpl;
import com.example.Task.Service.exception.ResourceNotFoundException;
import com.example.Task.Service.repository.SubTaskEntity;
import com.example.Task.Service.repository.SubTasksRepository;
import com.example.Task.Service.repository.TaskEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DeleteSubTaskImplTest {

    @Mock
    private SubTasksRepository subTasksRepository;

    private DeleteSubTaskImpl deleteSubTask;

    @BeforeEach
    void setUp() {
        deleteSubTask = new DeleteSubTaskImpl(subTasksRepository);
    }

    @Test
    void deleteSubTask_shouldDeleteOwnedSubTask() {
        Long subTaskId = 100L;
        Long userId = 20L;

        TaskEntity task = TaskEntity.builder()
                .taskId(10L)
                .userId(userId)
                .build();

        SubTaskEntity subTask = SubTaskEntity.builder()
                .subTask_id(subTaskId)
                .task(task)
                .subTask_title("Delete unit test")
                .subTask_completed(false)
                .build();

        when(subTasksRepository.findBySubTaskIdAndTask_UserId(
                subTaskId,
                userId
        )).thenReturn(Optional.of(subTask));

        deleteSubTask.deleteSubTask(subTaskId, userId);

        verify(subTasksRepository)
                .findBySubTaskIdAndTask_UserId(subTaskId, userId);

        verify(subTasksRepository).delete(subTask);
    }

    @Test
    void deleteSubTask_shouldThrowWhenSubTaskDoesNotExistForUser() {
        Long subTaskId = 100L;
        Long userId = 20L;

        when(subTasksRepository.findBySubTaskIdAndTask_UserId(
                subTaskId,
                userId
        )).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> deleteSubTask.deleteSubTask(subTaskId, userId)
        );

        assertEquals("Subtask not found", exception.getMessage());

        verify(subTasksRepository, never()).delete(
                org.mockito.ArgumentMatchers.any(SubTaskEntity.class)
        );
    }

    @Test
    void deleteSubTask_shouldNotAllowDeletingAnotherUsersSubTask() {
        Long subTaskId = 100L;
        Long authenticatedUserId = 20L;

        when(subTasksRepository.findBySubTaskIdAndTask_UserId(
                subTaskId,
                authenticatedUserId
        )).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> deleteSubTask.deleteSubTask(
                        subTaskId,
                        authenticatedUserId
                )
        );

        verify(subTasksRepository)
                .findBySubTaskIdAndTask_UserId(
                        subTaskId,
                        authenticatedUserId
                );

        verify(subTasksRepository, never()).delete(
                org.mockito.ArgumentMatchers.any(SubTaskEntity.class)
        );
    }

    @Test
    void deleteSubTask_shouldPropagateRepositoryDeleteException() {
        Long subTaskId = 100L;
        Long userId = 20L;

        TaskEntity task = TaskEntity.builder()
                .taskId(10L)
                .userId(userId)
                .build();

        SubTaskEntity subTask = SubTaskEntity.builder()
                .subTask_id(subTaskId)
                .task(task)
                .subTask_title("Repository failure test")
                .subTask_completed(false)
                .build();

        when(subTasksRepository.findBySubTaskIdAndTask_UserId(
                subTaskId,
                userId
        )).thenReturn(Optional.of(subTask));

        org.mockito.Mockito.doThrow(
                new RuntimeException("Database error")
        ).when(subTasksRepository).delete(subTask);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> deleteSubTask.deleteSubTask(subTaskId, userId)
        );

        assertEquals("Database error", exception.getMessage());

        verify(subTasksRepository).delete(subTask);
    }

}
