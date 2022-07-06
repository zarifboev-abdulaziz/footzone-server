package com.footzone.footzone.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@ResponseStatus(HttpStatus.FORBIDDEN)
@Data
public class ForBiddenException extends RuntimeException {

    private String type;

    private String message;

    public ForBiddenException(String type, String message){
        this.type = type;
        this.message = message;
    }
}
