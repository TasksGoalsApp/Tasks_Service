package com.example.Task.Service.domain.subtasksRequestsResponse;

import com.example.Task.Service.domain.SubTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetSubTasksByTaskIdResponse {
    List<SubTask> subTasks;
}
