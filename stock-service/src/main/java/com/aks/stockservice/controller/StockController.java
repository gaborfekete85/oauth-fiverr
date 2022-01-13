package com.aks.stockservice.controller;

import com.aks.stockservice.model.Order;
import com.aks.stockservice.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
public class StockController {
    private static final Set<String> SUPPORTED_SYMBOLS = Set.of("AMZN", "TSLA", "AAPL");

    @Autowired
    private ExchangeRateService exchangeRateService;

    @PostMapping("/order")
    public Order createOrder(HttpServletRequest request, @RequestBody Order order){
        System.out.println("Token: " + request.getHeader("Authorization"));
        order.setId(1);
        if(!SUPPORTED_SYMBOLS.contains(order.getSymbol())) {
            throw new SymbolNotSupportedException(String.format("%s symbol not supported. Supported symbols: %s", order.getSymbol(), SUPPORTED_SYMBOLS));
        }
        return exchangeRateService.calculateQuantity(order);
    }
}