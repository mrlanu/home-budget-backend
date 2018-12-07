package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.security.User;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category createCategory(User user, Category category);
    List<Category> findCategoriesByUser(User user);
    Optional<Category> findById(Long id);
}
