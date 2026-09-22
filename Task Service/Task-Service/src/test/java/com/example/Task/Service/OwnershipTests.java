package com.example.Task.Service;

import com.example.Task.Service.repository.*;
import com.example.Task.Service.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test") @Transactional
class OwnershipTests {
    @Autowired MockMvc mvc;
    @Autowired TasksRepository tasks;
    @Autowired SubTasksRepository subtasks;
    @Autowired ObjectMapper mapper;

    private TaskEntity task(long owner) {
        return tasks.saveAndFlush(TaskEntity.builder().userId(owner).taskTitle("Original")
                .taskDescription("Description").taskStatus(TaskStatus.TODO).taskPriority(TaskPriority.MEDIUM)
                .scheduleType(TaskScheduleType.ALL_DAY).startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(1)).subTasksList(new ArrayList<>()).build());
    }
    private String bearer(long id) throws Exception { return "Bearer " + TestTokens.token(id, "CUSTOMER"); }

    @Test void listAndUpdateAreScopedToTokenOwner() throws Exception {
        var own = task(1);
        var other = task(2);
        mvc.perform(get("/tasks/user").header("Authorization", bearer(1)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.tasks.length()").value(1))
                .andExpect(jsonPath("$.tasks[0].taskId").value(own.getTaskId()));
        var body = mapper.writeValueAsString(Map.of("taskId", other.getTaskId(), "taskTitle", "Changed",
                "taskDescription", "Description", "taskStatus", "DONE", "taskPriority", "HIGH",
                "scheduleType", "ALL_DAY", "startDate", LocalDate.now().plusDays(1).toString(),
                "endDate", LocalDate.now().plusDays(1).toString()));
        mvc.perform(put("/tasks").header("Authorization", bearer(1)).contentType("application/json").content(body))
                .andExpect(status().isNotFound());
        assertThat(tasks.findById(other.getTaskId()).orElseThrow().getTaskTitle()).isEqualTo("Original");
        mvc.perform(put("/tasks").header("Authorization", bearer(2)).contentType("application/json").content(body))
                .andExpect(status().isOk());
        assertThat(tasks.findById(other.getTaskId()).orElseThrow().getTaskTitle()).isEqualTo("Changed");
    }

    @Test void subtaskListCreateUpdateAndDeleteEnforceParentOwnership() throws Exception {
        var parent = task(2);
        var first = subtasks.saveAndFlush(SubTaskEntity.builder().task(parent).subTask_title("First").build());
        subtasks.saveAndFlush(SubTaskEntity.builder().task(parent).subTask_title("Second").build());
        mvc.perform(get("/tasks/subtasks/" + parent.getTaskId()).header("Authorization", bearer(1)))
                .andExpect(status().isNotFound());
        mvc.perform(get("/tasks/subtasks/" + parent.getTaskId()).header("Authorization", bearer(2)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.subTasks.length()").value(2));
        var create = mapper.writeValueAsString(Map.of("tasks_id", parent.getTaskId(), "subTask_title", "New",
                "completedDate", LocalDate.now().toString()));
        mvc.perform(post("/tasks/subtasks/create").header("Authorization", bearer(1)).contentType("application/json").content(create))
                .andExpect(status().isNotFound());
        mvc.perform(post("/tasks/subtasks/create").header("Authorization", bearer(2)).contentType("application/json").content(create))
                .andExpect(status().isCreated());
        var update = mapper.writeValueAsString(Map.of("subTask_id",first.getSubTask_id(),"subTask_title","Changed",
                "subTask_completed",true,"completedDate",LocalDate.now().toString()));
        mvc.perform(put("/tasks/subtasks/delete").header("Authorization", bearer(1)).contentType("application/json").content(update))
                .andExpect(status().isNotFound());
        mvc.perform(put("/tasks/subtasks/delete").header("Authorization", bearer(2)).contentType("application/json").content(update))
                .andExpect(status().isOk());
        mvc.perform(delete("/tasks/subtasks/" + first.getSubTask_id()).header("Authorization", bearer(1)))
                .andExpect(status().isNotFound());
        assertThat(subtasks.existsById(first.getSubTask_id())).isTrue();
        mvc.perform(delete("/tasks/subtasks/" + first.getSubTask_id()).header("Authorization", bearer(2)))
                .andExpect(status().isNoContent());
    }

    @Test void creationTakesOwnerFromToken() throws Exception {
        var body = mapper.writeValueAsString(Map.of("title","Created","description","Description","taskPriority","HIGH",
                "scheduleType","ALL_DAY","startDate",LocalDate.now().plusDays(1).toString(),
                "endDate",LocalDate.now().plusDays(1).toString(),"userId",999));
        mvc.perform(post("/tasks/create").header("Authorization",bearer(7)).contentType("application/json").content(body))
                .andExpect(status().isCreated());
        assertThat(tasks.findByUserId(7)).hasSize(1);
        assertThat(tasks.findByUserId(999)).isEmpty();
    }
}
