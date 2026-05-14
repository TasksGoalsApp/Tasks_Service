package com.example.Task.Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubTasksRepository extends JpaRepository<SubTaskEntity, Long> {
    List<SubTaskEntity> findByTasksEntity_Task_id(Long taskId);

    Optional<SubTaskEntity> findByTaskId(long task_Id);
}
