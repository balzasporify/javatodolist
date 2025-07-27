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
    protected void doAdd(Task task, Integer searchKey) {
        repository.add(task);
    }

    @Override
    protected void doEdit(Task task, Integer searchKey) {
        repository.set(searchKey, task);
    }

    @Override
    protected void doDelete(Integer searchKey) {
        repository.remove(searchKey);
    }

    @Override
    protected Task doFindById(Integer searchKey) {
        return repository.get(searchKey);
    }

    @Override
    protected List<Task> doCopyAll() {
        return repository;
    }

    private int size() {
        return repository.size();
    }
}
