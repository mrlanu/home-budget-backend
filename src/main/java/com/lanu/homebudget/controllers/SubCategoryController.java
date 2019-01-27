package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.SubCategory;
import com.lanu.homebudget.services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @PostMapping("/categories/{categoryId}/subcategories")
    public SubCategory createSubCategory(@PathVariable(value = "categoryId") Long categoryId,
                                         @RequestParam(name = "budgetId") Long budgetId,
                                         @Valid @RequestBody SubCategory subCategory){
        return subCategoryService.createSubCategory(categoryId, subCategory, budgetId);
    }

    @GetMapping("/categories/{categoryId}/subcategories")
    public List<SubCategory> getAllSubCategoriesByCategoryId(@PathVariable(value = "categoryId") Long categoryId){
        return subCategoryService.findAllByCategory_Id(categoryId);
    }
}
