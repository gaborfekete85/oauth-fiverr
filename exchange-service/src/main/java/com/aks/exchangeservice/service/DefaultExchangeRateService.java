package com.aks.exchangeservice.service;

import com.aks.exchangeservice.model.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Service
public class DefaultExchangeRateService implements ExchangeRateService {

    private static final Map<String, BigDecimal> SYMBOLS = new HashMap<String, BigDecimal>() {{
        put("AMZN", new BigDecimal(3300));
        put("TSLA", new BigDecimal(1100));
        put("AAPL", new BigDecimal(175));
    }};

    @Override
    public Order getRate(Order order) {
        // Query the rates from API or Repository ... Here it is hard coded in a HashMap
        BigDecimal basePrice = SYMBOLS.get(order.getSymbol());
        order.setBuyPrice(basePrice.add(new BigDecimal(Math.random() * 10)));
        return order;
    }
}