package com.userauthmanager.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class BadLoginOrPasswordException extends RuntimeException {

    public BadLoginOrPasswordException(String message){
        super(message);
    }

}
