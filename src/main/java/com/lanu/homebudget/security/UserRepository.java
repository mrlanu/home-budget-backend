package com.lanu.homebudget.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String userName);

    User findByUserId(Long id);

    boolean existsByUsername(String username);
}
