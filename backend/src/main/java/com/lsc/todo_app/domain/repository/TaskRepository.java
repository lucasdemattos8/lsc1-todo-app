package com.lsc.todo_app.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lsc.todo_app.domain.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
