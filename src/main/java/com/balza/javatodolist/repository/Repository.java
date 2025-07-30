package com.balza.javatodolist.repository;

import com.balza.javatodolist.model.Status;
import com.balza.javatodolist.model.Task;

import java.util.List;

public interface Repository {
    Task add(Task task);

    Task findById(int id);

    void edit(Task updatedTask, int id);

    void delete(int id);

    List<Task> getAll();

    List<Task> filterByStatus(Status status);

    List<Task> sortByDeadline();

    List<Task> sortByStatus();

}
