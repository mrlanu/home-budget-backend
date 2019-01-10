package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.repositories.BudgetRepository;
import com.lanu.homebudget.repositories.CategoryRepository;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Override
    public Category createCategory(Long budgetId, Category category) {
        category.setBudget(budgetRepository.findById(budgetId).get());
        return categoryRepository.save(category);
    }

    public List<Category> findCategoriesByBudgetId(Long budgetId) {
        return categoryRepository.findAllByBudget_Id(budgetId);
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
