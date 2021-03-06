package com.lanu.homebudget.views;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Group {
    private Long id;
    private String name;
    private List<GroupSubcategory> groupSubcategoryList;
    private double spent;
}
