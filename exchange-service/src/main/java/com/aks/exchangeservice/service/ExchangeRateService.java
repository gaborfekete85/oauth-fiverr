package com.aks.exchangeservice.service;

import com.aks.exchangeservice.model.Order;

public interface ExchangeRateService {

    Order getRate(Order order);
}
