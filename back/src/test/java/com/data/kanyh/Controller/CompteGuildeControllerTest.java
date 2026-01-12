package com.data.kanyh.controller;

import com.data.kanyh.dto.CompteGuildeDTO;
import com.data.kanyh.dto.TransactionDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.service.CompteGuildeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompteGuildeController - Tests unitaires")
class CompteGuildeControllerTest {

    @Mock
    private CompteGuildeService compteGuildeService;

    @InjectMocks
    private CompteGuildeController compteGuildeController;

    private CompteGuildeDTO compteGuildeDTO;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
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
    @DisplayName("getCompteGuilde - Doit retourner le compte de la guilde avec HTTP 200")
    void getCompteGuilde_ShouldReturnCompteGuilde_WithHttp200() {
        when(compteGuildeService.getCompteGuilde()).thenReturn(compteGuildeDTO);

        ResponseEntity<CompteGuildeDTO> response = compteGuildeController.getCompteGuilde();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getSoldeTotal()).isEqualTo(1000.0);

        verify(compteGuildeService, times(1)).getCompteGuilde();
    }

    @Test
    @DisplayName("getCompteGuilde - Doit retourner un compte avec solde à zéro si nouvellement créé")
    void getCompteGuilde_ShouldReturnNewCompte_WithZeroBalance() {
        CompteGuildeDTO newCompteDTO = new CompteGuildeDTO();
        newCompteDTO.setId(1L);
        newCompteDTO.setSoldeTotal(0.0);
        newCompteDTO.setDateMiseAJour(LocalDateTime.now());

        when(compteGuildeService.getCompteGuilde()).thenReturn(newCompteDTO);

        ResponseEntity<CompteGuildeDTO> response = compteGuildeController.getCompteGuilde();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSoldeTotal()).isEqualTo(0.0);

        verify(compteGuildeService, times(1)).getCompteGuilde();
    }

    // ========== Tests pour effectuerTransaction() ==========

    @Test
    @DisplayName("effectuerTransaction - Doit retourner le compte mis à jour avec HTTP 200 pour ajout")
    void effectuerTransaction_ShouldReturnUpdatedCompte_WithHttp200_WhenAddingMoney() {
        CompteGuildeDTO updatedDTO = new CompteGuildeDTO();
        updatedDTO.setId(1L);
        updatedDTO.setSoldeTotal(1500.0);
        updatedDTO.setDateMiseAJour(LocalDateTime.now());

        TransactionDTO ajoutTransaction = new TransactionDTO();
        ajoutTransaction.setMontant(500.0);
        ajoutTransaction.setDescription("Récompense de quête");

        when(compteGuildeService.effectuerTransaction(ajoutTransaction)).thenReturn(updatedDTO);

        ResponseEntity<CompteGuildeDTO> response = compteGuildeController.effectuerTransaction(ajoutTransaction);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSoldeTotal()).isEqualTo(1500.0);

        verify(compteGuildeService, times(1)).effectuerTransaction(ajoutTransaction);
    }

    @Test
    @DisplayName("effectuerTransaction - Doit retourner le compte mis à jour avec HTTP 200 pour dépense")
    void effectuerTransaction_ShouldReturnUpdatedCompte_WithHttp200_WhenSpendingMoney() {
        CompteGuildeDTO updatedDTO = new CompteGuildeDTO();
        updatedDTO.setId(1L);
        updatedDTO.setSoldeTotal(700.0);
        updatedDTO.setDateMiseAJour(LocalDateTime.now());

        TransactionDTO depenseTransaction = new TransactionDTO();
        depenseTransaction.setMontant(-300.0);
        depenseTransaction.setDescription("Achat équipement");

        when(compteGuildeService.effectuerTransaction(depenseTransaction)).thenReturn(updatedDTO);

        ResponseEntity<CompteGuildeDTO> response = compteGuildeController.effectuerTransaction(depenseTransaction);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSoldeTotal()).isEqualTo(700.0);

        verify(compteGuildeService, times(1)).effectuerTransaction(depenseTransaction);
    }

    @Test
    @DisplayName("effectuerTransaction - Doit propager IllegalArgumentException du service")
    void effectuerTransaction_ShouldPropagateIllegalArgumentException_WhenBalanceWouldBeNegative() {
        TransactionDTO invalidTransaction = new TransactionDTO();
        invalidTransaction.setMontant(-2000.0);
        invalidTransaction.setDescription("Dépense excessive");

        when(compteGuildeService.effectuerTransaction(invalidTransaction))
                .thenThrow(new IllegalArgumentException("Le solde ne peut pas être négatif"));

        assertThatThrownBy(() -> compteGuildeController.effectuerTransaction(invalidTransaction))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Le solde ne peut pas être négatif");

        verify(compteGuildeService, times(1)).effectuerTransaction(invalidTransaction);
    }

    @Test
    @DisplayName("effectuerTransaction - Doit propager NotFoundException du service")
    void effectuerTransaction_ShouldPropagateNotFoundException_WhenCompteDoesNotExist() {
        when(compteGuildeService.effectuerTransaction(transactionDTO))
                .thenThrow(new NotFoundException("Compte guilde non trouvé"));

        assertThatThrownBy(() -> compteGuildeController.effectuerTransaction(transactionDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Compte guilde non trouvé");

        verify(compteGuildeService, times(1)).effectuerTransaction(transactionDTO);
    }

    @Test
    @DisplayName("effectuerTransaction - Doit accepter une transaction avec montant zéro")
    void effectuerTransaction_ShouldAcceptTransaction_WithZeroAmount() {
        TransactionDTO zeroTransaction = new TransactionDTO();
        zeroTransaction.setMontant(0.0);
        zeroTransaction.setDescription("Transaction test");

        when(compteGuildeService.effectuerTransaction(zeroTransaction)).thenReturn(compteGuildeDTO);

        ResponseEntity<CompteGuildeDTO> response = compteGuildeController.effectuerTransaction(zeroTransaction);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        verify(compteGuildeService, times(1)).effectuerTransaction(zeroTransaction);
    }

    @Test
    @DisplayName("effectuerTransaction - Doit gérer les montants avec décimales")
    void effectuerTransaction_ShouldHandleDecimalAmounts() {
        CompteGuildeDTO updatedDTO = new CompteGuildeDTO();
        updatedDTO.setId(1L);
        updatedDTO.setSoldeTotal(1123.45);
        updatedDTO.setDateMiseAJour(LocalDateTime.now());

        TransactionDTO decimalTransaction = new TransactionDTO();
        decimalTransaction.setMontant(123.45);
        decimalTransaction.setDescription("Transaction avec décimales");

        when(compteGuildeService.effectuerTransaction(decimalTransaction)).thenReturn(updatedDTO);

        ResponseEntity<CompteGuildeDTO> response = compteGuildeController.effectuerTransaction(decimalTransaction);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSoldeTotal()).isEqualTo(1123.45);

        verify(compteGuildeService, times(1)).effectuerTransaction(decimalTransaction);
    }
}
