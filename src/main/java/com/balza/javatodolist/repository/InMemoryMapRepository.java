package com.balza.javatodolist.repository;

import com.balza.javatodolist.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryMapRepository extends AbstractRepository<Task> {
    protected Map<Integer, Task> repository = new HashMap<>();

    @Override
    protected Task getSearchKey(Integer uuid) {
        return repository.get(uuid);
    }

    @Override
    protected boolean isExist(Task searchKey) {
        return searchKey != null;
    }

    @Override
    protected void doAdd(Task task, Task searchKey) {
        repository.put(task.getUuid(), task);
    }

    @Override
    protected void doEdit(Task task, Task searchKey) {
        repository.put(task.getUuid(), task);
    }

    @Override
    protected void doDelete(Task searchKey) {
        repository.remove(searchKey.getUuid());
    }

    @Override
    protected Task doFindById(Task searchKey) {
        return searchKey;
    }

    @Override
    protected List<Task> doCopyAll() {
        return new ArrayList<>(repository.values());
    }
}
