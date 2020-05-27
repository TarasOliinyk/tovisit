package com.lits.tovisitapp.model;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"user", "places", "createdAt", "updatedAt"})
@ToString(exclude = {"user", "places"})
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
	private User user;

	@OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Place> places = new ArrayList<>();

	public void setUser(User user) {
		this.user = user;
		this.accountId = (user != null && user.getId() != null) ? user.getId() : null;
	}
}
