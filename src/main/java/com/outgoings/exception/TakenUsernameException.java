package com.outgoings.exception;

public class TakenUsernameException extends RuntimeException {
    public TakenUsernameException(String message) {
        super(message);
    }

    public TakenUsernameException(String message, Throwable cause) {
        super(message, cause);
    }

    public TakenUsernameException(Throwable cause) {
        super(cause);
    }
}
