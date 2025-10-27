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

    /**
     * Crée et enregistre un nouvel aventurier.
     *
     * @param dto les données de l'aventurier à créer
     * @return le DTO de l'aventurier créé
     */
    public AventurierDTO save(AventurierInputDTO dto) {
        Aventurier aventurier = aventurierRepository.save(aventurierMapper.toEntity(dto));
        return aventurierMapper.toDTO(aventurier);
    }

    /**
     * Récupère la liste de tous les aventuriers.
     *
     * @return la liste des DTOs de tous les aventuriers
     */
    public List<AventurierDTO> getAllAventurier() {
        return aventurierRepository.findAll()
                .stream()
                .map(aventurierMapper::toDTO)
                .toList();
    }

    /**
     * Récupère un aventurier par son id.
     *
     * @param id l'identifiant de l'aventurier
     * @return le DTO de l'aventurier trouvé
     * @throws NotFoundException si l'aventurier n'existe pas
     */
    public AventurierDTO getAventurierById(Long id) {
        return aventurierRepository.findById(id)
                .map(aventurierMapper::toDTO)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    /**
     * Met à jour un aventurier existant.
     *
     * @param dto les nouvelles données de l'aventurier
     * @return le DTO de l'aventurier mis à jour
     * @throws NotFoundException si l'aventurier n'existe pas
     */
    public AventurierDTO update(AventurierUpdateDTO dto) {
        Aventurier aventurier = aventurierRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        aventurierMapper.updateEntityFromDTO(dto, aventurier);
        Aventurier updated = aventurierRepository.save(aventurier);
        return aventurierMapper.toDTO(updated);
    }

    /**
     * Supprime un aventurier par son id.
     *
     * @param id l'identifiant de l'aventurier à supprimer
     * @throws NotFoundException si l'aventurier n'existe pas
     */
    public void delete(Long id) {
        if (!aventurierRepository.existsById(id)) {
            throw new NotFoundException(NOT_FOUND);
        }
        aventurierRepository.deleteById(id);
    }

}
