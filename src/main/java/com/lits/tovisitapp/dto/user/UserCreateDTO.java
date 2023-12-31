package com.lits.tovisitapp.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;

@Data
@EqualsAndHashCode(exclude = "password")
@ToString(exclude = "password")
public class UserCreateDTO {

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
