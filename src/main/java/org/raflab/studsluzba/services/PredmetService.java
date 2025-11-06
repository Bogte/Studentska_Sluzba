package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.model.dtos.PredmetDTO;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredmetService {

    @Autowired
    PredmetRepository predmetRepository;
    @Autowired
    StudijskiProgramRepository studijskiProgramRepository;

    public List<Predmet> getPredmetByStudProgram(Long studProgram) {
        return predmetRepository.getPredmetForStudentskiProgram(studProgram);
    }

    public List<Predmet> getAllPredmet() {
        return predmetRepository.getAllPredmet();
    }

    public Predmet getPredmetById(Long id) {
        return predmetRepository.findById(id).orElse(null);
    }
    //Cuvaj predmet
    public Predmet savePredmet(PredmetDTO predmetDTO) {
        Predmet p = EntityMappers.fromDTOToPredmet(predmetDTO);
        //Radi samo ako je imde studijskog programa definisano kao jedinstveno
        StudijskiProgram sp = studijskiProgramRepository.findByNaziv(predmetDTO.getStudProgramNaziv()).orElse(null);
        p.setStudProgram(sp);
        return predmetRepository.save(p);
    }
}
