package de.szut.lf8_project.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class for employee is not free
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmployeeNotFreeException extends RuntimeException {
    /**
     * constructor
     * @param message message
     */
    public EmployeeNotFreeException(String message) {
        super(message);
    }
}
