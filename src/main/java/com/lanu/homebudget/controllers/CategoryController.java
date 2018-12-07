package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.security.UserService;
import com.lanu.homebudget.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories")
    public Category createCategory(Principal principal, @Valid @RequestBody Category category){
        User user = userService.findByUsername(principal.getName()).get();
        return categoryService.createCategory(user, category);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories(Principal principal){
        User user = userService.findByUsername(principal.getName()).get();
        return categoryService.findCategoriesByUser(user);
    }
}
