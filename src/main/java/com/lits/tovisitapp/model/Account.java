package com.lits.tovisitapp.model;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@EqualsAndHashCode(exclude = {"password", "trips"})
@ToString(exclude = {"password", "trips"})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String username;
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "accountId")
    @Fetch(FetchMode.SELECT)
    public List<Role> role = new ArrayList<>();

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trip> trips = new ArrayList<>();

}
