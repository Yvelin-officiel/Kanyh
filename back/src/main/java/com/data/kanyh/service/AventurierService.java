package com.data.kanyh.service;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.AventurierInputDTO;
import com.data.kanyh.mapper.AventurierMapper;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.repository.AventurierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AventurierService {

    private final AventurierRepository aventurierRepository;
    private final AventurierMapper aventurierMapper;

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

    // TODO : exception NotFound
    public AventurierDTO getAventurierById(Long id) {
        return aventurierRepository.findById(id).map(aventurierMapper::toDTO).orElseThrow();
    }

}
