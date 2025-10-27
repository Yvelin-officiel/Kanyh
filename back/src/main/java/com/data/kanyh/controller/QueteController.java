package com.data.kanyh.controller;

import com.data.kanyh.dto.QueteDTO;
import com.data.kanyh.dto.QueteInputDTO;
import com.data.kanyh.service.QueteService;
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
}
