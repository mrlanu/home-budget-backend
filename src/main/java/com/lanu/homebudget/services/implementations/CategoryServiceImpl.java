package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Category;
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

    @Override
    public Category createCategory(User user, Category category) {
        category.setUser(user);
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findCategoriesByUser(User user) {
        return categoryRepository.findAllByUser(user);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category findByUserAndName(User user, String categoryName) {
        return categoryRepository.findByUserAndName(user, categoryName);
    }
}
