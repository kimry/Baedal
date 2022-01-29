package com.kimry.baedal;

import com.kimry.baedal.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ErrorResponse {
    private String message;

    ErrorResponse(String message){
        this.message = message;
    }

    public static ResponseEntity<ErrorResponse> responseEntity(ErrorCode errorCode){
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getMessage()));
    }

}
