package example.controller.handler;

import example.exception.ConditionNotMetException;
import example.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Comparator;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

   @ExceptionHandler
   @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
   public ErrorResponse handleConditionNotMet(ConditionNotMetException e) {
       return getErrorResponse(e);
   }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.NOT_FOUND)
   public ErrorResponse handleNotFound(NotFoundException e) {
       return getErrorResponse(e);
   }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public ErrorResponse handleValidation(MethodArgumentNotValidException e) {
       String message = e.getBindingResult().getFieldErrors().stream()
               .map(error -> error.getField() + ": " + error.getDefaultMessage())
               .distinct()
               .collect(Collectors.joining(", "));
       log.warn("Validation failed (argument not valid). violationsCount={}, details={}",
               e.getBindingResult().getAllErrors().size(), message);
       return new ErrorResponse(message);
   }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public ErrorResponse handleConstraintViolation(ConstraintViolationException e) {
       String message = e.getConstraintViolations().stream()
               .sorted(Comparator.comparing(v -> v.getPropertyPath().toString()))
               .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
               .collect(Collectors.joining(", "));

       log.warn("Validation failed (constraint violation). violationsCount={}, details={}",
               e.getConstraintViolations().size(), message);

       return new ErrorResponse(message);
   }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(Exception e) {
       return new ErrorResponse("Unexpected server error");
   }

    private ErrorResponse getErrorResponse(Throwable e) {
       return new ErrorResponse(e.getMessage());
   }
}
