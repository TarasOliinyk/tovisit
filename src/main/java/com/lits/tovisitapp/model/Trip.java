package com.lits.tovisitapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table
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
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	@Column(nullable = false)
	@UpdateTimestamp
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

//	@Column(name = "account_id", insertable = false, updatable = false, nullable = false)
//	private Long accountId;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
//	@JoinColumn(name="account_id", nullable = false)
	private Account account;

	@JsonManagedReference
	@OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Place> places = new ArrayList<>();

//	public void setAccount(Account account) {
//		this.account = account;
//		this.accountId = (account != null && account.getId() != null) ? account.getId() : null;
//	}

}
