package com.example.Task.Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubTasksRepository extends JpaRepository<SubTaskEntity, Long> {
    @org.springframework.data.jpa.repository.Query("select s from SubTaskEntity s where s.subTask_id = :id and s.task.userId = :userId")
    java.util.Optional<SubTaskEntity> findOwnedById(@org.springframework.data.repository.query.Param("id") long id,
            @org.springframework.data.repository.query.Param("userId") long userId);
   // List<SubTaskEntity> findByTasksEntity_Task_id(Long taskId);

    List<SubTaskEntity> findByTask_TaskId(Long taskId);
}
