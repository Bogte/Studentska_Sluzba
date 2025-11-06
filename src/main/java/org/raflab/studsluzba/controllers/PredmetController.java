package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.model.dtos.PredmetDTO;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.services.NastavnikService;
import org.raflab.studsluzba.services.PredmetService;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.beans.factory.annotation.Autowired;
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
}
