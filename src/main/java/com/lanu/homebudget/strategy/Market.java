package com.lanu.homebudget.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Market {
    @Value("${file.path}")
    private String path;
    private List<DayQuote> dayQuotes;
    private Portfolio portfolio;
    private static List<Trade> mockTrades = new ArrayList<>();

    public Market(Portfolio portfolio) {
        this.portfolio = portfolio;
        this.dayQuotes = new ArrayList<>();
    }

    @PostConstruct
    public void loadQuotes() {
        parseCSV(path);
    }

    public DataView getDataForDraw(LocalDate start, LocalDate end, BigDecimal amount, BigDecimal percent){
        DataView result = new DataView();
        portfolio.clear();
        run(start, end, amount, percent);
        List<DayQuote> filteredQuotes = filterQuotesByDate(start, end);
        BigDecimal max = filteredQuotes.stream().max(Comparator.comparing(DayQuote::getHigh)).get().getHigh();
        BigDecimal min = filteredQuotes.stream().min(Comparator.comparing(DayQuote::getLow)).get().getLow();
        BigDecimal range = max.subtract(min);
        filteredQuotes
                .forEach(dayQuote -> {
                    result.putX(dayQuote.getDate());
                    result.putOpen(dayQuote.getOpen());
                    result.putClose(dayQuote.getClose());
                    result.putHigh(dayQuote.getHigh());
                    result.putLow(dayQuote.getLow());
                });

        portfolio.getTrades().forEach(trade -> {
            double y = (trade.getPrice().subtract(min)).divide(range, RoundingMode.DOWN).doubleValue();
            result.putAnnotation(trade.getDate(), y,
                    String.format("Buy %d at %.2f", trade.getAmount().intValue(), trade.getPrice().doubleValue()));
        });
        return result;
    }

    private void run(LocalDate start, LocalDate end, BigDecimal amount, BigDecimal dropPercent){

        List<DayQuote> quotes = filterQuotesByDate(start, end);

        for (DayQuote dayQuote: quotes) {
            buyOnDropdown(dayQuote, amount, dropPercent);
            //buyByDayOfWeek(dayQuote, amount, 2);
            portfolio.countCurrentValue(dayQuote);
        }

        System.out.println(portfolio);
        BigDecimal gain = portfolio.getCurrentValue().subtract(portfolio.getInvested());
        BigDecimal gainPercent = gain.multiply(BigDecimal.valueOf(100)).divide(portfolio.getInvested(), RoundingMode.DOWN);
        System.out.println(String.format("Gain - %.2f, percent - %.2f", gain, gainPercent));
        portfolio.getTrades().forEach(System.out::println);
    }

    private void parseCSV(String path){
        try (BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
            String row;
            while ((row = reader.readLine()) != null) {
                String[] data = row.split(",");
                if (data[0].equals("Date")){
                    continue;
                }
                LocalDate localDate = LocalDate.parse(data[0]);
                dayQuotes.add(
                        new DayQuote(
                                localDate,
                                new BigDecimal(data[1]).setScale(2, RoundingMode.HALF_UP),
                                new BigDecimal(data[2]).setScale(2, RoundingMode.HALF_UP),
                                new BigDecimal(data[3]).setScale(2, RoundingMode.HALF_UP),
                                new BigDecimal(data[4]).setScale(2, RoundingMode.HALF_UP)

                        )
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<DayQuote> filterQuotesByDate(LocalDate start, LocalDate end) {
        return dayQuotes.stream()
                    .filter(dayQuote -> {
                        LocalDate localDate = dayQuote.getDate();
                        return (localDate.isAfter(start) && localDate.isBefore(end))
                                || localDate.isEqual(start) || localDate.isEqual(end);
                    }).collect(Collectors.toList());
    }

    private void buyByDayOfWeek(DayQuote dayQuote, BigDecimal value, int dayOfWeek){
        if (dayQuote.getDate().getDayOfWeek().getValue() == 2){
            portfolio.buyShares(value, dayQuote);
        }
    }

    private void buyOnDropdown(DayQuote dayQuote, BigDecimal value, BigDecimal dropPercent){

        BigDecimal amount = value.divide(dayQuote.getOpen(), RoundingMode.DOWN);

        if (mockTrades.size() == 0) {
            mockTrades.add(new Trade(dayQuote.getDate(), amount, dayQuote.getOpen()));
        }

        Trade lastTrade = mockTrades.get(mockTrades.size() - 1);

        double dropdown = (lastTrade.getPrice().subtract(lastTrade.getPrice().multiply(dropPercent)
                .divide(BigDecimal.valueOf(100), RoundingMode.DOWN)).doubleValue());
        double marketPrice = dayQuote.getOpen().doubleValue();

        if (dropdown > marketPrice) {
            portfolio.buyShares(value.multiply(BigDecimal.valueOf(mockTrades.size())), dayQuote);
            mockTrades.add(new Trade(dayQuote.getDate(), amount, dayQuote.getOpen()));
            //increase value depends on dropdown
        }

        BigDecimal sharesInMockTrades = mockTrades
                .stream()
                .map(Trade::getAmount)
                .reduce(BigDecimal::add).get();

        BigDecimal averagePriceInMockTrades = mockTrades
                .stream()
                .map(trade -> trade.getAmount().multiply(trade.getPrice()).setScale(2, RoundingMode.HALF_UP))
                .reduce(BigDecimal::add).get().divide(sharesInMockTrades, RoundingMode.DOWN);

        if ((dayQuote.getOpen().doubleValue() > averagePriceInMockTrades.doubleValue() && mockTrades.size() > 1)
                || mockTrades.get(0).getPrice().doubleValue() < marketPrice) {
            mockTrades.clear();
        }
    }
}
