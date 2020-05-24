package com.lits.tovisitapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"accounts", "permissions"})
@ToString(exclude = {"accounts", "permissions"})
public class RoleDTO {

    private Integer id;

    private String name;

    @JsonIgnore
    private List<AccountDTO> accounts = new ArrayList<>();

    private List<PermissionDTO> permissions = new ArrayList<>();
}
