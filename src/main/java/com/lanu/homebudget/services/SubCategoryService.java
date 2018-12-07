package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.SubCategory;

import java.util.List;

public interface SubCategoryService {

    SubCategory createSubCategory(Long categoryId, SubCategory subCategory);

    List<SubCategory> findAllByCategory_Id(Long categoryId);
}
