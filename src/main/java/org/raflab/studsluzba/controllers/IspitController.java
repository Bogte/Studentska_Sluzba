package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.model.dtos.IspitDTO;
import org.raflab.studsluzba.model.dtos.PrijavljeniStudentDTO;
import org.raflab.studsluzba.model.dtos.RezultatIspitaDTO;
import org.raflab.studsluzba.services.IspitPrijavaService;
import org.raflab.studsluzba.services.IspitService;
import org.raflab.studsluzba.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/ispit")
public class IspitController {

    @Autowired
    private IspitService ispitService;
    @Autowired
    private IspitPrijavaService ispitPrijavaService;
    @Autowired
    private StudentService studentService;

    //Svi ispiti
    @GetMapping
    public List<IspitDTO> getAll() {
        return ispitService.getAll();
    }
    //Trazi po ID
    @GetMapping("/{id}")
    public ResponseEntity<IspitDTO> getById(@PathVariable Long id) {
        IspitDTO dto = ispitService.getById(id);
        if (dto == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }
    //Create
    @PostMapping
    public ResponseEntity<IspitDTO> create(@RequestBody IspitDTO dto) {
        return ResponseEntity.ok(ispitService.create(dto));
    }
    //Update
    @PutMapping("/{id}")
    public ResponseEntity<IspitDTO> update(@PathVariable Long id, @RequestBody IspitDTO dto) {
        IspitDTO updated = ispitService.update(id, dto);
        if (updated == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }
    //Brisanje
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (ispitService.delete(id))
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
    //P{rijavljeni studnetui
    @GetMapping("/{id}/prijavljeni")
    public ResponseEntity<List<PrijavljeniStudentDTO>> getPrijavljeni(@PathVariable Long id) {

        List<PrijavljeniStudentDTO> list = ispitService.getPrijavljeniStudenti(id);

        return ResponseEntity.ok(list);
    }
    //Prosecna ocena na ispitu
    @GetMapping("/{id}/prosecna-ocena")
    public ResponseEntity<Double> getProsecnaOcena(@PathVariable Long id) {

        Double avg = ispitService.getProsecnaOcena(id);

        return ResponseEntity.ok(avg);
    }
    //Poloznei predmeti i studenti
    @GetMapping("/rezultati/{ispitId}")
    public ResponseEntity<List<RezultatIspitaDTO>> getRezultati(@PathVariable Long ispitId) {
        return ResponseEntity.ok(ispitPrijavaService.rezultatiIspita(ispitId));
    }
    //Koliko je puta polagao
    @GetMapping("/broj-polaganja")
    public ResponseEntity<Long> brojPolaganja(@RequestParam Long indeksId, @RequestParam Long predmetId
    ) {
        return ResponseEntity.ok(ispitService.brojPolaganjaPredmeta(indeksId, predmetId));
    }
}
