package com.autostock_backend.autostock_backend.Exception;

public class ApiException extends RuntimeException {
    private final int status;

    public ApiException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

