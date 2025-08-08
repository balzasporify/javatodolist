package com.balza.javatodolist.service;

import com.balza.javatodolist.model.Status;
import com.balza.javatodolist.model.Task;
import com.balza.javatodolist.repository.Repository;
import com.balza.javatodolist.util.exception.NotExistStorageException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZonedDateTime;
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
    @DisplayName("addTask | Успешно добавляет задачу, если данные корректны")
    void addTask_shouldReturnTaskWithId_whenDataIsValid() {
        LocalDate validDate = LocalDate.now().plusDays(1);
        Task taskWithId = new Task(0, "New Task", "Desc", Status.TODO, ZonedDateTime.now());

        when(repository.add(any(Task.class))).thenReturn(taskWithId);

        Task result = taskService.addTask("New Task", "Desc", Status.TODO, validDate);

        assertNotNull(result);
        assertEquals(0, result.getUuid());
        verify(repository).add(any(Task.class));
    }

    @Test
    @DisplayName("addTask | Возвращает null, если данные для задачи невалидны")
    void addTask_shouldReturnNull_whenDataIsInvalid() {
        LocalDate pastDate = LocalDate.now().minusDays(1);

        Task result = taskService.addTask("Invalid Task", "Desc", Status.TODO, pastDate);

        assertNull(result);
        verify(repository, never()).add(any(Task.class));
    }

    @Test
    @DisplayName("findTaskById | Возвращает задачу, если она существует")
    void findTaskById_shouldReturnTask_whenTaskExists() {
        int taskId = 1;
        Task expectedTask = new Task(taskId, "Existing Task", "Desc", Status.IN_PROGRESS, ZonedDateTime.now());
        when(repository.findById(taskId)).thenReturn(expectedTask);

        Task actualTask = taskService.findTaskById(taskId);

        assertEquals(expectedTask, actualTask);
        verify(repository).findById(taskId);
    }

    @Test
    @DisplayName("findTaskById | Возвращает null, если задача не существует")
    void findTaskById_shouldReturnNull_whenTaskDoesNotExist() {
        int taskId = 99;
        when(repository.findById(taskId)).thenThrow(new NotExistStorageException("..."));

        Task result = taskService.findTaskById(taskId);

        assertNull(result);
    }

    @Test
    @DisplayName("editTask | Успешно редактирует задачу, если она существует и данные валидны")
    void editTask_shouldReturnUpdatedTask_whenSuccessful() {
        int taskId = 1;
        LocalDate validDate = LocalDate.now().plusDays(1);
        Task existingTask = new Task(taskId, "Old Name", "Old Desc", Status.TODO, ZonedDateTime.now());

        when(repository.findById(taskId)).thenReturn(existingTask);

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        Task result = taskService.editTask(taskId, "New Name", "New Desc", Status.IN_PROGRESS, validDate);

        assertNotNull(result);
        assertEquals("New Name", result.getName());

        verify(repository).edit(taskCaptor.capture(), eq(taskId));
        assertEquals("New Name", taskCaptor.getValue().getName());
    }

    @Test
    @DisplayName("editTask | Возвращает null, если задача для редактирования не найдена")
    void editTask_shouldReturnNull_whenTaskNotFound() {
        int taskId = 99;
        LocalDate validDate = LocalDate.now().plusDays(1);
        when(repository.findById(taskId)).thenThrow(new NotExistStorageException("..."));

        Task result = taskService.editTask(taskId, "any", "any", Status.TODO, validDate);

        assertNull(result);
        verify(repository, never()).edit(any(), anyInt());
    }

    @Test
    @DisplayName("removeTask | Возвращает true, если задача успешно удалена")
    void removeTask_shouldReturnTrue_whenTaskExists() {
        int taskId = 1;

        boolean result = taskService.removeTask(taskId);

        assertTrue(result);
        verify(repository).delete(taskId);
    }

    @Test
    @DisplayName("removeTask | Возвращает false, если задача для удаления не найдена")
    void removeTask_shouldReturnFalse_whenTaskDoesNotExist() {
        int taskId = 99;
        doThrow(new NotExistStorageException("...")).when(repository).delete(taskId);

        boolean result = taskService.removeTask(taskId);

        assertFalse(result);
        verify(repository).delete(taskId);
    }

    @Test
    @DisplayName("getAllTasks | Возвращает список всех задач")
    void getAllTasks_shouldReturnListOfTasks() {
        List<Task> expectedTasks = List.of(new Task(1, "Task 1", "d", Status.TODO, ZonedDateTime.now()));
        when(repository.getAll()).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getAllTasks();

        assertEquals(expectedTasks, actualTasks);
        verify(repository).getAll();
    }

    @Test
    @DisplayName("getTasksByStatus | Возвращает отфильтрованный по статусу список")
    void getTasksByStatus_shouldReturnFilteredList() {
        List<Task> expectedTasks = List.of(new Task(1, "Done Task", "d", Status.DONE, ZonedDateTime.now()));
        when(repository.filterByStatus(Status.DONE)).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getTasksByStatus(Status.DONE);

        assertEquals(expectedTasks, actualTasks);
        verify(repository).filterByStatus(Status.DONE);
    }

    @Test
    @DisplayName("getSortedTasksByDeadline | Возвращает отсортированный по дедлайну список")
    void getSortedTasksByDeadline_shouldReturnSortedListByDeadLine() {
        List<Task> expectedTasks = List.of(
                new Task(1, "Task 1", "d", Status.TODO, ZonedDateTime.parse("2025-08-01T00:00:00Z")),
                new Task(2, "Task 2", "d", Status.TODO, ZonedDateTime.parse("2025-08-10T00:00:00Z"))
        );
        when(repository.sortByDeadline()).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getSortedTasksByDeadline();

        assertEquals(expectedTasks, actualTasks);
        verify(repository).sortByDeadline();
    }

    @Test
    @DisplayName("getSortedTasksByStatus | Возвращает отсортированный по статусу список")
    void getSortedTasksByStatus_shouldReturnSortedListByStatus() {
        List<Task> expectedTasks = List.of(
                new Task(1, "Task A", "d", Status.DONE, ZonedDateTime.now()),
                new Task(2, "Task B", "d", Status.IN_PROGRESS, ZonedDateTime.now())
        );
        when(repository.sortByStatus()).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getSortedTasksByStatus();

        assertEquals(expectedTasks, actualTasks);
        verify(repository).sortByStatus();
    }
}