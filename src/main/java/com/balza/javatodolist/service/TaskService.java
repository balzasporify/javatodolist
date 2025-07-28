package com.balza.javatodolist.service;

import com.balza.javatodolist.model.Status;
import com.balza.javatodolist.model.Task;
import com.balza.javatodolist.repository.Repository;
import com.balza.javatodolist.util.exception.ExistStorageException;
import com.balza.javatodolist.util.exception.NotExistStorageException;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

public class TaskService {
    public static final Logger LOGGER = Logger.getLogger(TaskService.class.getName());
    private final Repository repository;
    private int nextId = 0;

    public TaskService(Repository repository) {
        this.repository = repository;
    }

    private Integer generateId() {
        return nextId++;
    }

    public Task addTask(String name, String description, Status status, LocalDate deadline) {
        LOGGER.info("Processing add request for task: " + name);
        Task task = new Task(generateId(), name, description, status, deadline);
        task.validate();
        try {
            repository.add(task);
            return task;
        } catch (ExistStorageException e) {
            LOGGER.warning("Failed to process add request for task " + name + ": " + e.getMessage());
            throw e;
        }
    }

    public Task findTaskById(int id) {
        LOGGER.info("Processing find request for task id: " + id);
        try {
            return repository.findById(id);
        } catch (NotExistStorageException e) {
            LOGGER.warning("Failed to process find request for task id " + id + ": " + e.getMessage());
            throw e;
        }
    }

    public Task editTask(int id, String name, String description, Status status, LocalDate deadline) {
        LOGGER.info("Processing update request for task id: " + id);
        Task updatedTask = new Task(id, name, description, status, deadline);
        updatedTask.validate();
        try {
            repository.edit(updatedTask, id);
            return updatedTask;
        } catch (NotExistStorageException e) {
            LOGGER.warning("Failed to process update request for task id " + id + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean removeTask(int id) {
        LOGGER.info("Processing remove request for task id: " + id);
        try {
            repository.delete(id);
            return true;
        } catch (NotExistStorageException e) {
            LOGGER.warning("Failed to process remove request for task id " + id + ": " + e.getMessage());
            return false;
        }
    }

    public List<Task> getAllTasks() {
        LOGGER.info("Processing get all tasks request");
        return repository.getAll();
    }

    public List<Task> getTasksByStatus(Status status) {
        LOGGER.info("Processing filter request for tasks with status: " + status);
        return repository.filterByStatus(status);
    }

    public List<Task> getSortedTasksByDeadline() {
        LOGGER.info("Processing sort request for tasks by deadline");
        return repository.sortByDeadline();
    }

    public List<Task> getSortedTasksByStatus() {
        LOGGER.info("Processing sort request for tasks by status");
        return repository.sortByStatus();
    }
}
