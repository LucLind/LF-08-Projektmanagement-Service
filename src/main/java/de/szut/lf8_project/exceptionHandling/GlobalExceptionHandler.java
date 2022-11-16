package de.szut.lf8_project.exceptionHandling;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * The global exception handler class
 */
@ControllerAdvice
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "invalid JSON posted",
                content = @Content)
})
public class GlobalExceptionHandler {
    /**
     * resourceNotFoundException
     * @param ex the exception
     * @param request the web request
     * @return response entity not found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetailsDTO errorDetailsDTO = new ErrorDetailsDTO(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetailsDTO, HttpStatus.NOT_FOUND);
    }
     /**
      * employee not free exception
      * @param ex the exception
      * @param request the web request
      * @return response entity bad request
      * */
    @ExceptionHandler(EmployeeNotFreeException.class)
    public ResponseEntity<?> employeeNotFreeException(EmployeeNotFreeException ex, WebRequest request){
        ErrorDetailsDTO errorDetailsDTO = new ErrorDetailsDTO(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetailsDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * employee wrong skill exception
     * @param ex the exception
     * @param request the web request
     * @return response entity bad request
     * */
    @ExceptionHandler(EmployeeWrongSkillException.class)
    public ResponseEntity<?> employeeWrongSkillException(EmployeeWrongSkillException ex, WebRequest request){
        ErrorDetailsDTO errorDetailsDTO = new ErrorDetailsDTO(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetailsDTO, HttpStatus.BAD_REQUEST);
    }


}
