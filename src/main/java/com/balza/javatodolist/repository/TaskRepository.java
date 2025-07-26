package com.balza.javatodolist.repository;

import com.balza.javatodolist.model.Status;
import com.balza.javatodolist.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task add(Task task);

    Optional<Task> findById(int id);

    Task edit(int id, Task updatedTask);

    boolean delete(int id);

    List<Task> getAll();

    List<Task> filterByStatus(Status status);

    List<Task> sortByDeadline();

    List<Task> sortByStatus();

}
