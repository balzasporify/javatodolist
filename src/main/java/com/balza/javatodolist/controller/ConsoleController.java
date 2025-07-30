package com.balza.javatodolist.controller;

import com.balza.javatodolist.model.Status;
import com.balza.javatodolist.model.Task;
import com.balza.javatodolist.service.TaskService;
import com.balza.javatodolist.util.exception.NotExistStorageException;
import com.balza.javatodolist.util.exception.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleController {
    private final TaskService taskService;
    private final Scanner scanner;

    private record TaskInputData(String name, String description, Status status, LocalDate deadline) {
    }

    public ConsoleController(TaskService taskService) {
        this.taskService = taskService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("TODO App started. Available commands: add, list, edit, delete, filter, sort, exit");
        while (true) {
            System.out.println("Enter command: ");
            String command = scanner.nextLine().trim().toLowerCase();

            try {
                if (command.equals("exit")) {
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                }

                switch (command) {
                    case "add" -> handleAddCommand();
                    case "list" -> handleListCommand();
                    case "edit" -> handleEditCommand();
                    case "delete" -> handleDeleteCommand();
                    case "filter" -> handleFilterCommand();
                    case "sort" -> handleSortCommand();
                    default -> System.out.println("Unknown command. Try again.");
                }
            } catch (ValidationException e) {
                System.out.println("Validation error: " + e.getMessage());
            } catch (NotExistStorageException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use yyyy-mm-dd.");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. For status, use: TODO, IN_PROGRESS, DONE. For numbers, use digits.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private TaskInputData readTaskDetailsFromConsole() {
        System.out.println("Enter task name: ");
        String name = scanner.nextLine();
        System.out.println("Enter description:");
        String description = scanner.nextLine();
        System.out.println("Enter status (TODO/IN_PROGRESS/DONE):");
        Status status = Status.valueOf(scanner.nextLine().toUpperCase());
        System.out.println("Enter deadline (yyyy-mm-dd):");
        LocalDate deadline = LocalDate.parse(scanner.nextLine());
        return new TaskInputData(name, description, status, deadline);
    }

    private void handleAddCommand() {
        TaskInputData inputData = readTaskDetailsFromConsole();
        Task task = taskService.addTask(inputData.name(), inputData.description(), inputData.status(), inputData.deadline());
        System.out.println("Task added successfully: " + task);
    }

    private void handleListCommand() {
        taskService.getAllTasks().forEach(System.out::println);
    }

    private void handleEditCommand() {
        System.out.println("Enter task ID to edit:");
        int id = Integer.parseInt(scanner.nextLine());
        Task currentTask = taskService.findTaskById(id);
        System.out.println("Enter new details for current task: " + currentTask);
        TaskInputData inputData = readTaskDetailsFromConsole();
        Task updatedTask = taskService.editTask(id, inputData.name(), inputData.description(), inputData.status(), inputData.deadline());
        System.out.println("Task updated successfully: " + updatedTask);
    }

    private void handleDeleteCommand() {
        System.out.println("Enter task ID to delete:");
        int id = Integer.parseInt(scanner.nextLine());
        if (taskService.removeTask(id)) {
            System.out.println("Task deleted successfully");
        }
    }

    private void handleFilterCommand() {
        System.out.println("Enter status to filter (TODO/IN_PROGRESS/DONE): ");
        Status status = Status.valueOf(scanner.nextLine().toUpperCase());
        taskService.getTasksByStatus(status).forEach(System.out::println);
    }

    private void handleSortCommand() {
        System.out.println("Sort by (deadline/status):");
        String criterion = scanner.nextLine().toLowerCase();
        if ("deadline".equals(criterion)) {
            taskService.getSortedTasksByDeadline().forEach(System.out::println);
        } else if ("status".equals(criterion)) {
            taskService.getSortedTasksByStatus().forEach(System.out::println);
        } else {
            System.out.println("Invalid sort criterion. Use 'deadline' or 'status'.");
        }
    }
}
