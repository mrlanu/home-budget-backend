package com.lanu.homebudget.security;

import com.lanu.homebudget.entities.Budget;
import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.entities.SubCategory;
import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.exceptions.UserAlreadyExistsException;
import com.lanu.homebudget.repositories.BudgetRepository;
import com.lanu.homebudget.services.CategoryService;
import com.lanu.homebudget.services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Override
    public User createUser(User user){
        if (existByUsername(user.getUsername())){
            throw new UserAlreadyExistsException("User " + user.getUsername() + " already exists");
        }

        Budget budget = budgetRepository.save(new Budget(null, "Initial", null));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(new Role((long) 1, "USER")));
        user.setActive(true);
        user.addBudget(budget);
        User theUser = userRepository.save(user);

        Category categoryTransfer = categoryService
                .createCategory(budget.getId(), new Category(
                null, "Transfer", Transaction.TransactionType.TRANSFER, budget));
        SubCategory subCategoryOut = subCategoryService
                .createSubCategory(categoryTransfer.getId(), new SubCategory(
                null, "Out", null));
        SubCategory subCategoryIn = subCategoryService
                .createSubCategory(categoryTransfer.getId(), new SubCategory(
                        null, "In", null));

        return theUser;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUserId(Long id) {
        return userRepository.findByUserId(id);
    }

    @Override
    public Optional<User> findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }


    @Override
    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
