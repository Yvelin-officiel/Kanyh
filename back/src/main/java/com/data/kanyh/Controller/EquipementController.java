package com.data.kanyh.controller;

import com.data.kanyh.dto.*;
import com.data.kanyh.service.EquipementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipement")
public class EquipementController {

    private final EquipementService equipementService;

    public EquipementController(EquipementService equipementService) {
        this.equipementService = equipementService;
    }

    @PostMapping
    public ResponseEntity<EquipementDTO> createEquipement(@Valid @RequestBody EquipementInputDTO equipement) {
        EquipementDTO created = equipementService.save(equipement);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<EquipementDTO>> getAllEquipement() {
        List<EquipementDTO> list = equipementService.getAllEquipement();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipement(@PathVariable Long id) {
        equipementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<EquipementDTO> updateEquipement(@Valid @RequestBody EquipementUpdateDTO equipement) {
        EquipementDTO updated = equipementService.update(equipement);
        return ResponseEntity.ok(updated);
    }
}
