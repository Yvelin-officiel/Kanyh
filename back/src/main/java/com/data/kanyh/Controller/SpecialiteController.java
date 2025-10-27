package com.data.kanyh.controller;

import com.data.kanyh.model.Specialite;
import com.data.kanyh.service.SpecialiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specialite")
public class SpecialiteController {

    @Autowired
    private SpecialiteService specialiteService;

    @PostMapping
    public ResponseEntity<Specialite> addSpecialite(
            @RequestBody Specialite specialite
    ) {
        Specialite savedSpecialite = specialiteService.addSpecialite(specialite);
        return ResponseEntity.ok(savedSpecialite);
    }

    @GetMapping
    public ResponseEntity<List<Specialite>> getAllSpecialites() {
        List<Specialite> specialites = specialiteService.getAllSpecialites();
        return ResponseEntity.ok(specialites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialite(@PathVariable Integer id) {
        specialiteService.deleteSpecialite(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Specialite> updateSpecialite(
            @PathVariable Integer id,
            @RequestBody Specialite specialiteDetails
    ) {
        Specialite updatedSpecialite = specialiteService.updateSpecialite(id, specialiteDetails);
        return ResponseEntity.ok(updatedSpecialite);
    }
}
