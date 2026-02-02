package com.data.kanyh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeTransaction type;

    @Column(nullable = false)
    private Double montant;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateTransaction;

    private Long queteId;
    private Long aventurierId;
    private Long equipementId;

    @Column(nullable = false)
    private String initiePar;

    @Column(nullable = false)
    private Double soldeApres;

    @PrePersist
    protected void onCreate() {
        if (dateTransaction == null) {
            dateTransaction = LocalDateTime.now();
        }
    }
}
