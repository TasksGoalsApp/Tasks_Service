package com.example.Task.Service.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sub_tasks")
public class SubTaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subtask_id", nullable = false)
    private long subTask_id;
    @NotBlank
    @Length( max = 50)
    @Column(name = "subtask_title")
    private String subTask_title;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="task_id")
    private TaskEntity task;


    @Column(name = "subtask_completed")
    private boolean subTask_completed;


    @Column(name = "completedDate")
    private LocalDate completedDate;
}
