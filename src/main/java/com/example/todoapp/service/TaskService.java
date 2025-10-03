package com.example.todoapp.service;

import com.example.todoapp.model.Task;
import com.example.todoapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepo;

    public TaskService(TaskRepository taskRepo) {
        this.taskRepo = taskRepo;
    }

    public List<Task> findAll() {
        return taskRepo.findAll();
    }

    public Optional<Task> findById(Long id) {
        return taskRepo.findById(id);
    }

    public Task create(Task task) {
        return taskRepo.save(task);
    }

    public void delete(Long id) {
        taskRepo.deleteById(id);
    }
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }


}

