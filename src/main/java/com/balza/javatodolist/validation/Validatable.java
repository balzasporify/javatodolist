package com.balza.javatodolist.validation;

import com.balza.javatodolist.util.exception.ValidationException;

public interface Validatable {
    void validate() throws ValidationException;
}
