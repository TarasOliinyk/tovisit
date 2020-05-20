package com.lits.tovisitapp.model;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Long accountId;

    @OneToMany(fetch = FetchType.EAGER ,mappedBy = "roleId")
    @Fetch(FetchMode.SELECT)
    private List<Permission> permissions = new ArrayList<>();
}