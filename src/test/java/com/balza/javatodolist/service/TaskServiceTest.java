package com.balza.javatodolist.service;

import com.balza.javatodolist.model.Status;
import com.balza.javatodolist.model.Task;
import com.balza.javatodolist.repository.Repository;
import com.balza.javatodolist.util.exception.NotExistStorageException;
import com.balza.javatodolist.util.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private Repository repository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void addTask_shouldReturnTaskWithId_whenDataIsValid() {
        String name = "New Task";
        String description = "Description";
        LocalDate deadline = LocalDate.now().plusDays(1);
        Task taskWithId = new Task(0, name, description, Status.TODO, deadline);

        when(repository.add(any(Task.class))).thenReturn(taskWithId);

        Task result = taskService.addTask(name, description, Status.TODO, deadline);

        assertNotNull(result);
        assertEquals(0, result.getUuid());
        assertEquals(name, result.getName());
        verify(repository).add(any(Task.class));
    }

    @Test
    void addTask_shouldThrowValidationException_whenDeadlineIsInPast() {
        LocalDate pastDeadline = LocalDate.now().minusDays(1);

        assertThrows(ValidationException.class, () -> taskService.addTask("Invalid Task", "Desc", Status.TODO, pastDeadline));

        verify(repository, never()).add(any(Task.class));
    }

    @Test
    void findTaskById_shouldReturnTask_whenTaskExists() {
        int taskId = 1;
        Task expectedTask = new Task(taskId, "Existing Task", "Desc", Status.IN_PROGRESS, LocalDate.now());
        when(repository.findById(taskId)).thenReturn(expectedTask);

        Task actualTask = taskService.findTaskById(taskId);

        assertNotNull(actualTask);
        assertEquals(taskId, actualTask.getUuid());
        verify(repository).findById(taskId);
    }

    @Test
    void findTaskById_shouldThrowException_whenTaskDoesNotExist() {
        int taskId = 99;
        when(repository.findById(taskId)).thenThrow(new NotExistStorageException("Task not found"));

        assertThrows(NotExistStorageException.class, () -> taskService.findTaskById(taskId));
    }

    @Test
    void editTask_shouldUpdateTask_whenDataIsValid() {
        int taskId = 1;
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        taskService.editTask(taskId, "Updated Name", "Updated Desc", Status.DONE, LocalDate.now().plusDays(5));

        verify(repository).edit(taskCaptor.capture(), eq(taskId));

        Task capturedTask = taskCaptor.getValue();
        assertEquals(taskId, capturedTask.getUuid());
        assertEquals("Updated Name", capturedTask.getName());
        assertEquals(Status.DONE, capturedTask.getStatus());
    }

    @Test
    void removeTask_shouldReturnTrue_whenTaskExists() {
        int taskId = 1;

        boolean result = taskService.removeTask(taskId);

        assertTrue(result);
        verify(repository).delete(taskId);
    }

    @Test
    void removeTask_shouldReturnFalse_whenTaskDoesNotExist() {
        int taskId = 99;
        doThrow(new NotExistStorageException("Not Found")).when(repository).delete(taskId);

        boolean result = taskService.removeTask(taskId);

        assertFalse(result);
        verify(repository).delete(taskId);
    }

    @Test
    void getAllTasks_shouldReturnListOfTasks() {
        List<Task> expectedTasks = List.of(new Task(1, "Task 1", "d", Status.TODO, LocalDate.now()));
        when(repository.getAll()).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getAllTasks();

        assertEquals(expectedTasks, actualTasks);
        verify(repository).getAll();
    }

    @Test
    void getTasksByStatus_shouldReturnFilteredList() {
        List<Task> expectedTasks = List.of(new Task(1, "Done Task", "d", Status.DONE, LocalDate.now()));
        when(repository.filterByStatus(Status.DONE)).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getTasksByStatus(Status.DONE);

        assertEquals(expectedTasks, actualTasks);
        verify(repository).filterByStatus(Status.DONE);
    }

    @Test
    void getSortedTasksByDeadline_shouldReturnSortedListByDeadLine() {
        List<Task> expectedTasks = List.of(
                new Task(1, "Task 1", "d", Status.TODO, LocalDate.parse("2025-08-01")),
                new Task(2, "Task 2", "d", Status.TODO, LocalDate.parse("2025-08-10"))
        );
        when(repository.sortByDeadline()).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getSortedTasksByDeadline();

        assertEquals(expectedTasks, actualTasks);
        verify(repository).sortByDeadline();
    }

    @Test
    void getSortedTasksByStatus_shouldReturnSortedListByStatus() {
        List<Task> expectedTasks = List.of(
                new Task(1, "Task A", "d", Status.DONE, LocalDate.now()),
                new Task(2, "Task B", "d", Status.IN_PROGRESS, LocalDate.now())
        );
        when(repository.sortByStatus()).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getSortedTasksByStatus();

        assertEquals(expectedTasks, actualTasks);
        verify(repository).sortByStatus();
    }
}