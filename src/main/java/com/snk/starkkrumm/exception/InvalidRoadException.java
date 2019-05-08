package com.snk.starkkrumm.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(BAD_REQUEST)
public class InvalidRoadException extends RuntimeException {
    public InvalidRoadException(String message) {
        super(message);
    }
}
