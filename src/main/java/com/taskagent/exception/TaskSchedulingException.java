package com.taskagent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TaskSchedulingException extends RuntimeException {

    public TaskSchedulingException(String message) {
        super(message);
    }
}
