package com.lanu.homebudget.repositories;

import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    /*List<Category> findAllByUser(User user);
    Category findByUserAndName(User user, String name);*/

    List<Category> findAllByBudget_Id(Long budgetId);
    Category findByBudget_IdAndName(Long budgetId, String name);
}
