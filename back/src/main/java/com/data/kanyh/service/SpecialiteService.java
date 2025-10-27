package com.data.kanyh.service;

import com.data.kanyh.model.Specialite;
import com.data.kanyh.repository.SpecialiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialiteService {


    @Autowired
    private SpecialiteRepository specialiteRepository;

    public Specialite addSpecialite(Specialite specialite) {
        return specialiteRepository.save(specialite);
    }

    public List<Specialite> getAllSpecialites() {
        return specialiteRepository.findAll();
    }

    public void deleteSpecialite(Integer id) {
        specialiteRepository.deleteById(id);
    }

    public Specialite updateSpecialite(Integer id, Specialite specialiteDetails) {
        Specialite specialite = specialiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Specialite non trouvée avec l'id: " + id));

        specialite.setNom(specialiteDetails.getNom());

        return specialiteRepository.save(specialite);
    }
}
