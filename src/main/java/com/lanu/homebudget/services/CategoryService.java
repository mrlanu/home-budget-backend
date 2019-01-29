package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category createCategory(Long budgetId, Category category);
    Category editCategory(Category category);
    ResponseEntity<?> deleteCategory(Long categoryId);
    List<Category> findCategoriesByBudgetId(Long budgetId);
    Optional<Category> findById(Long id);
    Category findByBudgetIdAndName(Long budgetId, String categoryName);
}
