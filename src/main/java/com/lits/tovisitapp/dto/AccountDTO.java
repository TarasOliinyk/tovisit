package com.lits.tovisitapp.dto;

import com.lits.tovisitapp.model.Trip;
import lombok.*;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"password", "trips", "roles"})
@ToString(exclude = {"password", "trips"})
public class AccountDTO {

    private Long id;

    @NotBlank(message = "Account first name cannot be blank or null")
    private String firstName;

    @NotBlank(message = "Account last name cannot be blank or null")
    private String lastName;

    @NotBlank(message = "Account username cannot be blank or null")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,}$",
            message = "Account username has to be at least 3 characters long and can consist of letters and/or numbers")
    private String username;

    @NotBlank(message = "Account password cannot be blank or null")
    @Pattern(regexp = ".{6,}", message = "Account password has to be at least 6 characters long")
    private String password;

    private List<RoleDTO> roles = new ArrayList<>();

    private List<Trip> trips = new ArrayList<>();
}
