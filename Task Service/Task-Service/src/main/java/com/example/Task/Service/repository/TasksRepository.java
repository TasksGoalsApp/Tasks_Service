package com.example.Task.Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TasksRepository extends JpaRepository<TasksEntity, Long> {
    List<TasksEntity> findByUser_id(long userId);
}
