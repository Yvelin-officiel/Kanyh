package com.data.kanyh.service;

import com.data.kanyh.dto.CompteGuildeDTO;
import com.data.kanyh.dto.TransactionDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.CompteGuildeMapper;
import com.data.kanyh.model.CompteGuilde;
import com.data.kanyh.repository.CompteGuildeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompteGuildeService - Tests unitaires")
class CompteGuildeServiceTest {

    @Mock
    private CompteGuildeRepository compteGuildeRepository;

    @Mock
    private CompteGuildeMapper compteGuildeMapper;

    @InjectMocks
    private CompteGuildeService compteGuildeService;

    private CompteGuilde compteGuilde;
    private CompteGuildeDTO compteGuildeDTO;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        compteGuilde = new CompteGuilde();
        compteGuilde.setId(1L);
        compteGuilde.setSoldeTotal(1000.0);
        compteGuilde.setDateMiseAJour(LocalDateTime.now());

        compteGuildeDTO = new CompteGuildeDTO();
        compteGuildeDTO.setId(1L);
        compteGuildeDTO.setSoldeTotal(1000.0);
        compteGuildeDTO.setDateMiseAJour(LocalDateTime.now());

        transactionDTO = new TransactionDTO();
        transactionDTO.setMontant(500.0);
        transactionDTO.setDescription("Test transaction");
    }

    // ========== Tests pour getCompteGuilde() ==========

    @Test
    @DisplayName("getCompteGuilde - Doit retourner le compte existant")
    void getCompteGuilde_ShouldReturnExistingCompte() {
        when(compteGuildeRepository.findById(1L)).thenReturn(Optional.of(compteGuilde));
        when(compteGuildeMapper.toDTO(compteGuilde)).thenReturn(compteGuildeDTO);

        CompteGuildeDTO result = compteGuildeService.getCompteGuilde();

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getSoldeTotal()).isEqualTo(1000.0);

        verify(compteGuildeRepository, times(1)).findById(1L);
        verify(compteGuildeMapper, times(1)).toDTO(compteGuilde);
        verify(compteGuildeRepository, never()).save(any());
    }

    @Test
    @DisplayName("getCompteGuilde - Doit créer un nouveau compte s'il n'existe pas")
    void getCompteGuilde_ShouldCreateNewCompte_WhenNotExists() {
        CompteGuilde nouveauCompte = new CompteGuilde();
        nouveauCompte.setId(1L);
        nouveauCompte.setSoldeTotal(0.0);
        nouveauCompte.setDateMiseAJour(LocalDateTime.now());

        CompteGuildeDTO nouveauCompteDTO = new CompteGuildeDTO();
        nouveauCompteDTO.setId(1L);
        nouveauCompteDTO.setSoldeTotal(0.0);
        nouveauCompteDTO.setDateMiseAJour(LocalDateTime.now());

        when(compteGuildeRepository.findById(1L)).thenReturn(Optional.empty());
        when(compteGuildeRepository.save(any(CompteGuilde.class))).thenReturn(nouveauCompte);
        when(compteGuildeMapper.toDTO(nouveauCompte)).thenReturn(nouveauCompteDTO);

        CompteGuildeDTO result = compteGuildeService.getCompteGuilde();

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getSoldeTotal()).isEqualTo(0.0);

        verify(compteGuildeRepository, times(1)).findById(1L);
        verify(compteGuildeRepository, times(1)).save(any(CompteGuilde.class));
        verify(compteGuildeMapper, times(1)).toDTO(nouveauCompte);
    }

    // ========== Tests pour effectuerTransaction() ==========

    @Test
    @DisplayName("effectuerTransaction - Doit ajouter de l'argent avec un montant positif")
    void effectuerTransaction_ShouldAddMoney_WhenPositiveAmount() {
        TransactionDTO ajoutTransaction = new TransactionDTO();
        ajoutTransaction.setMontant(500.0);
        ajoutTransaction.setDescription("Récompense de quête");

        CompteGuilde updatedCompte = new CompteGuilde();
        updatedCompte.setId(1L);
        updatedCompte.setSoldeTotal(1500.0);
        updatedCompte.setDateMiseAJour(LocalDateTime.now());

        CompteGuildeDTO updatedDTO = new CompteGuildeDTO();
        updatedDTO.setId(1L);
        updatedDTO.setSoldeTotal(1500.0);
        updatedDTO.setDateMiseAJour(LocalDateTime.now());

        when(compteGuildeRepository.findById(1L)).thenReturn(Optional.of(compteGuilde));
        when(compteGuildeRepository.save(any(CompteGuilde.class))).thenReturn(updatedCompte);
        when(compteGuildeMapper.toDTO(updatedCompte)).thenReturn(updatedDTO);

        CompteGuildeDTO result = compteGuildeService.effectuerTransaction(ajoutTransaction);

        assertThat(result).isNotNull();
        assertThat(result.getSoldeTotal()).isEqualTo(1500.0);

        verify(compteGuildeRepository, times(1)).findById(1L);
        verify(compteGuildeRepository, times(1)).save(any(CompteGuilde.class));
        verify(compteGuildeMapper, times(1)).toDTO(updatedCompte);
    }

    @Test
    @DisplayName("effectuerTransaction - Doit retirer de l'argent avec un montant négatif")
    void effectuerTransaction_ShouldRemoveMoney_WhenNegativeAmount() {
        TransactionDTO depenseTransaction = new TransactionDTO();
        depenseTransaction.setMontant(-300.0);
        depenseTransaction.setDescription("Achat équipement");

        CompteGuilde updatedCompte = new CompteGuilde();
        updatedCompte.setId(1L);
        updatedCompte.setSoldeTotal(700.0);
        updatedCompte.setDateMiseAJour(LocalDateTime.now());

        CompteGuildeDTO updatedDTO = new CompteGuildeDTO();
        updatedDTO.setId(1L);
        updatedDTO.setSoldeTotal(700.0);
        updatedDTO.setDateMiseAJour(LocalDateTime.now());

        when(compteGuildeRepository.findById(1L)).thenReturn(Optional.of(compteGuilde));
        when(compteGuildeRepository.save(any(CompteGuilde.class))).thenReturn(updatedCompte);
        when(compteGuildeMapper.toDTO(updatedCompte)).thenReturn(updatedDTO);

        CompteGuildeDTO result = compteGuildeService.effectuerTransaction(depenseTransaction);

        assertThat(result).isNotNull();
        assertThat(result.getSoldeTotal()).isEqualTo(700.0);

        verify(compteGuildeRepository, times(1)).findById(1L);
        verify(compteGuildeRepository, times(1)).save(any(CompteGuilde.class));
        verify(compteGuildeMapper, times(1)).toDTO(updatedCompte);
    }

    @Test
    @DisplayName("effectuerTransaction - Doit lancer IllegalArgumentException si solde devient négatif")
    void effectuerTransaction_ShouldThrowException_WhenResultingBalanceIsNegative() {
        TransactionDTO invalidTransaction = new TransactionDTO();
        invalidTransaction.setMontant(-1500.0);
        invalidTransaction.setDescription("Dépense excessive");

        when(compteGuildeRepository.findById(1L)).thenReturn(Optional.of(compteGuilde));

        assertThatThrownBy(() -> compteGuildeService.effectuerTransaction(invalidTransaction))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Le solde ne peut pas être négatif");

        verify(compteGuildeRepository, times(1)).findById(1L);
        verify(compteGuildeRepository, never()).save(any());
        verify(compteGuildeMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("effectuerTransaction - Doit lancer NotFoundException si compte n'existe pas")
    void effectuerTransaction_ShouldThrowNotFoundException_WhenCompteDoesNotExist() {
        when(compteGuildeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> compteGuildeService.effectuerTransaction(transactionDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Compte guilde non trouvé");

        verify(compteGuildeRepository, times(1)).findById(1L);
        verify(compteGuildeRepository, never()).save(any());
        verify(compteGuildeMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("effectuerTransaction - Doit accepter une transaction qui ramène le solde à zéro")
    void effectuerTransaction_ShouldAcceptTransaction_WhenResultingBalanceIsZero() {
        TransactionDTO zeroTransaction = new TransactionDTO();
        zeroTransaction.setMontant(-1000.0);
        zeroTransaction.setDescription("Dépense totale");

        CompteGuilde updatedCompte = new CompteGuilde();
        updatedCompte.setId(1L);
        updatedCompte.setSoldeTotal(0.0);
        updatedCompte.setDateMiseAJour(LocalDateTime.now());

        CompteGuildeDTO updatedDTO = new CompteGuildeDTO();
        updatedDTO.setId(1L);
        updatedDTO.setSoldeTotal(0.0);
        updatedDTO.setDateMiseAJour(LocalDateTime.now());

        when(compteGuildeRepository.findById(1L)).thenReturn(Optional.of(compteGuilde));
        when(compteGuildeRepository.save(any(CompteGuilde.class))).thenReturn(updatedCompte);
        when(compteGuildeMapper.toDTO(updatedCompte)).thenReturn(updatedDTO);

        CompteGuildeDTO result = compteGuildeService.effectuerTransaction(zeroTransaction);

        assertThat(result).isNotNull();
        assertThat(result.getSoldeTotal()).isEqualTo(0.0);

        verify(compteGuildeRepository, times(1)).findById(1L);
        verify(compteGuildeRepository, times(1)).save(any(CompteGuilde.class));
        verify(compteGuildeMapper, times(1)).toDTO(updatedCompte);
    }

    @Test
    @DisplayName("effectuerTransaction - Doit gérer les montants avec décimales")
    void effectuerTransaction_ShouldHandleDecimalAmounts() {
        TransactionDTO decimalTransaction = new TransactionDTO();
        decimalTransaction.setMontant(123.45);
        decimalTransaction.setDescription("Transaction avec décimales");

        CompteGuilde updatedCompte = new CompteGuilde();
        updatedCompte.setId(1L);
        updatedCompte.setSoldeTotal(1123.45);
        updatedCompte.setDateMiseAJour(LocalDateTime.now());

        CompteGuildeDTO updatedDTO = new CompteGuildeDTO();
        updatedDTO.setId(1L);
        updatedDTO.setSoldeTotal(1123.45);
        updatedDTO.setDateMiseAJour(LocalDateTime.now());

        when(compteGuildeRepository.findById(1L)).thenReturn(Optional.of(compteGuilde));
        when(compteGuildeRepository.save(any(CompteGuilde.class))).thenReturn(updatedCompte);
        when(compteGuildeMapper.toDTO(updatedCompte)).thenReturn(updatedDTO);

        CompteGuildeDTO result = compteGuildeService.effectuerTransaction(decimalTransaction);

        assertThat(result).isNotNull();
        assertThat(result.getSoldeTotal()).isEqualTo(1123.45);

        verify(compteGuildeRepository, times(1)).findById(1L);
        verify(compteGuildeRepository, times(1)).save(any(CompteGuilde.class));
        verify(compteGuildeMapper, times(1)).toDTO(updatedCompte);
    }
}
