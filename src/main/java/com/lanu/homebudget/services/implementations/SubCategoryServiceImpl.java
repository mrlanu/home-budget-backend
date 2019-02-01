package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.entities.SubCategory;
import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.exceptions.ResourceNotFoundException;
import com.lanu.homebudget.repositories.SubCategoryRepository;
import com.lanu.homebudget.repositories.TransactionRepository;
import com.lanu.homebudget.services.CategoryService;
import com.lanu.homebudget.services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public SubCategory createSubCategory(Long categoryId, SubCategory subCategory) {
        return categoryService.findById(categoryId).map(category -> {
            subCategory.setCategory(category);
            return subCategoryRepository.save(subCategory);
        }).orElseThrow(() -> new ResourceNotFoundException("CategoryId " + categoryId + " not found"));
    }

    @Override
    public ResponseEntity<?> deleteSubCategory(Long subCategoryId) {
        if (hasSubCategoryAnyTransactions(subCategoryId)){
            return ResponseEntity.unprocessableEntity().build();
        }
        return subCategoryRepository.findById(subCategoryId).map(subCategory -> {
            subCategoryRepository.delete(subCategory);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("SubCategoryId " + subCategoryId + " not found"));
    }

    private boolean hasSubCategoryAnyTransactions(Long subCategoryId){
        return transactionRepository.findFirstBySubCategory_Id(subCategoryId) != null;
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
