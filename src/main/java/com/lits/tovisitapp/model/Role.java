package com.lits.tovisitapp.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"accounts", "permissions"})
@ToString(exclude = {"accounts", "permissions"})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<Account> accounts = new ArrayList<>();

    @ManyToMany
    private List<Permission> permissions = new ArrayList<>();
}
