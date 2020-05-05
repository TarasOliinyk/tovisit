package com.lits.tovisitapp.dto;

import com.lits.tovisitapp.model.Trip;
import com.lits.tovisitapp.supplementary_data.AccountRole;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccountDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private AccountRole role = AccountRole.USER;
    private List<Trip> trips = new ArrayList<>();
}
