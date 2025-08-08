package com.balza.javatodolist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Task {
    private Integer uuid;
    private String name;
    private String description;
    private Status status;
    private ZonedDateTime deadline;

    public Task(String name, String description, Status status, ZonedDateTime deadline) {
        this(null, name, description, status, deadline);
    }

}
