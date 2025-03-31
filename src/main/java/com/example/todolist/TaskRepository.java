package com.example.todolist;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompleted(boolean completed, Sort sort);
}
