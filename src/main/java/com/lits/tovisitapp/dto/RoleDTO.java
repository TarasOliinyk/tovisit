package com.lits.tovisitapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"accounts", "permissions"})
@ToString(exclude = {"accounts", "permissions"})
public class RoleDTO {

    private Integer id;

    @NotBlank(message = "Role name cannot be blank or null")
    private String name;

    @JsonIgnore
    private List<AccountDTO> accounts = new ArrayList<>();

    private List<PermissionDTO> permissions = new ArrayList<>();
}
