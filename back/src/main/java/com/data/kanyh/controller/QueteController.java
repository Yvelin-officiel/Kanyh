package com.data.kanyh.controller;

import com.data.kanyh.dto.QueteDTO;
import com.data.kanyh.dto.QueteInputDTO;
import com.data.kanyh.dto.TeamSuggestionDTO;
import com.data.kanyh.service.QueteService;
import com.data.kanyh.service.TeamGenerationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quetes")
@AllArgsConstructor
public class QueteController {

    private final QueteService queteService;
    private final TeamGenerationService teamGenerationService;

    @GetMapping
    public ResponseEntity<List<QueteDTO>> getAllQuetes() {
        return ResponseEntity.ok(queteService.getAllQuetes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QueteDTO> getQueteById(@PathVariable Long id) {
        return ResponseEntity.ok(queteService.getQueteById(id));
    }

    @PostMapping
    public ResponseEntity<QueteDTO> postQuete(@Valid @RequestBody QueteInputDTO input) {
        QueteDTO created = queteService.save(input);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QueteDTO> updateQuete(@PathVariable Long id, @RequestBody QueteInputDTO input) {
        return ResponseEntity.ok(queteService.updateQuete(id, input));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuete(@PathVariable Long id) {
        queteService.deleteQuete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/historique/{aventurierId}")
    public ResponseEntity<List<QueteDTO>> getQuetesByAventurierId(@PathVariable Long aventurierId) {
        return ResponseEntity.ok(queteService.getQuetesByAventurierId(aventurierId));
    }

    /**
     * Génère une suggestion automatique d'équipe pour une quête.
     * Utilise un algorithme de matching basé sur la disponibilité,
     * les spécialités requises, le niveau d'expérience et le ratio coût/performance.
     *
     * @param id l'identifiant de la quête
     * @param nombreParticipants le nombre d'aventuriers souhaités (par défaut : 4)
     * @return un {@link TeamSuggestionDTO} contenant les IDs des aventuriers suggérés
     */
    @PostMapping("/{id}/generate-team")
    public ResponseEntity<TeamSuggestionDTO> generateTeam(
            @PathVariable Long id,
            @RequestParam(defaultValue = "4") Integer nombreParticipants
    ) {
        TeamSuggestionDTO suggestion = teamGenerationService.generateTeam(id, nombreParticipants);
        return ResponseEntity.ok(suggestion);
    }
}
