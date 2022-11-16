package de.szut.lf8_project.exceptionHandling;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * DTO class for error details
 */
@Data
@AllArgsConstructor
public class ErrorDetailsDTO {
    private Date timestamp;
    private String message;
    private String details;
}
