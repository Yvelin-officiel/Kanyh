package com.data.kanyh.mapper;

import com.data.kanyh.dto.CompteGuildeDTO;
import com.data.kanyh.model.CompteGuilde;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("CompteGuildeMapper - Tests unitaires")
class CompteGuildeMapperTest {

    private CompteGuildeMapper compteGuildeMapper;
    private CompteGuilde compteGuilde;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        compteGuildeMapper = new CompteGuildeMapper();
        now = LocalDateTime.now();

        compteGuilde = new CompteGuilde();
        compteGuilde.setId(1L);
        compteGuilde.setSoldeTotal(1000.0);
        compteGuilde.setDateMiseAJour(now);
    }

    // ========== Tests pour toDTO() ==========

    @Test
    @DisplayName("toDTO - Doit convertir une entité CompteGuilde en CompteGuildeDTO avec tous les champs")
    void toDTO_ShouldConvertEntityToDTO_WithAllFields() {
        CompteGuildeDTO dto = compteGuildeMapper.toDTO(compteGuilde);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getSoldeTotal()).isEqualTo(1000.0);
        assertThat(dto.getDateMiseAJour()).isCloseTo(now, within(1, ChronoUnit.SECONDS));
    }

    @Test
    @DisplayName("toDTO - Doit gérer un compte avec solde zéro")
    void toDTO_ShouldHandleZeroBalance() {
        compteGuilde.setSoldeTotal(0.0);

        CompteGuildeDTO dto = compteGuildeMapper.toDTO(compteGuilde);

        assertThat(dto).isNotNull();
        assertThat(dto.getSoldeTotal()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("toDTO - Doit gérer un compte avec solde avec décimales")
    void toDTO_ShouldHandleDecimalBalance() {
        compteGuilde.setSoldeTotal(1234.56);

        CompteGuildeDTO dto = compteGuildeMapper.toDTO(compteGuilde);

        assertThat(dto).isNotNull();
        assertThat(dto.getSoldeTotal()).isEqualTo(1234.56);
    }

    @Test
    @DisplayName("toDTO - Doit gérer un compte avec un grand solde")
    void toDTO_ShouldHandleLargeBalance() {
        compteGuilde.setSoldeTotal(999999.99);

        CompteGuildeDTO dto = compteGuildeMapper.toDTO(compteGuilde);

        assertThat(dto).isNotNull();
        assertThat(dto.getSoldeTotal()).isEqualTo(999999.99);
    }

    @Test
    @DisplayName("toDTO - Doit copier correctement la date de mise à jour")
    void toDTO_ShouldCopyDateMiseAJourCorrectly() {
        LocalDateTime specificDate = LocalDateTime.of(2026, 1, 12, 10, 30, 0);
        compteGuilde.setDateMiseAJour(specificDate);

        CompteGuildeDTO dto = compteGuildeMapper.toDTO(compteGuilde);

        assertThat(dto).isNotNull();
        assertThat(dto.getDateMiseAJour()).isEqualTo(specificDate);
    }

    @Test
    @DisplayName("toDTO - Doit mapper l'ID correctement")
    void toDTO_ShouldMapIdCorrectly() {
        compteGuilde.setId(999L);

        CompteGuildeDTO dto = compteGuildeMapper.toDTO(compteGuilde);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(999L);
    }
}
