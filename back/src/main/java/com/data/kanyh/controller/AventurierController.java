package com.data.kanyh.controller;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.AventurierDetailDTO;
import com.data.kanyh.dto.AventurierInputDTO;
import com.data.kanyh.dto.AventurierUpdateDTO;
import com.data.kanyh.service.AventurierService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aventuriers")
public class AventurierController {

    private final AventurierService aventurierService;

    public AventurierController(AventurierService aventurierService) {
        this.aventurierService = aventurierService;
    }

    @PostMapping
    public ResponseEntity<AventurierDTO> createAventurier(@Valid @RequestBody AventurierInputDTO aventurier) {
        AventurierDTO created = aventurierService.save(aventurier);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<AventurierDTO>> getAllAventurier() {
        List<AventurierDTO> list = aventurierService.getAllAventurier();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AventurierDetailDTO> getAventurierById(@PathVariable Long id) {
        AventurierDetailDTO dto = aventurierService.getAventurierDetail(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AventurierDTO> updateAventurier(@Valid @RequestBody AventurierUpdateDTO aventurier) {
        AventurierDTO updated = aventurierService.update(aventurier);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAventurier(@PathVariable Long id) {
        aventurierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
