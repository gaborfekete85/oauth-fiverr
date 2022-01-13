package com.aks.stockservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ExchangeRateException extends RuntimeException {

    public ExchangeRateException(Exception e) {
        super(e);
    }
}