package com.example.Task.Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubTasksRepository extends JpaRepository<SubTasksEntity, Long> {

}
