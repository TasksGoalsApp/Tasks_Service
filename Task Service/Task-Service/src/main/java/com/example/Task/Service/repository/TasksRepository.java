package com.example.Task.Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TasksRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByUserId(long userId);
    Optional<TaskEntity> findByTaskIdAndUserId(Long taskId, Long userId);

}
