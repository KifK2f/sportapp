package com.monsalon.sportapp.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Exemple : "ADMIN", "USER"

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }
}
