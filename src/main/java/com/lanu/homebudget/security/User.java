package com.lanu.homebudget.security;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;
    private String password;

    @OneToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    private List<Role> roles;

    private boolean active;
}
