package com.nisum.restfulapi.exceptions;

import com.nisum.restfulapi.model.DTO.ResponseRegister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseRegister> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        return new ResponseEntity<ResponseRegister>(ResponseRegister.builder()
                .mensaje(ex.getAllErrors().get(0).getDefaultMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }
}
