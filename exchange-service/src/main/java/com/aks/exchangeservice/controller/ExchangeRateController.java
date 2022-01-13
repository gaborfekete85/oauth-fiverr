package com.aks.exchangeservice.controller;

import com.aks.exchangeservice.model.Order;
import com.aks.exchangeservice.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @PostMapping("/rates/rate")
    public Order getRate(HttpServletRequest request, @RequestBody Order order){
	    System.out.println("Token: " + request.getHeader("Authorization"));
        return exchangeRateService.getRate(order);
    }
}
