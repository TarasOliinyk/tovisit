package com.lits.tovisitapp.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Data
@NoArgsConstructor
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Place type name cannot be null")
    @NotEmpty(message = "Place type name cannot be empty")
    private String name;
}
