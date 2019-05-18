package com.snk.starkkrumm.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)

public class ExcelUploadException extends RuntimeException {
    public ExcelUploadException(String message) {
        super(message);
    }
}
