package com.lanu.homebudget.security;

import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.entities.SubCategory;
import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.exceptions.UserAlreadyExistsException;
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

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(new Role((long) 1, "USER")));
        user.setActive(true);
        User theUser = userRepository.save(user);

        Category categoryTransfer = categoryService
                .createCategory(user, new Category(
                null, "Transfer", Transaction.TransactionType.TRANSFER, theUser));
        SubCategory subCategoryOut = subCategoryService
                .createSubCategory(categoryTransfer.getId(), new SubCategory(
                null, "Out", null));
        SubCategory subCategoryIn = subCategoryService
                .createSubCategory(categoryTransfer.getId(), new SubCategory(
                        null, "In", null));

        return theUser;
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
