package com.data.kanyh.service;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.AventurierInputDTO;
import com.data.kanyh.dto.AventurierUpdateDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.AventurierMapper;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.repository.AventurierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AventurierServiceTest {

    @Mock
    private AventurierRepository aventurierRepository;

    @Mock
    private AventurierMapper aventurierMapper;

    @InjectMocks
    private AventurierService aventurierService;

    @Test
    void testSave() {
        AventurierInputDTO inputDTO = new AventurierInputDTO();
        Aventurier aventurier = new Aventurier();
        AventurierDTO expectedDTO = new AventurierDTO();

        when(aventurierMapper.toEntity(inputDTO)).thenReturn(aventurier);
        when(aventurierRepository.save(aventurier)).thenReturn(aventurier);
        when(aventurierMapper.toDTO(aventurier)).thenReturn(expectedDTO);

        AventurierDTO result = aventurierService.save(inputDTO);

        assertEquals(expectedDTO, result);
        verify(aventurierRepository).save(aventurier);
    }

    @Test
    void testGetAllAventurier() {
        Aventurier aventurier1 = new Aventurier();
        Aventurier aventurier2 = new Aventurier();
        AventurierDTO dto1 = new AventurierDTO();
        AventurierDTO dto2 = new AventurierDTO();
        List<Aventurier> aventuriers = List.of(aventurier1, aventurier2);

        when(aventurierRepository.findAll()).thenReturn(aventuriers);
        when(aventurierMapper.toDTO(aventurier1)).thenReturn(dto1);
        when(aventurierMapper.toDTO(aventurier2)).thenReturn(dto2);

        List<AventurierDTO> result = aventurierService.getAllAventurier();

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
        verify(aventurierRepository).findAll();
    }

    @Test
    void testGetAventurierById_WhenExists() {
        Long id = 1L;
        Aventurier aventurier = new Aventurier();
        AventurierDTO expectedDTO = new AventurierDTO();

        when(aventurierRepository.findById(id)).thenReturn(java.util.Optional.of(aventurier));
        when(aventurierMapper.toDTO(aventurier)).thenReturn(expectedDTO);

        AventurierDTO result = aventurierService.getAventurierById(id);

        assertEquals(expectedDTO, result);
        verify(aventurierRepository).findById(id);
    }

    @Test
    void testGetAventurier_WhenNotExists_ShouldThrowNotFoundException() {
        Long id = 1L;

        when(aventurierRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> aventurierService.getAventurierById(id));
        verify(aventurierRepository).findById(id);
    }

    @Test
    void testUpdate_WhenExists() {
        Long id = 1L;
        AventurierUpdateDTO updateDTO = new AventurierUpdateDTO();
        updateDTO.setId(id);
        Aventurier aventurier = new Aventurier();
        Aventurier updatedAventurier = new Aventurier();
        AventurierDTO expectedDTO = new AventurierDTO();

        when(aventurierRepository.findById(id)).thenReturn(java.util.Optional.of(aventurier));
        when(aventurierRepository.save(aventurier)).thenReturn(updatedAventurier);
        when(aventurierMapper.toDTO(updatedAventurier)).thenReturn(expectedDTO);

        AventurierDTO result = aventurierService.update(updateDTO);

        assertEquals(expectedDTO, result);
        verify(aventurierMapper).updateEntityFromDTO(updateDTO, aventurier);
        verify(aventurierRepository).save(aventurier);
    }

    @Test
    void testUpdate_WhenNotExists_ShouldThrowNotFoundException() {
        Long id = 1L;
        AventurierUpdateDTO updateDTO = new AventurierUpdateDTO();
        updateDTO.setId(id);

        when(aventurierRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> aventurierService.update(updateDTO));
        verify(aventurierRepository).findById(id);
    }

    @Test
    void testDelete_WhenExists() {
        Long id = 1L;

        when(aventurierRepository.existsById(id)).thenReturn(true);

        aventurierService.delete(id);

        verify(aventurierRepository).existsById(id);
        verify(aventurierRepository).deleteById(id);
    }

    @Test
    void testDelete_WhenNotExists_ShouldThrowNotFoundException() {
        Long id = 1L;

        when(aventurierRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> aventurierService.delete(id));
        verify(aventurierRepository).existsById(id);
    }
}
