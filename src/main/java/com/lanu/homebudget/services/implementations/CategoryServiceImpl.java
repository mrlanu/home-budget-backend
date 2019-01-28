package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.entities.SubCategory;
import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.repositories.BudgetRepository;
import com.lanu.homebudget.repositories.CategoryRepository;
import com.lanu.homebudget.repositories.SubCategoryRepository;
import com.lanu.homebudget.services.CategoryService;
import com.lanu.homebudget.views.ListSubcategoryByCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Override
    public Category createCategory(Long budgetId, Category category) {
        category.setBudget(budgetRepository.findById(budgetId).get());
        return categoryRepository.save(category);
    }

    public List<Category> findCategoriesByBudgetId(Long budgetId) {
        return categoryRepository.findAllByBudget_Id(budgetId)
                .stream()
                .filter(category -> category.getType() != Transaction.TransactionType.TRANSFER)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category findByBudgetIdAndName(Long budgetId, String categoryName) {
        return categoryRepository.findByBudget_IdAndName(budgetId, categoryName);
    }
}
