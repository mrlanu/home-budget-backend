package com.lanu.homebudget.strategy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
public class DataController {

    private Market market;

    public DataController(Market market) {
        this.market = market;
    }

    @GetMapping("/test")
    public DataView getData(@RequestParam(name = "startDate") String startDateStr,
                            @RequestParam(name = "endDate") String endDateStr,
                            @RequestParam(name = "amount") BigDecimal amount,
                            @RequestParam(name = "percentDropdown") BigDecimal percent){
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        return market.getDataForDraw(startDate, endDate, amount, percent);
    }
}
