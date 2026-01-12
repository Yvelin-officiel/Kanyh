package com.data.kanyh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "compte_guilde")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompteGuilde {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double soldeTotal;

    @Column(nullable = false)
    private LocalDateTime dateMiseAJour;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.dateMiseAJour = LocalDateTime.now();
    }
}
