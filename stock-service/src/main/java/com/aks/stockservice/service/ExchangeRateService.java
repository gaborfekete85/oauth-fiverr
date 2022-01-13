package com.aks.stockservice.service;

import com.aks.stockservice.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.RoundingMode;

@Service
public class ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    @Autowired
    private WebClient webClient;

    public Order calculateQuantity(final Order order) {
        Mono<Order> response = webClient
                .post()
                .bodyValue(order)
                .retrieve().bodyToMono(Order.class);

        Order rate = response.block();
        order.setBuyPrice(rate.getBuyPrice());
        order.setQuantity(order.getAmount().divide(rate.getBuyPrice(), 2, RoundingMode.UP));
        return order;
    }

}