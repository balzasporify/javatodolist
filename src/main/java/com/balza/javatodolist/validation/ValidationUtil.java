package com.balza.javatodolist.validation;

import com.balza.javatodolist.model.Task;
import com.balza.javatodolist.util.exception.ValidationException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static void validate(Task task) {
        if (task.getName() == null || task.getName().trim().isEmpty()) {
            throw new ValidationException("Task name cannot be empty");
        }
        if (task.getStatus() == null) {
            throw new ValidationException("Status cannot be null");
        }
        if (task.getDeadline() == null) {
            throw new ValidationException("Deadline cannot be null");
        }

        ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        if (task.getDeadline().isBefore(ZonedDateTime.now(moscowZone))) {
            throw new ValidationException("Deadline cannot be in the past");
        }

        if (task.getDescription() != null && task.getDescription().length() > 100) {
            throw new ValidationException("Description cannot be longer than 100 characters");
        }
    }
}
