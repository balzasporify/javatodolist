package com.balza.javatodolist.service;

import com.balza.javatodolist.model.Status;
import com.balza.javatodolist.model.Task;
import com.balza.javatodolist.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;

public class TaskService {
    private final TaskRepository taskRepository;
    private int nextId = 0;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task addTask(String name, String description, Status status, LocalDate deadline) {
        return new Task(generateId(),name, description, status, deadline);
    }

    public Task updateTask(int id, String name, String description, Status status, LocalDate deadline) {
        return new Task(generateId(), name, description, status, deadline);
    }

    boolean removeTask(int id) {
        return false;
    }

    List<Task> filterTasksByStatus(Status status) {
        return null;
    }

    List<Task> getSortedTasksByStatus() {
        return null;
    }

    List<Task> getSortedTasksByDeadline() {
        return null;
    }

    private Integer generateId() {
        return nextId++;
    }
}
