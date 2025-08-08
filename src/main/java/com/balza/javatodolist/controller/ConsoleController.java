package com.balza.javatodolist.controller;

import com.balza.javatodolist.model.Status;
import com.balza.javatodolist.model.Task;
import com.balza.javatodolist.service.TaskService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleController {
    private final TaskService taskService;
    private final Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


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
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private TaskInputData readTaskDetailsFromConsole() {
        System.out.print("Enter task name: ");
        String name = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        Status status = readStatusFromConsole();
        LocalDate deadline = readDateFromConsole();
        return new TaskInputData(name, description, status, deadline);
    }

    private Status readStatusFromConsole() {
        while (true) {
            System.out.print("Enter status (TODO/IN_PROGRESS/DONE): ");
            try {
                return Status.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Please try again.");
            }
        }
    }

    private LocalDate readDateFromConsole() {
        while (true) {
            System.out.print("Enter deadline (yyyy-MM-dd): ");
            try {
                return LocalDate.parse(scanner.nextLine(), formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }
    }

    private int readTaskIdFromConsole() {
        while (true) {
            System.out.print("Enter task ID: ");
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID. Please enter a number.");
            }
        }
    }

    private void handleAddCommand() {
        TaskInputData inputData = readTaskDetailsFromConsole();
        Task task = taskService.addTask(inputData.name(), inputData.description(), inputData.status(), inputData.deadline());

        if (task != null) {
            System.out.println("Task added successfully: " + task);
        } else {
            System.out.println("Error: Could not add task. Please check data (e.g., deadline cannot be in the past).");
        }
    }

    private void handleListCommand() {
        taskService.getAllTasks().forEach(System.out::println);
    }

    private void handleEditCommand() {
        int id = readTaskIdFromConsole();
        if (taskService.findTaskById(id) == null) {
            System.out.println("Error: Task with ID " + id + " not found.");
            return;
        }

        System.out.println("Enter new details for task " + id);
        TaskInputData input = readTaskDetailsFromConsole();
        Task updatedTask = taskService.editTask(id, input.name(), input.description(), input.status(), input.deadline());

        if (updatedTask != null) {
            System.out.println("Task updated successfully: " + updatedTask);
        } else {
            System.out.println("Error: Could not update task. Please check data.");
        }
    }

    private void handleDeleteCommand() {
        int id = readTaskIdFromConsole();
        if (taskService.removeTask(id)) {
            System.out.println("Task deleted successfully");
        } else {
            System.out.println("Error: Task with ID " + id + " not found.");
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
