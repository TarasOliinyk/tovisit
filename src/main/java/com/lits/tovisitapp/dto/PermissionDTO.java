package com.lits.tovisitapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lits.tovisitapp.model.Role;
import lombok.*;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "roles")
@ToString(exclude = "roles")
public class PermissionDTO {

    private Long id;

    @NotBlank(message = "Permission name cannot be blank or null")
    private String name;

    @JsonIgnore
    private List<Role> roles = new ArrayList<>();
}
