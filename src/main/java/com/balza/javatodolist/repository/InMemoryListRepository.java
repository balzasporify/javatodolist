package com.balza.javatodolist.repository;

import com.balza.javatodolist.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryListRepository extends AbstractRepository<Integer> {
    private final List<Task> repository = new ArrayList<>();

    @Override
    protected Integer getSearchKey(Integer uuid) {
        for (int i = 0; i < size(); i++) {
            if (repository.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey != null;
    }

    @Override
    protected Task doAdd(Task task) {
        int newId = generateId();
        task.setUuid(newId);
        repository.add(task);
        return task;
    }

    @Override
    protected void doEdit(Task task, Integer searchKey) {
        repository.set(searchKey, task);
    }

    @Override
    protected void doDelete(Integer searchKey) {
        repository.remove(searchKey.intValue());
    }

    @Override
    protected Task doFindById(Integer searchKey) {
        return repository.get(searchKey);
    }

    @Override
    protected List<Task> doCopyAll() {
        return new ArrayList<>(repository);
    }

    private int size() {
        return repository.size();
    }
}
