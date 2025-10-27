package com.data.kanyh.service;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.AventurierInputDTO;
import com.data.kanyh.dto.AventurierUpdateDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.AventurierMapper;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.repository.AventurierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AventurierService {

    private final AventurierRepository aventurierRepository;
    private final AventurierMapper aventurierMapper;

    private static final String NOT_FOUND = "Aventurier non trouvé";

    public AventurierService(AventurierRepository aventurierRepository, AventurierMapper aventurierMapper) {
        this.aventurierRepository = aventurierRepository;
        this.aventurierMapper = aventurierMapper;
    }

    public AventurierDTO save(AventurierInputDTO dto) {
        Aventurier aventurier = aventurierRepository.save(aventurierMapper.toEntity(dto));
        return aventurierMapper.toDTO(aventurier);
    }

    public List<AventurierDTO> getAllAventurier() {
        return aventurierRepository.findAll()
                .stream()
                .map(aventurierMapper::toDTO)
                .toList();
    }

    public AventurierDTO getAventurierById(Long id) {
        return aventurierRepository.findById(id)
                .map(aventurierMapper::toDTO)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    public AventurierDTO update(AventurierUpdateDTO dto) {
        Aventurier aventurier = aventurierRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        aventurierMapper.updateEntityFromDTO(dto, aventurier);
        Aventurier updated = aventurierRepository.save(aventurier);
        return aventurierMapper.toDTO(updated);
    }

    public void delete(Long id) {
        if (!aventurierRepository.existsById(id)) {
            throw new NotFoundException(NOT_FOUND);
        }
        aventurierRepository.deleteById(id);
    }

}
