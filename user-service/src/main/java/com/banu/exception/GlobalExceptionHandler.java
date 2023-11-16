package com.banu.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice // contolera giden hataları yakalayıp müdahale edicek
//exceptionların dış dünyuaya açılmasını sağlayacak
public class GlobalExceptionHandler {

    @ExceptionHandler(UserManagerException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleManagerException(UserManagerException exception){
        ErrorType errorType = exception.getErrorType();
        HttpStatus httpStatus =errorType.getHttpStatus();
        return new ResponseEntity<>(createError(errorType,exception),httpStatus);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(DataIntegrityViolationException exception){
        ErrorType errorType = ErrorType.USERNAME_DUBLICATE;
        return new ResponseEntity<>(createError(errorType,exception),errorType.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        ErrorType errorType = ErrorType.BAD_REQUEST;
        List<String> fields = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(e->fields.add(e.getField()+" : "+e.getDefaultMessage()));
        ErrorMessage errorMessage = createError(errorType,exception);
        errorMessage.setFields(fields);
        return new ResponseEntity<>(errorMessage,errorType.getHttpStatus());
    }

    public ErrorMessage createError(ErrorType errorType,Exception exception){
        System.out.println("Hata oluştu: "+exception.getMessage());
        return ErrorMessage.builder()
                .code(errorType.getCode())
                .message(errorType.getMessage())
                .build();
    }
}
