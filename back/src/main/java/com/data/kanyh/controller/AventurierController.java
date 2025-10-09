package com.data.kanyh.controller;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.AventurierInputDTO;
import com.data.kanyh.service.AventurierService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aventuriers")
public class AventurierController {

    private final AventurierService aventurierService;

    public AventurierController(AventurierService aventurierService) {
        this.aventurierService = aventurierService;
    }

    @PostMapping
    public AventurierDTO createAventurier(@RequestBody AventurierInputDTO aventurier) {
        return aventurierService.save(aventurier);
    }
}
