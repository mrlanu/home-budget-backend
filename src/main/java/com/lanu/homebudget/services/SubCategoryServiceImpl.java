package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.SubCategory;
import com.lanu.homebudget.exceptions.ResourceNotFoundException;
import com.lanu.homebudget.repositories.SubCategoryRepository;
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
    public SubCategory createSubCategory(Long categoryId, SubCategory subCategory) {
        return categoryService.findById(categoryId).map(category -> {
            subCategory.setCategory(category);
            return subCategoryRepository.save(subCategory);
        }).orElseThrow(() -> new ResourceNotFoundException("CategoryId " + categoryId + " not found"));
    }

    @Override
    public List<SubCategory> findAllByCategory_Id(Long categoryId) {
        return subCategoryRepository.findAllByCategory_Id(categoryId);
    }
}
