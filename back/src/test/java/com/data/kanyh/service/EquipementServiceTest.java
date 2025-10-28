package com.data.kanyh.service;

import com.data.kanyh.dto.EquipementDTO;
import com.data.kanyh.dto.EquipementInputDTO;
import com.data.kanyh.dto.EquipementUpdateDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.EquipementMapper;
import com.data.kanyh.model.Equipement;
import com.data.kanyh.repository.EquipementRepository;
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
public class EquipementServiceTest {

    @Mock
    private EquipementRepository equipementRepository;

    @Mock
    private EquipementMapper equipementMapper;

    @InjectMocks
    private EquipementService equipementService;

    @Test
    void save_ShouldConvertInputAndReturnDTO() {
        EquipementInputDTO input = new EquipementInputDTO();
        Equipement entity = new Equipement();
        Equipement savedEntity = new Equipement();
        EquipementDTO expectedDTO = new EquipementDTO();

        when(equipementMapper.toEntity(input)).thenReturn(entity);
        when(equipementRepository.save(entity)).thenReturn(savedEntity);
        when(equipementMapper.toDTO(savedEntity)).thenReturn(expectedDTO);

        EquipementDTO result = equipementService.save(input);

        assertEquals(expectedDTO, result);
        verify(equipementMapper).toEntity(input);
        verify(equipementRepository).save(entity);
    }

    @Test
    void getAllEquipement_ShouldReturnListOfDTOs() {
        Equipement e1 = new Equipement();
        Equipement e2 = new Equipement();
        EquipementDTO d1 = new EquipementDTO();
        EquipementDTO d2 = new EquipementDTO();

        List<Equipement> entities = List.of(e1, e2);

        when(equipementRepository.findAll()).thenReturn(entities);
        when(equipementMapper.toDTO(e1)).thenReturn(d1);
        when(equipementMapper.toDTO(e2)).thenReturn(d2);

        List<EquipementDTO> result = equipementService.getAllEquipement();

        assertEquals(2, result.size());
        assertEquals(d1, result.get(0));
        assertEquals(d2, result.get(1));
        verify(equipementRepository).findAll();
    }

    @Test
    void delete_ShouldCallRepositoryDelete_WhenIdExists() {
        Long id = 1L;

        when(equipementRepository.existsById(id)).thenReturn(true);
        doNothing().when(equipementRepository).deleteById(id);

        equipementService.delete(id);

        verify(equipementRepository).existsById(id);
        verify(equipementRepository).deleteById(id);
    }

    @Test
    void delete_ShouldThrowNotFoundException_WhenIdDoesNotExist() {
        Long id = 2L;

        when(equipementRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> equipementService.delete(id));
        verify(equipementRepository).existsById(id);
    }

    @Test
    void update_ShouldUpdateAndReturnDTO_WhenEntityExists() {
        Long id = 1L;
        EquipementUpdateDTO dto = new EquipementUpdateDTO();
        dto.setId(id);

        Equipement existing = new Equipement();
        Equipement updatedEntity = new Equipement();
        EquipementDTO expectedDTO = new EquipementDTO();

        when(equipementRepository.findById(id)).thenReturn(Optional.of(existing));
        when(equipementRepository.save(existing)).thenReturn(updatedEntity);
        when(equipementMapper.toDTO(updatedEntity)).thenReturn(expectedDTO);

        EquipementDTO result = equipementService.update(dto);

        assertEquals(expectedDTO, result);
        verify(equipementRepository).findById(id);
        verify(equipementMapper).updateEntityFromDTO(dto, existing);
        verify(equipementRepository).save(existing);
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenEntityDoesNotExist() {
        Long id = 99L;
        EquipementUpdateDTO dto = new EquipementUpdateDTO();
        dto.setId(id);

        when(equipementRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> equipementService.update(dto));
        verify(equipementRepository).findById(id);
    }
}
