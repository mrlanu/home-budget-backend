package com.lanu.homebudget.security;

import com.lanu.homebudget.exceptions.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;

@RestController
public class SecurityController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public User registerUser(@Valid @RequestBody User user) {
        if (userService.existByUsername(user.getUsername())){
            throw new UserAlreadyExistsException("User " + user.getUsername() + " already exists");
        }
        user.setRoles(Arrays.asList(new Role((long) 1, "USER")));
        user.setActive(true);
        return userService.save(user);
    }

    @GetMapping("/user")
    public User getLoggedInUser(Principal principal){
        return userService.findByUsername(principal.getName()).get();
    }
}
