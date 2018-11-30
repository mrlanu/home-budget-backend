package com.lanu.homebudget.security;

public interface UserService {

    User save(User user);

    boolean existByUsername(String username);
}
