package com.data.kanyh.Controller;

import com.data.kanyh.model.Specialite;
import com.data.kanyh.service.SpecialiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SpecialiteController {

    @Autowired
    private SpecialiteService specialiteService;

    @PostMapping("/specialite")
    public ResponseEntity<Specialite> addSpecialite(
            @RequestBody Specialite specialite
    ) {
        Specialite savedSpecialite = specialiteService.addSpecialite(specialite);
        return ResponseEntity.ok(savedSpecialite);
    }

    @GetMapping("/specialite")
    public ResponseEntity<List<Specialite>> getAllSpecialites() {
        List<Specialite> specialites = specialiteService.getAllSpecialites();
        return ResponseEntity.ok(specialites);
    }

    @DeleteMapping("/specialite/{id}")
    public ResponseEntity<Void> deleteSpecialite(@PathVariable Integer id) {
        specialiteService.deleteSpecialite(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/specialite/{id}")
    public ResponseEntity<Specialite> updateSpecialite(
            @PathVariable Integer id,
            @RequestBody Specialite specialiteDetails
    ) {
        Specialite updatedSpecialite = specialiteService.updateSpecialite(id, specialiteDetails);
        return ResponseEntity.ok(updatedSpecialite);
    }
}
