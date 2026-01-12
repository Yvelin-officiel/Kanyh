package com.data.kanyh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompteGuildeDTO {
    private Long id;
    private Double soldeTotal;
    private LocalDateTime dateMiseAJour;
}
