package de.szut.lf8_project.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class for the wrong skill
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmployeeWrongSkillException extends RuntimeException{
    /**
     * constructor
     * @param message message
     */
    public EmployeeWrongSkillException(String message){super(message);}
}
