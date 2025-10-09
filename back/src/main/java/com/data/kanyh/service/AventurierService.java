package com.data.kanyh.service;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.AventurierInputDTO;
import com.data.kanyh.mapper.AventurierMapper;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.repository.AventurierRepository;
import org.springframework.stereotype.Service;

@Service
public class AventurierService {

    private final AventurierRepository aventurierRepository;
    private final AventurierMapper aventurierMapper;

    public AventurierService(AventurierRepository aventurierRepository, AventurierMapper aventurierMapper) {
        this.aventurierRepository = aventurierRepository;
        this.aventurierMapper = aventurierMapper;
    }

    public AventurierDTO save(AventurierInputDTO dto) {
        Aventurier entity = aventurierMapper.toEntity(dto);
        Aventurier aventurier = aventurierRepository.save(entity);
        return aventurierMapper.toDTO(aventurier);
    }
}
