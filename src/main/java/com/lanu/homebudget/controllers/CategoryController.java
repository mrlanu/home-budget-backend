package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @PutMapping("/categories")
    public Category updateCategory(@Valid @RequestBody Category categoryRequest) {
        return categoryService.editCategory(categoryRequest);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable (value = "categoryId") Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}
