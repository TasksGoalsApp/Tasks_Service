package com.example.Task.Service.business.tasksImplTests;


import com.example.Task.Service.business.Impl.tasksImpl.GetAllTasksByUserImpl;
import com.example.Task.Service.domain.Task;
import com.example.Task.Service.domain.TaskPriority;
import com.example.Task.Service.domain.TaskScheduleType;
import com.example.Task.Service.domain.TaskStatus;
import com.example.Task.Service.domain.tasksRequestsResponse.GetAllTaskByUserResponse;
import com.example.Task.Service.repository.SubTaskEntity;
import com.example.Task.Service.repository.TaskEntity;
import com.example.Task.Service.repository.TasksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GetAllTasksByUserImplTest {

    @Mock
    private TasksRepository tasksRepository;

    private GetAllTasksByUserImpl getAllTasksByUser;

    @BeforeEach
    void setUp() {
        getAllTasksByUser = new GetAllTasksByUserImpl(tasksRepository);
    }

    @Test
    void getAllTasksByUser_shouldReturnMappedTasks() {
        Long userId = 20L;

        TaskEntity firstTask = TaskEntity.builder()
                .taskId(1L)
                .userId(userId)
                .taskTitle("Write tests")
                .taskDescription("Increase coverage")
                .taskPriority(TaskPriority.HIGH)
                .taskStatus(TaskStatus.IN_PROGRESS)
                .scheduleType(TaskScheduleType.FIXED_TIME)
                .startDate(LocalDate.of(2026, 7, 30))
                .endDate(LocalDate.of(2026, 7, 30))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .subTasksList(List.of())
                .build();

        TaskEntity secondTask = TaskEntity.builder()
                .taskId(2L)
                .userId(userId)
                .taskTitle("Review SonarQube")
                .taskDescription("Check quality gate")
                .taskPriority(TaskPriority.MEDIUM)
                .taskStatus(TaskStatus.TODO)
                .scheduleType(TaskScheduleType.ALL_DAY)
                .startDate(LocalDate.of(2026, 7, 31))
                .endDate(LocalDate.of(2026, 7, 31))
                .subTasksList(List.of())
                .build();

        when(tasksRepository.findByUserId(userId)).thenReturn(List.of(firstTask, secondTask));

        GetAllTaskByUserResponse response = getAllTasksByUser.getAllTaskByUser(userId);

        assertEquals(2, response.getTasks().size());

        Task firstResponse = response.getTasks().getFirst();

        assertEquals(1L, firstResponse.getTaskId());
        assertEquals("Write tests", firstResponse.getTaskTitle());
        assertEquals("Increase coverage", firstResponse.getTaskDescription());
        assertEquals(TaskPriority.HIGH, firstResponse.getTaskPriority());
        assertEquals(TaskStatus.IN_PROGRESS, firstResponse.getTaskStatus());
        assertEquals(TaskScheduleType.FIXED_TIME, firstResponse.getScheduleType());
        assertEquals(LocalDate.of(2026, 7, 30), firstResponse.getStartDate());
        assertEquals(LocalTime.of(10, 0), firstResponse.getStartTime());

        Task secondResponse = response.getTasks().get(1);

        assertEquals(2L, secondResponse.getTaskId());
        assertEquals("Review SonarQube", secondResponse.getTaskTitle());
        assertEquals(TaskStatus.TODO, secondResponse.getTaskStatus());
        assertEquals(
                TaskScheduleType.ALL_DAY,
                secondResponse.getScheduleType()
        );

        verify(tasksRepository).findByUserId(userId);
    }

    @Test
    void getAllTasksByUser_shouldMapSubTasks() {
        Long userId = 20L;

        TaskEntity task = TaskEntity.builder()
                .taskId(1L)
                .userId(userId)
                .taskTitle("Task with subtasks")
                .taskDescription("Mapping test")
                .taskPriority(TaskPriority.HIGH)
                .taskStatus(TaskStatus.TODO)
                .scheduleType(TaskScheduleType.ALL_DAY)
                .startDate(LocalDate.of(2026, 7, 30))
                .endDate(LocalDate.of(2026, 7, 30))
                .build();

        SubTaskEntity incompleteSubTask = SubTaskEntity.builder()
                .subTask_id(100L)
                .task(task)
                .subTask_title("Incomplete subtask")
                .subTask_completed(false)
                .build();

        SubTaskEntity completedSubTask = SubTaskEntity.builder()
                .subTask_id(101L)
                .task(task)
                .subTask_title("Completed subtask")
                .subTask_completed(true)
                .completedDate(LocalDate.of(2026, 7, 29))
                .build();

        task.setSubTasksList(
                List.of(incompleteSubTask, completedSubTask)
        );

        when(tasksRepository.findByUserId(userId)).thenReturn(List.of(task));

        GetAllTaskByUserResponse response = getAllTasksByUser.getAllTaskByUser(userId);

        assertEquals(1, response.getTasks().size());
        assertEquals(2, response.getTasks().get(0).getSubTaskList().size());

        assertEquals(100L, response.getTasks().get(0).getSubTaskList().get(0).getSubTask_id());
        assertFalse(response.getTasks().get(0).getSubTaskList().get(0).isSubTask_completed());

        assertEquals(101L, response.getTasks().get(0).getSubTaskList().get(1).getSubTask_id());
        assertTrue(response.getTasks().get(0).getSubTaskList().get(1).isSubTask_completed());
        assertEquals(LocalDate.of(2026, 7, 29), response.getTasks().get(0).getSubTaskList().get(1).getCompletedDate());
    }

    @Test
    void getAllTasksByUser_shouldReturnEmptyListWhenUserHasNoTasks() {
        Long userId = 20L;

        when(tasksRepository.findByUserId(userId))
                .thenReturn(List.of());

        GetAllTaskByUserResponse response = getAllTasksByUser.getAllTaskByUser(userId);

        assertTrue(response.getTasks().isEmpty());

        verify(tasksRepository).findByUserId(userId);
    }

    @Test
    void getAllTasksByUser_shouldPropagateRepositoryException() {
        Long userId = 20L;

        when(tasksRepository.findByUserId(userId)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> getAllTasksByUser.getAllTaskByUser(userId));

        assertEquals("Database error", exception.getMessage());
    }

}
