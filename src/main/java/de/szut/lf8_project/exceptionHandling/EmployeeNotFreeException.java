package de.szut.lf8_project.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmployeeNotFreeException extends RuntimeException {
    public EmployeeNotFreeException(String message) {
        super(message);
    }
}
