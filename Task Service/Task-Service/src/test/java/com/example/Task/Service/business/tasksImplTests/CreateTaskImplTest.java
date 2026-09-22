package com.example.Task.Service.business.tasksImplTests;

import com.example.Task.Service.business.Impl.tasksImpl.CreateTaskImpl;
import com.example.Task.Service.business.Impl.tasksImpl.TaskScheduleValidator;
import com.example.Task.Service.domain.TaskPriority;
import com.example.Task.Service.domain.TaskScheduleType;
import com.example.Task.Service.domain.TaskStatus;
import com.example.Task.Service.domain.tasksRequestsResponse.CreateTaskRequest;
import com.example.Task.Service.domain.tasksRequestsResponse.CreateTaskResponse;
import com.example.Task.Service.repository.TaskEntity;
import com.example.Task.Service.repository.TasksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CreateTaskImplTest {

    @Mock
    private TasksRepository tasksRepository;

    @Mock
    private TaskScheduleValidator taskScheduleValidator;

    private CreateTaskImpl createTask;

    @BeforeEach
    void setUp() {
        createTask = new CreateTaskImpl(tasksRepository, taskScheduleValidator);
    }

    @Test
    void createTask_shouldValidateSaveAndReturnTaskId() {
        long userId = 15L;

        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Study Spring Boot")
                .description("Write unit tests for the task service")
                .taskPriority(TaskPriority.HIGH)
                .scheduleType(TaskScheduleType.FIXED_TIME)
                .startDate(LocalDate.of(2026, 7, 28))
                .endDate(LocalDate.of(2026, 7, 28))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .build();

        when(tasksRepository.save(any(TaskEntity.class))).thenAnswer(invocation -> {
                    TaskEntity entity = invocation.getArgument(0);
                    entity.setTaskId(100L);
                    return entity;
                });

        CreateTaskResponse response = createTask.createTask(request, userId);

        assertEquals(100L, response.getTaskId());

        verify(taskScheduleValidator).validate(
                request.getScheduleType(),
                request.getStartDate(),
                request.getEndDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        ArgumentCaptor<TaskEntity> entityCaptor = ArgumentCaptor.forClass(TaskEntity.class);

        verify(tasksRepository).save(entityCaptor.capture());

        TaskEntity savedEntity = entityCaptor.getValue();

        assertEquals(userId, savedEntity.getUserId());
        assertEquals(request.getTitle(), savedEntity.getTaskTitle());
        assertEquals(request.getDescription(), savedEntity.getTaskDescription());
        assertEquals(request.getTaskPriority(), savedEntity.getTaskPriority());
        assertEquals(request.getScheduleType(), savedEntity.getScheduleType());
        assertEquals(request.getStartDate(), savedEntity.getStartDate());
        assertEquals(request.getEndDate(), savedEntity.getEndDate());
        assertEquals(request.getStartTime(), savedEntity.getStartTime());
        assertEquals(request.getEndTime(), savedEntity.getEndTime());
        assertEquals(TaskStatus.TODO, savedEntity.getTaskStatus());
    }

    @Test
    void createTask_shouldNotSaveWhenValidationFails() {
        long userId = 15L;

        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Invalid task")
                .description("Invalid schedule")
                .taskPriority(TaskPriority.MEDIUM)
                .scheduleType(TaskScheduleType.FIXED_TIME)
                .startDate(LocalDate.of(2026, 7, 28))
                .endDate(LocalDate.of(2026, 7, 28))
                .startTime(LocalTime.of(12, 0))
                .endTime(LocalTime.of(11, 0))
                .build();

        doThrow(new IllegalArgumentException(
                "Start time must be before end time"
        )).when(taskScheduleValidator).validate(
                request.getScheduleType(),
                request.getStartDate(),
                request.getEndDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> createTask.createTask(request, userId));

        assertEquals("Start time must be before end time", exception.getMessage());

        verify(tasksRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void createTask_shouldPropagateRepositoryException() {
        long userId = 15L;

        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Study Spring Boot")
                .description("Repository failure test")
                .taskPriority(TaskPriority.LOW)
                .scheduleType(TaskScheduleType.ALL_DAY)
                .startDate(LocalDate.of(2026, 7, 28))
                .endDate(LocalDate.of(2026, 7, 28))
                .build();

        when(tasksRepository.save(any(TaskEntity.class))).thenThrow(new RuntimeException("Database unavailable"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> createTask.createTask(request, userId));

        assertEquals("Database unavailable", exception.getMessage());

        verify(taskScheduleValidator).validate(
                request.getScheduleType(),
                request.getStartDate(),
                request.getEndDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        verify(tasksRepository).save(any(TaskEntity.class));
    }


}
