package com.lanu.homebudget.security;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    /*@Autowired
    private PasswordEncoder passwordEncoder;*/

    @Override
    public User save(User user){
        /*user.setPassword(passwordEncoder.encode(user.getPassword()));*/
        user.setPassword("123");
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUserId(Long id) {
        return userRepository.findByUserId(id);
    }

    @Override
    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
