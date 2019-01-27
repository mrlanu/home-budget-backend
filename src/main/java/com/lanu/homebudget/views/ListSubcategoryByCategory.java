package com.lanu.homebudget.views;

import com.lanu.homebudget.entities.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListSubcategoryByCategory {
    private Long id;
    private String name;
    private List<SubCategory> subCategoryList;
}
