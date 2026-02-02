package com.data.kanyh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiquesDTO {

    private Double totalRevenus30Jours;
    private Double totalDepenses30Jours;
    private Double soldeMoyen30Jours;
    private Map<String, Double> repartitionParType;
    private Integer nombreTransactions30Jours;
}
