package com.balza.javatodolist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    TODO("TODO"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String message;
}
