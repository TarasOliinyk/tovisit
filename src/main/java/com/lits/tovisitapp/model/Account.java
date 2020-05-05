package com.lits.tovisitapp.model;

import com.lits.tovisitapp.supplementary_data.AccountRole;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = {"password", "trips"})
@EqualsAndHashCode(exclude = {"password", "trips"})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Account first name cannot be null")
    private String firstName;

    @Column(nullable = false)
    @NotNull(message = "Account last name cannot be null")
    private String lastName;

    @Column(nullable = false)
    @NotNull(message = "Account username cannot be null")
    @NotEmpty(message = "Account username cannot be empty")
    private String userName;

    @Column(nullable = false)
    @NotNull(message = "Account password cannot be null")
    private String password;

    @Column(nullable = false, columnDefinition = "varchar(32) default 'USER'")
    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "Account role cannot be null")
    private AccountRole role = AccountRole.USER;

    @OneToMany
    private List<Trip> trips = new ArrayList<>();

    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Account(String firstName, String lastName, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
    }
}
