package com.example.Task.Service.business.Impl.tasksImpl;

import com.example.Task.Service.business.IGetAllTasksByUser;
import com.example.Task.Service.business.TasksConvertor;
import com.example.Task.Service.domain.Task;
import com.example.Task.Service.domain.tasksRequestsResponse.GetAllTaskByUserResponse;
import com.example.Task.Service.repository.TaskEntity;
import com.example.Task.Service.repository.TasksRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class GetAllTasksByUserImpl implements IGetAllTasksByUser {

    private final TasksRepository tasksRepository;

    @Transactional
    @Override
    public GetAllTaskByUserResponse getAllTaskByUser(long userId) {
        List<TaskEntity> entities = tasksRepository.findByUserId(userId);

        List<Task> tasks = entities.stream()
                .map(TasksConvertor::convert)
                .toList();

        return GetAllTaskByUserResponse.builder()
                .tasks(tasks)
                .build();
    }



}
