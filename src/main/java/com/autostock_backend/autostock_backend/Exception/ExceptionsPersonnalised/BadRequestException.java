package com.autostock_backend.autostock_backend.Exception.ExceptionsPersonnalised;

import com.autostock_backend.autostock_backend.Exception.ApiException;

public class BadRequestException extends ApiException {
    public BadRequestException(String message) {
        super(message, 400);
    }
}