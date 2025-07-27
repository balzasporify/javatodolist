package com.balza.javatodolist.service;

import com.balza.javatodolist.model.Status;
import com.balza.javatodolist.model.Task;
import com.balza.javatodolist.repository.Repository;

import java.time.LocalDate;
import java.util.List;

public class TaskService {
    private final Repository repository;
    private int nextId = 0;

    public TaskService(Repository repository) {
        this.repository = repository;
    }

    private Integer generateId() {
        return nextId++;
    }
}
