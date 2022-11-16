package de.szut.lf8_project.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmployeeWrongSkillException extends RuntimeException{
    public EmployeeWrongSkillException(String message){super(message);}
}
