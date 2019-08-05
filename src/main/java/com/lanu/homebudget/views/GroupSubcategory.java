package com.lanu.homebudget.views;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupSubcategory {
    private Long id;
    private String name;
    private double spent;
}
