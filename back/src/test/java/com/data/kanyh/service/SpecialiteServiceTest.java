package com.data.kanyh.service;

import com.data.kanyh.model.Specialite;
import com.data.kanyh.repository.SpecialiteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialiteServiceTest {

    @Mock
    private SpecialiteRepository specialiteRepository;

    @InjectMocks
    private SpecialiteService specialiteService;

    @Test
    void addSpecialite_ShouldSaveAndReturnSpecialite_WhenValidSpecialiteProvided() {
        Specialite specialite = new Specialite();
        specialite.setNom("Guerrier");

        Specialite savedSpecialite = new Specialite();
        savedSpecialite.setId(1);
        savedSpecialite.setNom("Guerrier");

        when(specialiteRepository.save(specialite)).thenReturn(savedSpecialite);

        Specialite result = specialiteService.addSpecialite(specialite);

        assertEquals(savedSpecialite, result);
    }

    @Test
    void getAllSpecialites_ShouldReturnListOfSpecialites_WhenDataExists() {
        Specialite specialite1 = new Specialite();
        specialite1.setId(1);
        specialite1.setNom("Guerrier");

        Specialite specialite2 = new Specialite();
        specialite2.setId(2);
        specialite2.setNom("Dermatologie");

        List<Specialite> specialites = List.of(specialite1, specialite2);

        when(specialiteRepository.findAll()).thenReturn(specialites);

        List<Specialite> result = specialiteService.getAllSpecialites();

        assertEquals(specialites, result);
    }

    @Test
    void deleteSpecialite_ShouldCallRepositoryDelete_WhenIdExists() {
        Integer id = 1;

        doNothing().when(specialiteRepository).deleteById(id);

        specialiteService.deleteSpecialite(id);

        verify(specialiteRepository, times(1)).deleteById(id);
    }

    @Test
    void updateSpecialite_ShouldUpdateAndReturnSpecialite_WhenIdExists() {
        Integer id = 1;
        Specialite specialiteDetails = new Specialite();
        specialiteDetails.setNom("Guerrier Avancée");

        Specialite existingSpecialite = new Specialite();
        existingSpecialite.setId(id);
        existingSpecialite.setNom("Guerrier");

        Specialite updatedSpecialite = new Specialite();
        updatedSpecialite.setId(id);
        updatedSpecialite.setNom("Guerrier Avancée");

        when(specialiteRepository.findById(id)).thenReturn(Optional.of(existingSpecialite));
        when(specialiteRepository.save(existingSpecialite)).thenReturn(updatedSpecialite);

        Specialite result = specialiteService.updateSpecialite(id, specialiteDetails);

        assertEquals(updatedSpecialite, result);
    }

    @Test
    void updateSpecialite_ShouldThrowException_WhenIdDoesNotExist() {
        Integer id = 99;
        Specialite specialiteDetails = new Specialite();
        specialiteDetails.setNom("Guerrier Avancée");

        when(specialiteRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                specialiteService.updateSpecialite(id, specialiteDetails)
        );

        assertEquals("Specialite non trouvée avec l'id: " + id, exception.getMessage());
    }
}
