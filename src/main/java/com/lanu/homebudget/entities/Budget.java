package com.lanu.homebudget.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lanu.homebudget.security.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String ownerUsername;

    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "budget_user",
            joinColumns = @JoinColumn(name = "budget_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userList;
}
