package com.lanu.homebudget.strategy;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AnnotationView {
    private LocalDate x;
    private double y;
    private String xref;
    private String yref;
    private String text;
    private Font font;
    private boolean showarrow;
    private String xanchor;
    private int ax;
    private int ay;

    public AnnotationView(LocalDate x, double y, String text) {
        this.x = x;
        this.y = y;
        this.xref = "x";
        this.yref = "paper";
        this.text = text;
        this.font = new Font("magenta");
        this.showarrow = true;
        this.xanchor = "right";
        this.ax = -20;
        this.ay = 0;
    }

    @Data
    @NoArgsConstructor
    private static class Font {
        private String color;
        public Font(String color) {
            this.color = color;
        }
    }
}
