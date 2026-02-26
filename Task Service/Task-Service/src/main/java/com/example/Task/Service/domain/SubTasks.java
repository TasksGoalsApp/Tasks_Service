package com.example.Task.Service.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubTasks {

    private long subTask_id;
    private String subTask_title;
    private Tasks tasks;
    private boolean subTask_completed;
    private LocalDate completedDate;

}
