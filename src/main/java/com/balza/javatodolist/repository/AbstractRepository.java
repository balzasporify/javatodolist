package com.balza.javatodolist.repository;

import com.balza.javatodolist.model.Status;
import com.balza.javatodolist.model.Task;
import com.balza.javatodolist.util.exception.ExistStorageException;
import com.balza.javatodolist.util.exception.NotExistStorageException;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class AbstractRepository<SK> implements Repository {
    private static final Logger LOGGER = Logger.getLogger(AbstractRepository.class.getName());

    protected abstract SK getSearchKey(Integer uuid);

    protected abstract boolean isExist(SK searchKey);

    protected abstract void doAdd(Task task, SK searchKey);

    protected abstract void doEdit(Task task, SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract Task doFindById(SK searchKey);

    protected abstract List<Task> doCopyAll();

    @Override
    public void add(Task task) {
        LOGGER.info("Adding task " + task);
        doAdd(task, getNotExistingSearchKey(task.getUuid()));
    }

    @Override
    public Task findById(int id) {
        LOGGER.info("Finding task with id " + id);
        return doFindById(getExistingSearchKey(id));
    }

    @Override
    public void edit(Task updatedTask, int id) {
        LOGGER.info("Editing task " + updatedTask);
        doEdit(updatedTask, getExistingSearchKey(id));
    }

    @Override
    public void delete(int id) {
        LOGGER.info("Deleting task with id " + id);
        doDelete(getExistingSearchKey(id));
    }

    @Override
    public List<Task> getAll() {
        LOGGER.info("getAll");
        return doCopyAll();
    }

    @Override
    public List<Task> filterByStatus(Status status) {
        LOGGER.info("filterByStatus");
        return doCopyAll().stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> sortByDeadline() {
        LOGGER.info("sortByDeadline");
        List<Task> list = doCopyAll();
        list.sort(Comparator.comparing(Task::getDeadline));
        return list;
    }

    @Override
    public List<Task> sortByStatus() {
        LOGGER.info("sortByStatus");
        List<Task> list = doCopyAll();
        list.sort(Comparator.comparing(Task::getStatus));
        return list;
    }

    protected SK getNotExistingSearchKey(Integer uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            LOGGER.warning("Task with id " + uuid + " already exists");
            throw new ExistStorageException("Task with id " + uuid + " already exists");
        } else {
            return searchKey;
        }
    }

    protected SK getExistingSearchKey(Integer uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            LOGGER.warning("Task with id " + uuid + " does not exist");
            throw new NotExistStorageException("Task with id " + uuid + " does not exist");
        } else {
            return searchKey;
        }
    }
}
