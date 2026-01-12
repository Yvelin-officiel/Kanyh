package com.data.kanyh.mapper;

import com.data.kanyh.dto.CompteGuildeDTO;
import com.data.kanyh.model.CompteGuilde;
import org.springframework.stereotype.Component;

@Component
public class CompteGuildeMapper {

    /**
     * Convertit une entité {@link CompteGuilde} en {@link CompteGuildeDTO}.
     *
     * @param compteGuilde l'entité {@link CompteGuilde} à convertir
     * @return le {@link CompteGuildeDTO} correspondant
     */
    public CompteGuildeDTO toDTO(CompteGuilde compteGuilde) {
        CompteGuildeDTO dto = new CompteGuildeDTO();
        dto.setId(compteGuilde.getId());
        dto.setSoldeTotal(compteGuilde.getSoldeTotal());
        dto.setDateMiseAJour(compteGuilde.getDateMiseAJour());
        return dto;
    }
}
