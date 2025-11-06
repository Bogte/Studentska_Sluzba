package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredmetService {

    @Autowired
    PredmetRepository predmetRepository;

    public List<Predmet> getPredmetByStudProgram(Long studProgram) {
        return predmetRepository.getPredmetForStudentskiProgram(studProgram);
    }

    public List<Predmet> getAllPredmet() {
        return predmetRepository.getAllPredmet();
    }
}
