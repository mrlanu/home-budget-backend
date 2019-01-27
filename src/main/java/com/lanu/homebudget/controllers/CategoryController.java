package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.entities.SubCategory;
import com.lanu.homebudget.services.CategoryService;
import com.lanu.homebudget.views.ListSubcategoryByCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories")
    public Category createCategory(@RequestParam(name = "budgetId") Long budgetId, @Valid @RequestBody Category category){
        return categoryService.createCategory(budgetId, category);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories(@RequestParam(name = "budgetId") Long budgetId){
        return categoryService.findCategoriesByBudgetId(budgetId);
    }

    @GetMapping("/categories/getGroups")
    public List<ListSubcategoryByCategory> getGroupedSubCategoryByCategory(
            @RequestParam(name = "budgetId") Long budgetId){
        return categoryService.getGroupedSubCategoryByCategory(budgetId);
    }
}
