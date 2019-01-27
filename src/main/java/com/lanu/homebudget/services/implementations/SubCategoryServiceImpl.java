package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.entities.SubCategory;
import com.lanu.homebudget.exceptions.ResourceNotFoundException;
import com.lanu.homebudget.repositories.SubCategoryRepository;
import com.lanu.homebudget.services.CategoryService;
import com.lanu.homebudget.services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Override
    public SubCategory createSubCategory(Long categoryId, SubCategory subCategory, Long budgetId) {
        return categoryService.findById(categoryId).map(category -> {
            subCategory.setCategory(category);
            subCategory.setBudgetId(budgetId);
            return subCategoryRepository.save(subCategory);
        }).orElseThrow(() -> new ResourceNotFoundException("CategoryId " + categoryId + " not found"));
    }

    @Override
    public List<SubCategory> findAllByCategory_Id(Long categoryId) {
        return subCategoryRepository.findAllByCategory_Id(categoryId);
    }

    @Override
    public SubCategory findByCategoryAndName(Category category, String name) {
        return subCategoryRepository.findByCategoryAndName(category, name);
    }
}
