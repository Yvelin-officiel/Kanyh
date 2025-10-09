package com.data.kanyh.service;

import com.data.kanyh.dto.QueteDTO;
import com.data.kanyh.dto.QueteInputDTO;
import com.data.kanyh.mapper.QueteMapper;
import com.data.kanyh.model.Quete;
import com.data.kanyh.model.StatutQuete;
import com.data.kanyh.repository.QueteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
public class QueteService {

    private final QueteRepository queteRepository;
    private final QueteMapper queteMapper;

    public QueteService(QueteRepository queteRepository, QueteMapper queteMapper) {
        this.queteRepository = queteRepository;
        this.queteMapper = queteMapper;
    }

//    public List<QueteDTO> getAllQuetes() {
//        return queteRepository.findAll().stream()
//                .map(queteMapper::toDTO)
//                .toList();
//    }
//
//    public QueteDTO getQueteById(Long id) {
//        return queteRepository.findById(id)
//                .map(queteMapper::toDTO)
//                .orElseThrow(() -> new ResourceNotFoundException("Quête non trouvée"));
//    }

    public QueteDTO save(QueteInputDTO input) {
        Quete quete = queteMapper.toEntity(input);
        return queteMapper.toDTO(queteRepository.save(quete));
    }

//    public QueteDTO updateQuete(Long id, QueteInputDTO input) {
//        Quete quete = queteRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Quête non trouvée"));
//        queteMapper.updateEntityFromDTO(input, quete);
//        return queteMapper.toDTO(queteRepository.save(quete));
//    }
//
//    public void deleteQuete(Long id) {
//        if (!queteRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Quête non trouvée");
//        }
//        queteRepository.deleteById(id);
//    }
}
