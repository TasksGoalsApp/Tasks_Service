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
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private long taskId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @NotBlank
    @Column(name = "title", nullable = false)
    private String taskTitle;

    @NotBlank
    @Column(name = "description", nullable = false)
    private String taskDescription;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "task_status", nullable = false, length = 32)
    private TaskStatus taskStatus = TaskStatus.TODO;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_priority", nullable = false, length = 32)
    private TaskPriority taskPriority;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type", nullable = false, length = 32)
    private TaskScheduleType scheduleType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Builder.Default
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubTaskEntity> subTasksList = new ArrayList<>();
}