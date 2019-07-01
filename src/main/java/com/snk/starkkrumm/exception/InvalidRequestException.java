package com.snk.starkkrumm.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@ResponseStatus(FORBIDDEN)
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException() {
        super("Invalid request.");
    }
}
