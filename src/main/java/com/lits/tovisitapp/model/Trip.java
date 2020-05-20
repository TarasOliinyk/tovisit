package com.lits.tovisitapp.model;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"account", "places", "createdAt", "updatedAt"})
@ToString(exclude = {"account", "places"})
public class Trip {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(nullable = false)
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Column(name = "account_id", insertable = false, updatable = false/*, nullable = false*/)
	private Long accountId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Account account;

	@OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Place> places = new ArrayList<>();

	public void setAccount(Account account) {
		this.account = account;
		this.accountId = (account != null && account.getId() != null) ? account.getId() : null;
	}
}
