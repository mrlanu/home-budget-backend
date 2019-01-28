package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.entities.SubCategory;
import com.lanu.homebudget.views.ListSubcategoryByCategory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategoryService {

    Category createCategory(Long budgetId, Category category);
    List<Category> findCategoriesByBudgetId(Long budgetId);
    Optional<Category> findById(Long id);
    Category findByBudgetIdAndName(Long budgetId, String categoryName);
}
