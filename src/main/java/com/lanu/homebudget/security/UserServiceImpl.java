package com.lanu.homebudget.security;

import com.lanu.homebudget.entities.*;
import com.lanu.homebudget.exceptions.UserAlreadyExistsException;
import com.lanu.homebudget.repositories.BudgetRepository;
import com.lanu.homebudget.services.AccountService;
import com.lanu.homebudget.services.CategoryService;
import com.lanu.homebudget.services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private AccountService accountService;

    @Override
    public User createUser(User user){
        if (existByUsername(user.getUsername())){
            throw new UserAlreadyExistsException("User " + user.getUsername() + " already exists");
        }

        Budget budget = budgetRepository.save(new Budget(null, "Initial", user.getUsername(), null));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // user.setRoles(Arrays.asList(new Role((long) 1, "USER")));
        user.setActive(true);
        user.addBudget(budget);
        UserDetails userDetails = new UserDetails();
        user.setUserDetails(userDetails);
        User theUser = userRepository.save(user);

        createInitCategoriesAndAccounts(budget);

        return theUser;
    }

    private void createInitCategoriesAndAccounts(Budget budget){
        Category categoryTransfer = categoryService
                .createCategory(budget.getId(), new Category(
                        null, "Transfer", Transaction.TransactionType.TRANSFER, new ArrayList<>(), budget));
        SubCategory subCategoryOut = subCategoryService
                .createSubCategory(categoryTransfer.getId(), new SubCategory(
                        null, "Out", null));
        SubCategory subCategoryIn = subCategoryService
                .createSubCategory(categoryTransfer.getId(), new SubCategory(
                        null, "In", null));


        Category categoryFood = categoryService
        .createCategory(budget.getId(), new Category(null, "Food", Transaction.TransactionType.EXPENSE, new ArrayList<>(), budget));
        SubCategory subCategoryFoodRestaurant = subCategoryService
                .createSubCategory(categoryFood.getId(), new SubCategory(
                        null, "Restaurant", null));
        SubCategory subCategoryFoodCoffee = subCategoryService
                .createSubCategory(categoryFood.getId(), new SubCategory(
                        null, "Coffee", null));

        Category categoryFun = categoryService
                .createCategory(budget.getId(), new Category(null, "Fun", Transaction.TransactionType.EXPENSE, new ArrayList<>(), budget));
        SubCategory subCategoryFun = subCategoryService
                .createSubCategory(categoryFun.getId(), new SubCategory(
                        null, "Entertainment", null));

        Category categoryOther = categoryService
                .createCategory(budget.getId(), new Category(null, "Other", Transaction.TransactionType.EXPENSE, new ArrayList<>(), budget));
        SubCategory subCategoryOther = subCategoryService
                .createSubCategory(categoryOther.getId(), new SubCategory(
                        null, "Uber", null));

        Category categorySalary = categoryService
                .createCategory(budget.getId(), new Category(null, "Salary", Transaction.TransactionType.INCOME, new ArrayList<>(), budget));
        SubCategory subCategorySalaryJohn = subCategoryService
                .createSubCategory(categorySalary.getId(), new SubCategory(
                        null, "John", null));

        Category categoryInvestment = categoryService
                .createCategory(budget.getId(), new Category(null, "Investment", Transaction.TransactionType.INCOME, new ArrayList<>(), budget));
        SubCategory subCategorySalaryInvestmentStocks = subCategoryService
                .createSubCategory(categoryInvestment.getId(), new SubCategory(
                        null, "Dividend Stocks", null));

        Account initAccChase = accountService.createAccount(
                budget.getId(),
                new Account(null, "Chase", "Checking", "USD", 0.0, 0.0, true, budget)
        );

        Account initAccCash = accountService.createAccount(
                budget.getId(),
                new Account(null, "My wallet", "Cash", "USD", 0.0, 0.0, true, budget)
        );
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
