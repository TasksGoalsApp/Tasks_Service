package com.example.Task.Service.business.tasksImplTests;

import com.example.Task.Service.business.Impl.tasksImpl.TaskScheduleValidator;
import com.example.Task.Service.business.Impl.tasksImpl.UpdateTaskImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.Task.Service.domain.TaskPriority;
import com.example.Task.Service.domain.TaskScheduleType;
import com.example.Task.Service.domain.TaskStatus;
import com.example.Task.Service.domain.tasksRequestsResponse.UpdateTaskRequest;
import com.example.Task.Service.domain.tasksRequestsResponse.UpdateTaskResponse;
import com.example.Task.Service.repository.TaskEntity;
import com.example.Task.Service.exception.ResourceNotFoundException;
import com.example.Task.Service.repository.TasksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateTaskImplTest {

    @Mock
    private TasksRepository tasksRepository;

    @Mock
    private TaskScheduleValidator taskScheduleValidator;

    private UpdateTaskImpl updateTask;

    @BeforeEach
    void setUp() {
        updateTask = new UpdateTaskImpl(tasksRepository, taskScheduleValidator);
    }

    @Test
    void updateTask_shouldUpdateOwnedTaskAndReturnResponse() {
        Long taskId = 10L;
        Long userId = 20L;

        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .taskId(taskId)
                .taskTitle("Updated task")
                .taskDescription("Updated description")
                .taskPriority(TaskPriority.HIGH)
                .taskStatus(TaskStatus.IN_PROGRESS)
                .scheduleType(TaskScheduleType.FIXED_TIME)
                .startDate(LocalDate.of(2026, 7, 29))
                .endDate(LocalDate.of(2026, 7, 29))
                .startTime(LocalTime.of(12, 0))
                .endTime(LocalTime.of(13, 0))
                .build();

        TaskEntity existingTask = TaskEntity.builder()
                .taskId(taskId)
                .userId(userId)
                .taskTitle("Old title")
                .taskDescription("Old description")
                .taskPriority(TaskPriority.LOW)
                .taskStatus(TaskStatus.TODO)
                .scheduleType(TaskScheduleType.ALL_DAY)
                .startDate(LocalDate.of(2026, 7, 28))
                .endDate(LocalDate.of(2026, 7, 28))
                .build();

        when(tasksRepository.findByTaskIdAndUserId(taskId, userId)).thenReturn(Optional.of(existingTask));

        when(tasksRepository.save(any(TaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateTaskResponse response = updateTask.updateTask(request, userId);

        verify(taskScheduleValidator).validate(
                request.getScheduleType(),
                request.getStartDate(),
                request.getEndDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);

        verify(tasksRepository).save(captor.capture());

        TaskEntity savedTask = captor.getValue();

        assertEquals(taskId, savedTask.getTaskId());
        assertEquals(userId, savedTask.getUserId());
        assertEquals("Updated task", savedTask.getTaskTitle());
        assertEquals("Updated description", savedTask.getTaskDescription());
        assertEquals(TaskPriority.HIGH, savedTask.getTaskPriority());
        assertEquals(TaskStatus.IN_PROGRESS, savedTask.getTaskStatus());
        assertEquals(TaskScheduleType.FIXED_TIME, savedTask.getScheduleType());
        assertEquals(LocalDate.of(2026, 7, 29), savedTask.getStartDate());
        assertEquals(LocalDate.of(2026, 7, 29), savedTask.getEndDate());
        assertEquals(LocalTime.of(12, 0), savedTask.getStartTime());
        assertEquals(LocalTime.of(13, 0), savedTask.getEndTime());

        assertEquals(taskId, response.getTask_id());
    }

    @Test
    void updateTask_shouldThrowWhenOwnedTaskDoesNotExist() {
        Long taskId = 10L;
        Long userId = 20L;

        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .taskId(taskId)
                .taskTitle("Updated task")
                .taskDescription("Updated description")
                .taskPriority(TaskPriority.HIGH)
                .taskStatus(TaskStatus.IN_PROGRESS)
                .scheduleType(TaskScheduleType.ALL_DAY)
                .startDate(LocalDate.of(2026, 7, 29))
                .endDate(LocalDate.of(2026, 7, 29))
                .build();

        when(tasksRepository.findByTaskIdAndUserId(taskId, userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> updateTask.updateTask(request, userId));

        assertEquals("Task not found", exception.getMessage());

        verify(taskScheduleValidator, never()).validate(
                any(),
                any(),
                any(),
                any(),
                any()
        );

        verify(tasksRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void updateTask_shouldNotSaveWhenScheduleValidationFails() {
        Long taskId = 10L;
        Long userId = 20L;

        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .taskId(taskId)
                .taskTitle("Updated task")
                .taskDescription("Invalid schedule")
                .taskPriority(TaskPriority.MEDIUM)
                .taskStatus(TaskStatus.TODO)
                .scheduleType(TaskScheduleType.FIXED_TIME)
                .startDate(LocalDate.of(2026, 7, 29))
                .endDate(LocalDate.of(2026, 7, 29))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(13, 0))
                .build();

        TaskEntity existingTask = TaskEntity.builder()
                .taskId(taskId)
                .userId(userId)
                .build();

        when(tasksRepository.findByTaskIdAndUserId(taskId, userId)).thenReturn(Optional.of(existingTask));

        org.mockito.Mockito.doThrow(new IllegalArgumentException("Start time must be before end time")).when(taskScheduleValidator).validate(
                request.getScheduleType(),
                request.getStartDate(),
                request.getEndDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> updateTask.updateTask(request, userId));

        assertEquals("Start time must be before end time", exception.getMessage());

        verify(tasksRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void updateTask_shouldNotAllowUpdatingAnotherUsersTask() {
        Long taskId = 10L;
        Long authenticatedUserId = 20L;

        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .taskId(taskId)
                .taskTitle("Unauthorized update")
                .taskDescription("Should not be saved")
                .taskPriority(TaskPriority.HIGH)
                .taskStatus(TaskStatus.IN_PROGRESS)
                .scheduleType(TaskScheduleType.ALL_DAY)
                .startDate(LocalDate.of(2026, 7, 29))
                .endDate(LocalDate.of(2026, 7, 29))
                .build();

        when(tasksRepository.findByTaskIdAndUserId(taskId, authenticatedUserId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> updateTask.updateTask(request, authenticatedUserId));

        verify(tasksRepository).findByTaskIdAndUserId(taskId, authenticatedUserId);

        verify(tasksRepository, never()).save(any(TaskEntity.class));
    }

}
