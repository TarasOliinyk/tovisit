package com.lits.tovisitapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.lits.tovisitapp.supplementary_data.AccountRole;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"password", "trips"})
@ToString(exclude = {"password", "trips"})
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
//	@Enumerated(value = EnumType.STRING)
//	@Builder.Default
//	private AccountRole role = AccountRole.USER;
	private String role;

	@JsonManagedReference
	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Trip> trips = new ArrayList<>();

}
