package com.data.kanyh.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class QueteInputDTO {
    @NotBlank
    private String nom;

    @NotBlank
    private String description;

    @NotNull
    private Double prime;

    @NotNull
    private Integer dureeEstimee;

    @NotNull
    private LocalDate datePeremption;
}
