package Controller;

import model.Specialite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.SpecialiteService;

@RestController
public class SpecialiteController {

    @Autowired
    private SpecialiteService specialiteService;

    @PostMapping("/Specialite")
    public ResponseEntity<Specialite> addSpecialite(
            @RequestBody Specialite specialite
    ) {
        Specialite savedSpecialite = specialiteService.addSpecialite(specialite);
        return ResponseEntity.ok(savedSpecialite);
    }
}
