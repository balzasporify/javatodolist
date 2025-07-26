package com.balza.javatodolist.model;

import com.balza.javatodolist.util.exception.ValidationException;
import com.balza.javatodolist.validation.Validatable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class Task implements Validatable {
    private final Integer uuid;
    private String name;
    private String description;
    private Status status;
    private LocalDate deadline;

    @Override
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Task name cannot be empty");
        }
        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            throw new ValidationException("Deadline cannot be in the past");
        }
    }
}
