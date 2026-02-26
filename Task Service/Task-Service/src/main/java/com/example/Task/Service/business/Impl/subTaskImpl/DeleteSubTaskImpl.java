package com.example.Task.Service.business.Impl.subTaskImpl;

import com.example.Task.Service.business.IDeleteSubTask;
import com.example.Task.Service.repository.SubTasksRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeleteSubTaskImpl implements IDeleteSubTask {
   private final SubTasksRepository subTasksRepository;

    @Transactional
    @Override
    public void deleteSubTask(long id) {
        subTasksRepository.deleteById(id);
    }
}
