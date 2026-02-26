package com.example.Task.Service.repository;

import com.example.Task.Service.domain.SubTasks;
import com.example.Task.Service.domain.TaskPriority;
import com.example.Task.Service.domain.TaskStatus;
import com.example.Task.Service.domain.TaskType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "tasks")
public class TasksEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private long task_id;

    @Column(name = "user_id")
    private long user_id;
    @NotBlank
    @Column(name = "title")
    private String task_title;

    @NotBlank
    @Column(name = "description")
    private String task_description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private TaskStatus task_status = TaskStatus.TODO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private TaskPriority task_priority= TaskPriority.MEDIUM;

    @NotBlank
    @Column(nullable = false)
    private LocalDate task_day;

    @NotBlank
    @Column(nullable = false)
    private LocalTime start_time;

    @NotBlank
    @Column(nullable = false)
    private LocalTime end_time;


    @Column(nullable = false)
    private boolean weekly;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubTasksEntity> subTasksList = new ArrayList<>();

}
