package com.balza.javatodolist.util.exception;

public class NotExistStorageException extends RuntimeException {
    public NotExistStorageException(String message) {
        super(message);
    }
}
