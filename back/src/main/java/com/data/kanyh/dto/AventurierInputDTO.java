package com.data.kanyh.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AventurierInputDTO {

    @NotBlank
    private String nom;

    @NotBlank
    private String specialite;

    @NotBlank
    private Number tauxJournalierBase;

}
