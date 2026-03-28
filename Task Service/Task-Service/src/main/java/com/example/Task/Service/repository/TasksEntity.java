package com.example.Task.Service.repository;

import com.example.Task.Service.domain.TaskPriority;
import com.example.Task.Service.domain.TaskScheduleType;
import com.example.Task.Service.domain.TaskStatus;
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

    @Column(name = "user_id", nullable = false)
    private long user_id;

    @NotBlank
    @Column(name = "title", nullable = false)
    private String task_title;

    @NotBlank
    @Column(name = "description", nullable = false)
    private String task_description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status", nullable = false, length = 32)
    private TaskStatus task_status = TaskStatus.TODO;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_priority", nullable = false, length = 32)
    private TaskPriority task_priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type", nullable = false, length = 32)
    private TaskScheduleType schedule_type;

    @Column(name = "start_date", nullable = false)
    private LocalDate start_date;

    @Column(name = "end_date", nullable = false)
    private LocalDate end_date;

    @Column(name = "start_time")
    private LocalTime start_time;

    @Column(name = "end_time")
    private LocalTime end_time;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubTasksEntity> subTasksList = new ArrayList<>();
}