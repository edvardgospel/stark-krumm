package com.snk.starkkrumm.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)

public class ExcelCreationException extends RuntimeException {
    public ExcelCreationException(String message) {
        super(message);
    }
}
