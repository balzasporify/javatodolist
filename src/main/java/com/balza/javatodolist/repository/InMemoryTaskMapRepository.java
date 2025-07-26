package com.balza.javatodolist.repository;

import com.balza.javatodolist.model.Status;
import com.balza.javatodolist.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryTaskMapRepository implements TaskRepository{
    private final Map<Integer,Task> repository = new HashMap<>();

    @Override
    public Task add(Task task) {
        return null;
    }

    @Override
    public Optional<Task> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Task edit(int id, Task updatedTask) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public List<Task> getAll() {
        return List.of();
    }

    @Override
    public List<Task> filterByStatus(Status status) {
        return List.of();
    }

    @Override
    public List<Task> sortByDeadline() {
        return List.of();
    }

    @Override
    public List<Task> sortByStatus() {
        return List.of();
    }
}
