package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.model.dtos.IspitDTO;
import org.raflab.studsluzba.services.IspitService;
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
}
