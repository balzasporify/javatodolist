package com.balza.javatodolist.service;

import com.balza.javatodolist.model.Status;
import com.balza.javatodolist.model.Task;
import com.balza.javatodolist.repository.Repository;
import com.balza.javatodolist.util.exception.ExistStorageException;
import com.balza.javatodolist.util.exception.NotExistStorageException;
import com.balza.javatodolist.util.exception.ValidationException;
import com.balza.javatodolist.validation.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.logging.Logger;

public class TaskService {
    public static final Logger LOGGER = Logger.getLogger(TaskService.class.getName());
    private final Repository repository;
    private final ZoneId moscowZoneId = ZoneId.of("Europe/Moscow");

    public TaskService(Repository repository) {
        this.repository = repository;
    }

    private ZonedDateTime convertToEndOfDayMoscow(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atTime(LocalTime.MAX).atZone(moscowZoneId);
    }

    public Task addTask(String name, String description, Status status, LocalDate deadline) {
        LOGGER.info("Processing add request for task: " + name);
        try {
            ZonedDateTime moscowDeadLine = convertToEndOfDayMoscow(deadline);
            Task task = new Task(name, description, status, moscowDeadLine);
            ValidationUtil.validate(task);
            return repository.add(task);
        } catch (ExistStorageException e) {
            LOGGER.warning("Failed to process add request for task " + name + ": " + e.getMessage());
            return null;
        } catch (ValidationException e) {
            LOGGER.warning("Validation error: " + e.getMessage());
            return null;
        }
    }

    public Task findTaskById(int id) {
        LOGGER.info("Processing find request for task id: " + id);
        try {
            return repository.findById(id);
        } catch (NotExistStorageException e) {
            LOGGER.warning("Failed to process find request for task id " + id + ": " + e.getMessage());
            return null;
        }
    }

    public Task editTask(int id, String name, String description, Status status, LocalDate deadline) {
        LOGGER.info("Processing update request for task id: " + id);
        try {
            repository.findById(id);
            ZonedDateTime moscowDeadLine = convertToEndOfDayMoscow(deadline);
            Task updatedTask = new Task(id, name, description, status, moscowDeadLine);
            ValidationUtil.validate(updatedTask);
            repository.edit(updatedTask, id);
            return updatedTask;
        } catch (NotExistStorageException e) {
            LOGGER.warning("Failed to process update request for task id " + id + ": " + e.getMessage());
            return null;
        } catch (ValidationException e) {
            LOGGER.warning("Update failed for task id " + id + ". New data is invalid: " + e.getMessage());
            return null;
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
