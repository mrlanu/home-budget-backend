package com.lanu.homebudget.strategy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Trade {
    private LocalDate date;
    private BigDecimal amount;
    private BigDecimal price;
}
