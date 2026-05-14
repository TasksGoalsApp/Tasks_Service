package com.example.Task.Service.business.Impl.subTaskImpl;

import com.example.Task.Service.business.IGetSubTasksByTaskId;
import com.example.Task.Service.business.SubTaskConverter;
import com.example.Task.Service.domain.SubTask;
import com.example.Task.Service.domain.subtasksRequestsResponse.GetSubTasksByTaskIdRequest;
import com.example.Task.Service.domain.subtasksRequestsResponse.GetSubTasksByTaskIdResponse;
import com.example.Task.Service.repository.SubTasksRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class GetSubTasksByTaskIdImpl implements IGetSubTasksByTaskId {
    private final SubTasksRepository subTasksRepository;

    @Transactional
    @Override
    public GetSubTasksByTaskIdResponse getSubTasksByTaskId(long  taskId) {

        List<SubTask> subTaskList = subTasksRepository.findByTask_TaskId(taskId)
                .stream()
                .map(SubTaskConverter::convert)
                .toList();

        return new GetSubTasksByTaskIdResponse(subTaskList);
    }
}
