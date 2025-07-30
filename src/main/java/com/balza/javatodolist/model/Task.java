package com.balza.javatodolist.model;

import com.balza.javatodolist.util.exception.ValidationException;
import com.balza.javatodolist.validation.Validatable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Task implements Validatable {
    private Integer uuid;
    private String name;
    private String description;
    private Status status;
    private LocalDate deadline;

    public Task(String name, String description, Status status, LocalDate deadline) {
        this(null, name, description, status, deadline);
    }

    @Override
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Task name cannot be empty");
        }
        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            throw new ValidationException("Deadline cannot be in the past");
        }
        if (description.length() > 100) {
            throw new ValidationException("Description cannot be longer than 100 characters");
        }
    }
}
