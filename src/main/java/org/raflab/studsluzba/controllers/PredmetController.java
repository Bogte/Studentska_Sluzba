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
    public ResponseEntity<?> createPredmet(@RequestBody PredmetDTO predmetDTO) {
        try{
            Predmet p = predmetService.savePredmet(predmetDTO);
            return ResponseEntity.ok(EntityMappers.fromPredmetToDTO(p));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //Brisi predmet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePredmet(@PathVariable Long id) {
        Predmet p = predmetService.getPredmetById(id);
        if (p == null){
            return ResponseEntity.notFound().build();
        }
        predmetService.deletePredmet(id);
        return ResponseEntity.noContent().build();
    }
    //Izmeni predmet
    @PutMapping("/{id}")
    public ResponseEntity<PredmetDTO> updatePredmet(@PathVariable Long id, @RequestBody PredmetDTO predmetDTO) {
        PredmetDTO izmenjen = predmetService.updatePredmet(id, predmetDTO);
        if (izmenjen == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(izmenjen);
    }
    //Prosecna ocena u periodu
    @GetMapping("/{id}/prosecna-ocena")
    public ResponseEntity<?> getProsecnaOcena(@PathVariable Long id, @RequestParam int odGodine, @RequestParam int doGodine) {
        Double prosek = predmetService.getProsecnaOcenaZaPredmetIUPeriodu(id, odGodine, doGodine);
        if (prosek == null) {
            return ResponseEntity.ok("Nema položenih ispita u tom periodu.");
        }
        return ResponseEntity.ok(prosek);
    }
}
