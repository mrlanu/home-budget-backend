package com.lanu.homebudget.repositories;

import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.entities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    List<SubCategory> findAllByCategory_Id(Long categoryId);
    SubCategory findByCategoryAndName(Category category, String name);
    List<SubCategory> findAllByBudgetId(Long budgetId);
}
