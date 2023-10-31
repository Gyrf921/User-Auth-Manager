package com.userauthmanager.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TokenLifetimeExpiredException extends RuntimeException{
    public TokenLifetimeExpiredException(String message){
        super(message);
    }
}
