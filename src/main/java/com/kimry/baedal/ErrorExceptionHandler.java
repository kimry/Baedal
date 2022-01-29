package com.kimry.baedal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value =  CustomException.class )
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
        return ErrorResponse.responseEntity(e.getErrorCode());
    }

}
