package com.data.kanyh.Controller;

import com.data.kanyh.controller.SpecialiteController;
import com.data.kanyh.model.Specialite;
import com.data.kanyh.service.SpecialiteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialiteControllerTest {

    @Mock
    private SpecialiteService specialiteService;

    @InjectMocks
    private SpecialiteController specialiteController;

    @Test
    void addSpecialite_ShouldReturnSavedSpecialite_WhenValidRequest() {
        Specialite specialite = new Specialite();
        specialite.setNom("Guerrier");

        Specialite savedSpecialite = new Specialite();
        savedSpecialite.setId(1);
        savedSpecialite.setNom("Guerrier");

        when(specialiteService.addSpecialite(specialite)).thenReturn(savedSpecialite);

        ResponseEntity<Specialite> response = specialiteController.addSpecialite(specialite);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedSpecialite, response.getBody());
    }

    @Test
    void getAllSpecialites_ShouldReturnListOfSpecialites_WhenDataExists() {
        Specialite specialite1 = new Specialite();
        specialite1.setId(1);
        specialite1.setNom("Guerrier");

        Specialite specialite2 = new Specialite();
        specialite2.setId(2);
        specialite2.setNom("Archer");

        List<Specialite> specialites = List.of(specialite1, specialite2);

        when(specialiteService.getAllSpecialites()).thenReturn(specialites);

        ResponseEntity<List<Specialite>> response = specialiteController.getAllSpecialites();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(specialites, response.getBody());
    }

    @Test
    void deleteSpecialite_ShouldReturnNoContent_WhenSpecialiteExists() {
        Integer id = 1;

        doNothing().when(specialiteService).deleteSpecialite(id);

        ResponseEntity<Void> response = specialiteController.deleteSpecialite(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(specialiteService, times(1)).deleteSpecialite(id);
    }

    @Test
    void updateSpecialite_ShouldReturnUpdatedSpecialite_WhenValidRequest() {
        Integer id = 1;
        Specialite specialiteDetails = new Specialite();
        specialiteDetails.setNom("Guerrier Avancée");

        Specialite updatedSpecialite = new Specialite();
        updatedSpecialite.setId(id);
        updatedSpecialite.setNom("Guerrier Avancée");

        when(specialiteService.updateSpecialite(id, specialiteDetails)).thenReturn(updatedSpecialite);

        ResponseEntity<Specialite> response = specialiteController.updateSpecialite(id, specialiteDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSpecialite, response.getBody());
    }
}
