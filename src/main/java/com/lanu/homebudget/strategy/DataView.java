package com.lanu.homebudget.strategy;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DataView {
    private List<LocalDate> x;
    private List<BigDecimal> open;
    private List<BigDecimal> close;
    private List<BigDecimal> high;
    private List<BigDecimal> low;
    private List<AnnotationView> annotations;

    public DataView() {
        this.x = new ArrayList<>();
        this.open = new ArrayList<>();
        this.close = new ArrayList<>();
        this.high = new ArrayList<>();
        this.low = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }

    public void putX(LocalDate x){
        this.x.add(x);
    }

    public void putOpen(BigDecimal open){
        this.open.add(open);
    }

    public void putClose(BigDecimal close){
        this.close.add(close);
    }

    public void putHigh(BigDecimal high){
        this.high.add(high);
    }

    public void putLow(BigDecimal low){
        this.low.add(low);
    }

    public void putAnnotation(LocalDate date, double y, String text){
        annotations.add(new AnnotationView(date, y, text));
    }
}
