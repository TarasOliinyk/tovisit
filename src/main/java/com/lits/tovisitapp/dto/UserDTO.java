package com.lits.tovisitapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lits.tovisitapp.model.Trip;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"id", "password", "trips"})
@ToString(exclude = {"password", "trips"})
public class UserDTO {

    private Long id;

    @NotBlank(message = "User first name cannot be blank or null")
    private String firstName;

    @NotBlank(message = "User last name cannot be blank or null")
    private String lastName;

    @NotBlank(message = "User username cannot be blank or null")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,}$",
            message = "User username has to be at least 3 characters long and can consist of letters and/or numbers")
    private String username;

    @NotBlank(message = "User password cannot be blank or null")
    @Pattern(regexp = ".{6,}", message = "User password has to be at least 6 characters long")
    private String password;

    @JsonIgnore
    private List<Trip> trips = new ArrayList<>();
}
