package com.lanu.homebudget.security;

import java.util.Optional;

public interface UserService {

    User save(User user);

    boolean existByUsername(String username);

    Optional<User> findByUserId(Long id);

    Optional<User> findByUsername(String userName);
}
