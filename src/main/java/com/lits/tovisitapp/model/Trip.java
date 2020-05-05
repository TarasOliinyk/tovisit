package com.lits.tovisitapp.model;

import com.lits.tovisitapp.utils.LocalDatePersistenceConverter;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"accountId", "createdAt", "updatedAt"})
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false)
    @NotNull(message = "Trip name cannot be null")
    @NotEmpty(message = "Trip name cannot be empty")
    private String name;

    @Column(nullable = false)
    @CreationTimestamp
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate updatedAt;

    @OneToMany
    private List<Place> places = new ArrayList<>();

    public Trip(String name) {
        this.name = name;
    }
}
