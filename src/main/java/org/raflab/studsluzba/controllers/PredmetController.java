package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.dtos.PredmetDTO;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.services.NastavnikService;
import org.raflab.studsluzba.services.PredmetService;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/predmet")
public class PredmetController {

    @Autowired
    PredmetService predmetService;
    @Autowired
    private NastavnikService nastavnikService;

    @GetMapping(path = "/proba")
    public List<PredmetDTO> getNazivByStudProgram(@RequestParam(required = false) Long studProgram) {
        return EntityMappers.toPredmetDTOList(predmetService.getPredmetByStudProgram(studProgram));
    }

    @GetMapping(path = "/all")
    public List<PredmetDTO> getAllPredmet() {
        return EntityMappers.toPredmetDTOList(predmetService.getAllPredmet());
    }
    //Trazi po Id
    @GetMapping(path = "/{id}")
    public ResponseEntity<PredmetDTO> getPredmetById(@PathVariable Long id) {
        Predmet p = predmetService.getPredmetById(id);
        if (p == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(EntityMappers.fromPredmetToDTO(p));
    }
    //Cuvaj predmet
    @PostMapping
    public PredmetDTO createPredmet(@RequestBody PredmetDTO predmetDTO) {
        return EntityMappers.fromPredmetToDTO(predmetService.savePredmet(predmetDTO));
    }
}
