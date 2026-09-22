package com.example.Task.Service.controller;

import com.example.Task.Service.business.ICreateTask;
import com.example.Task.Service.business.IGetAllTasksByUser;
import com.example.Task.Service.business.IUpdateTask;
import com.example.Task.Service.domain.TaskPriority;
import com.example.Task.Service.domain.TaskScheduleType;
import com.example.Task.Service.domain.TaskStatus;
import com.example.Task.Service.domain.tasksRequestsResponse.CreateTaskRequest;
import com.example.Task.Service.domain.tasksRequestsResponse.CreateTaskResponse;
import com.example.Task.Service.domain.tasksRequestsResponse.GetAllTaskByUserResponse;
import com.example.Task.Service.domain.tasksRequestsResponse.UpdateTaskRequest;
import com.example.Task.Service.domain.tasksRequestsResponse.UpdateTaskResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskController taskController;

    @MockitoBean
    private ICreateTask createTask;

    @MockitoBean
    private IUpdateTask updateTask;

    @MockitoBean
    private IGetAllTasksByUser getAllTasksByUser;

    @MockitoBean
    private JwtDecoder jwtDecoder;


    private CreateTaskRequest createValidCreateRequest() {
        return CreateTaskRequest.builder()
                .title("Write controller tests")
                .description("Test the TaskController")
                .taskPriority(TaskPriority.MEDIUM)
                .scheduleType(TaskScheduleType.FIXED_TIME)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(1))
                .build();
    }

    private UpdateTaskRequest createValidUpdateRequest() {
        return UpdateTaskRequest.builder()
                .taskTitle("Updated task")
                .taskDescription("Updated description")
                .taskStatus(TaskStatus.IN_PROGRESS)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(1))
                .build();
    }

    private Jwt createJwt(Long userId, String role) {
        Instant now = Instant.now();

        return new Jwt(
                "test-token",
                now,
                now.plusSeconds(3600),
                Map.of("alg", "none"),
                Map.of(
                        "sub", "hristo",
                        "id", userId,
                        "roles", List.of(role)
                )
        );
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        Long userId = 20L;

        Jwt jwt = mock(Jwt.class);

        when(jwt.getClaim("id")).thenReturn(userId);

        CreateTaskRequest request = createValidCreateRequest();

        CreateTaskResponse expectedResponse = CreateTaskResponse.builder()
                .taskId(100L)
                .build();

        when(createTask.createTask(request, userId))
                .thenReturn(expectedResponse);

        ResponseEntity<CreateTaskResponse> result =
                taskController.createTask(request, jwt);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());

        verify(jwt).getClaim("id");
        verify(createTask).createTask(request, userId);
    }

    @Test
    void updateTask_shouldSetPathVariableAndReturnOk() {
        Long taskId = 100L;
        Long userId = 20L;

        UpdateTaskRequest request = createValidUpdateRequest();

        UpdateTaskResponse expectedResponse = UpdateTaskResponse.builder()
                .task_id(taskId)
                .build();

        Jwt jwt = mock(Jwt.class);

        when(jwt.getClaim("id")).thenReturn(userId);
        when(updateTask.updateTask(request, userId))
                .thenReturn(expectedResponse);

        ResponseEntity<UpdateTaskResponse> result =
                taskController.updateTask(taskId, request, jwt);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        assertEquals(taskId, request.getTaskId());

        verify(jwt).getClaim("id");
        verify(updateTask).updateTask(request, userId);
    }

    @Test
    void shouldReturnBadRequestWhenCreateTaskRequestIsInvalid()
            throws Exception {

        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("")
                .description("")
                .taskPriority(null)
                .scheduleType(null)
                .startDate(null)
                .endDate(null)
                .build();

        mockMvc.perform(post("/tasks")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(createTask);
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        Long taskId = 100L;
        Long userId = 20L;

        Jwt jwt = mock(Jwt.class);

        when(jwt.getClaim("id")).thenReturn(userId);

        UpdateTaskRequest request = createValidUpdateRequest();

        UpdateTaskResponse expectedResponse = UpdateTaskResponse.builder()
                .task_id(taskId)
                .build();

        when(updateTask.updateTask(request, userId))
                .thenReturn(expectedResponse);

        ResponseEntity<UpdateTaskResponse> result =
                taskController.updateTask(taskId, request, jwt);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        assertEquals(taskId, request.getTaskId());

        verify(jwt).getClaim("id");
        verify(updateTask).updateTask(request, userId);
    }

    @Test
    void shouldReturnBadRequestWhenUpdateTaskRequestIsInvalid()
            throws Exception {

        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .taskTitle("")
                .taskDescription("")
                .build();

        mockMvc.perform(put("/tasks/{taskId}", 100L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(updateTask);
    }

    @Test
    void shouldReturnAllTasksForCurrentUser() {
        Long userId = 20L;

        Jwt jwt = mock(Jwt.class);

        when(jwt.getClaim("id")).thenReturn(userId);

        GetAllTaskByUserResponse expectedResponse =
                GetAllTaskByUserResponse.builder()
                        .build();

        when(getAllTasksByUser.getAllTaskByUser(userId))
                .thenReturn(expectedResponse);

        ResponseEntity<GetAllTaskByUserResponse> result =
                taskController.showTasks(jwt);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());

        verify(jwt).getClaim("id");
        verify(getAllTasksByUser).getAllTaskByUser(userId);
    }

    @Test
    void shouldReturnBadRequestWhenCreateTaskJsonIsMalformed() throws Exception {

        mockMvc.perform(post("/tasks")
                        .contentType(APPLICATION_JSON)
                        .content("""
                            {
                              "title": "Task",
                              "taskPriority":""
                            }
                            """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(createTask);
    }


}
