package com.data.kanyh.service;

import com.data.kanyh.dto.QueteDTO;
import com.data.kanyh.dto.QueteInputDTO;
import com.data.kanyh.mapper.QueteMapper;
import com.data.kanyh.model.Quete;
import com.data.kanyh.model.StatutQuete;
import com.data.kanyh.repository.QueteRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@AllArgsConstructor
public class QueteService {

    private final QueteRepository queteRepository;
    private final QueteMapper queteMapper;

    public List<QueteDTO> getAllQuetes() {
        return queteRepository.findAll().stream()
                .map(queteMapper::toDTO)
                .toList();
    }

    // TODO : gérer le cas où la quête n'existe pas
    public QueteDTO getQueteById(Long id) {
        return queteRepository.findById(id)
                .map(queteMapper::toDTO).stream().findFirst().orElseThrow();
    }

    public QueteDTO save(QueteInputDTO input) {
        Quete quete = queteRepository.save(queteMapper.toEntity(input));
        return queteMapper.toDTO(quete);
    }

    // TODO : gérer le cas où la quête n'existe pas
    public QueteDTO updateQuete(Long id, QueteInputDTO input) {
        Quete quete = queteRepository.findById(id)
                .orElseThrow();
        queteMapper.updateEntityFromDTO(input, quete);
        return queteMapper.toDTO(queteRepository.save(quete));
    }

    // TODO : gérer le cas où la quête n'existe pas
    public void deleteQuete(Long id) {
        queteRepository.deleteById(id);
    }
}
