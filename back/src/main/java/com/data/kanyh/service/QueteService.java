package com.data.kanyh.service;

import com.data.kanyh.dto.QueteDTO;
import com.data.kanyh.dto.QueteInputDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.QueteMapper;
import com.data.kanyh.model.Quete;
import com.data.kanyh.repository.QueteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    public QueteDTO getQueteById(Long id) {
        return queteRepository.findById(id)
                .map(queteMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Quête non trouvée"));
    }

    public QueteDTO save(QueteInputDTO input) {
        Quete quete = queteRepository.save(queteMapper.toEntity(input));
        return queteMapper.toDTO(quete);
    }

    public QueteDTO updateQuete(Long id, QueteInputDTO input) {
        Quete quete = queteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quête non trouvée"));
        queteMapper.updateEntityFromDTO(input, quete);
        return queteMapper.toDTO(queteRepository.save(quete));
    }

    public void deleteQuete(Long id) {
        if (!queteRepository.existsById(id)) {
            throw new NotFoundException("Quête non trouvée");
        }
        queteRepository.deleteById(id);
    }
}
