package com.data.kanyh.controller;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.AventurierInputDTO;
import com.data.kanyh.service.AventurierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/aventuriers")
public class AventurierController {

    private final AventurierService aventurierService;

    public AventurierController(AventurierService aventurierService) {
        this.aventurierService = aventurierService;
    }

    @PostMapping
    public ResponseEntity<AventurierDTO> createAventurier(@RequestBody AventurierInputDTO aventurier) {
        AventurierDTO created = aventurierService.save(aventurier);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<AventurierDTO>> getAllAventurier() {
        List<AventurierDTO> list = aventurierService.getAllAventurier();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AventurierDTO> getAventurierById(@PathVariable Long id) {
        AventurierDTO dto = aventurierService.getAventurierById(id);
        return ResponseEntity.ok(dto);
    }
}
