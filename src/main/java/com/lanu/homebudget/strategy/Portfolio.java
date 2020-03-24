package com.lanu.homebudget.strategy;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class Portfolio {
    private List<Trade> trades;
    private BigDecimal invested;
    private BigDecimal sharesAmount;
    private BigDecimal averagePrice;
    private BigDecimal currentValue;

    public Portfolio() {
        this.trades = new ArrayList<>();
        this.invested = BigDecimal.ZERO;
        this.sharesAmount = BigDecimal.ZERO;
        this.averagePrice = BigDecimal.ZERO;
        this.currentValue = BigDecimal.ZERO;
    }

    public void buyShares(BigDecimal investmentAmount, DayQuote dayQuote){
        BigDecimal amount = investmentAmount.divide(dayQuote.getOpen(), RoundingMode.DOWN);

        trades.add(new Trade(dayQuote.getDate(), amount, dayQuote.getOpen()));
        invested = invested.add(dayQuote.getOpen().multiply(amount)).setScale(2, RoundingMode.HALF_UP);
        sharesAmount = sharesAmount.add(amount);
        averagePrice = trades
                .stream()
                .map(trade -> trade.getAmount().multiply(trade.getPrice()).setScale(2, RoundingMode.HALF_UP))
                .reduce(BigDecimal::add).get().divide(sharesAmount, RoundingMode.DOWN);
    }

    public void countCurrentValue(DayQuote dayQuote){
        currentValue = sharesAmount.multiply(dayQuote.getClose());
    }

    public void clear(){
        trades.clear();
        invested = BigDecimal.ZERO;
        sharesAmount = BigDecimal.ZERO;
        averagePrice = BigDecimal.ZERO;
        currentValue = BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "trades=" + trades.size() +
                ", invested=" + invested +
                ", sharesAmount=" + sharesAmount +
                ", averagePrice=" + averagePrice +
                ", currentValue=" + currentValue +
                '}';
    }
}
