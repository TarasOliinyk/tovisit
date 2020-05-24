package com.lits.tovisitapp.dto;

import com.lits.tovisitapp.model.Trip;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"password", "trips", "roles"})
@ToString(exclude = {"password", "trips"})
public class AccountDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private List<RoleDTO> roles = new ArrayList<>();

    private List<Trip> trips = new ArrayList<>();
}
