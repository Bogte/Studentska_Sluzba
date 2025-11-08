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
    //Brisi predmet
    public void deletePredmet(Long id) {
        predmetRepository.deleteById(id);
    }
    //Itzmeni predmet
    public PredmetDTO updatePredmet(Long id, PredmetDTO predmetDTO) {
        Predmet p = predmetRepository.findById(id).orElse(null);
        if (p == null) {
            return null;
        }

        p.setNaziv(predmetDTO.getStudProgramNaziv());
        p.setSifra(predmetDTO.getSifra());
        p.setEspb(predmetDTO.getEspb());
        p.setObavezan(predmetDTO.isObavezan());
        p.setOpis(predmetDTO.getOpis());

        if(predmetDTO.getStudProgramNaziv() != null) {
            StudijskiProgram sp = studijskiProgramRepository.findByNaziv(predmetDTO.getStudProgramNaziv()).orElse(null);
            p.setStudProgram(sp);
        }

        Predmet pred = predmetRepository.save(p);
        return EntityMappers.fromPredmetToDTO(pred);
    }
}
