package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.IspitIzlazakRequest;
import org.raflab.studsluzba.controllers.request.IspitPrijavaRequest;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.dtos.RezultatIspitaDTO;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.services.IspitPrijavaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prijava")
@CrossOrigin
public class IspitPrijavaController {

    @Autowired
    private IspitPrijavaService ispitPrijavaService;

    //Prijava ispita
    @PostMapping
    public ResponseEntity<String> prijavi(@RequestBody IspitPrijavaRequest req) {

        return ResponseEntity.ok(ispitPrijavaService.prijaviIspit(req.getIspitId(), req.getStudentIndeksId()));
    }
    //Upisivanje polozenih predmeta
    //Nisam bas najbolje razumeo zahtev ili mi je samo moja sema/zamisao baze uzasna :(
    @PostMapping("/unesi-izlazak/{prijavaId}")
    public ResponseEntity<String> unesiIzlazak(@PathVariable Long prijavaId) {
        return ResponseEntity.ok(ispitPrijavaService.unesiIzlazakNaIspit(prijavaId));
    }

}
