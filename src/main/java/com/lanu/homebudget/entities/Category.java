package com.lanu.homebudget.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lanu.homebudget.security.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /*@OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private List<SubCategory> subCategoryList;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
