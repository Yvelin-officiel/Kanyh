package service;

import model.Specialite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.SpecialiteRepository;

@Service
public class SpecialiteService {


    @Autowired
    private SpecialiteRepository specialiteRepository;

    public Specialite addSpecialite(Specialite specialite) {
        return specialiteRepository.save(specialite);
    }
}
