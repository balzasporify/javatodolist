package com.balza.javatodolist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class Task {
    @ToString.Exclude
    private final String uuid;
    private final String name;
    private final String description;
    private final Status status;
    private final LocalDate deadline;
}
