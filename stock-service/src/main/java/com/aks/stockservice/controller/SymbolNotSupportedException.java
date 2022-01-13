package com.aks.stockservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class SymbolNotSupportedException extends RuntimeException {

    public SymbolNotSupportedException(String message) {
        super(message);
    }
}