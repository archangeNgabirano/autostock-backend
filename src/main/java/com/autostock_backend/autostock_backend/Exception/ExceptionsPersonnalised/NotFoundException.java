package com.autostock_backend.autostock_backend.Exception.ExceptionsPersonnalised;

import com.autostock_backend.autostock_backend.Exception.ApiException;

public class NotFoundException extends ApiException {
    public NotFoundException(String message) {
        super(message, 404);
    }
}
