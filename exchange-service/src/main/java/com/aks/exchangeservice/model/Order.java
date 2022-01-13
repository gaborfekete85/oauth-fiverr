package com.aks.exchangeservice.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {

    private Integer id;
    private String symbol;
    private BigDecimal amount;
    private BigDecimal buyPrice;
    private BigDecimal quantity;

}
