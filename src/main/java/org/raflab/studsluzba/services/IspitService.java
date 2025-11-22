package org.raflab.studsluzba.services;

import lombok.Data;
import org.hibernate.sql.Update;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.IspitPrijava;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.dtos.IspitDTO;
import org.raflab.studsluzba.model.dtos.PrijavljeniStudentDTO;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class IspitService {

    @Autowired
    private IspitRepository ispitRepository;

    @Autowired
    private PredmetRepository predmetRepository;

    @Autowired
    private NastavnikRepository nastavnikRepository;

    @Autowired
    private IspitniRokRepository ispitniRokRepository;

    @Autowired
    private IspitPrijavaRepository ispitPrijavaRepository;

    @Autowired
    private StudentIndeksRepository studentIndeksRepository;

    @Autowired
    private SlusaPredmetRepository slusaPredmetRepository;

    //Svi ispiti
    public List<IspitDTO> getAll() {
        return StreamSupport.stream(ispitRepository.findAll().spliterator(), false).map(EntityMappers::fromIspitToDTO).collect(Collectors.toList());
    }

    //Prondaji po ID
    public IspitDTO getById(Long id) {
        Ispit ispit = ispitRepository.findById(id).orElse(null);
        return ispit == null ? null : EntityMappers.fromIspitToDTO(ispit);
    }

    //Create
    public IspitDTO create(IspitDTO dto) {
        Ispit ispit = EntityMappers.fromDTOToIspit(dto, predmetRepository, nastavnikRepository, ispitniRokRepository);
        return EntityMappers.fromIspitToDTO(ispitRepository.save(ispit));
    }

    //Update
    public IspitDTO update(Long id, IspitDTO dto) {

        Ispit stari = ispitRepository.findById(id).orElse(null);
        if (stari == null) return null;

        stari.setDatumOdrzavanja(dto.getDatumOdrzavanja());
        stari.setVremePocetka(dto.getVremePocetka());
        stari.setZakljucen(dto.isZakljucen());

        if (dto.getPredmetId() != null)
            stari.setPredmet(predmetRepository.findById(dto.getPredmetId()).orElse(null));

        if (dto.getNastavnikId() != null)
            stari.setNastavnik(nastavnikRepository.findById(dto.getNastavnikId()).orElse(null));

        if (dto.getIspitniRokId() != null)
            stari.setIspitniRok(ispitniRokRepository.findById(dto.getIspitniRokId()).orElse(null));

        return EntityMappers.fromIspitToDTO(ispitRepository.save(stari));
    }

    //Brisanje
    public boolean delete(Long id) {
        if (!ispitRepository.existsById(id))
            return false;
        ispitRepository.deleteById(id);
        return true;
    }

    //Prijavljeni studentit za ispit
    @Transactional
    public List<PrijavljeniStudentDTO> getPrijavljeniStudenti(Long ispitId) {

        Ispit ispit = ispitRepository.findById(ispitId).orElse(null);
        if (ispit == null)
            return Collections.emptyList();

        List<IspitPrijava> prijave = ispit.getPrijave();
        prijave.size();

        return prijave.stream().map(EntityMappers::fromIspitPrijavaToDTO).collect(Collectors.toList());
    }

    //Prosecna ocena na ispitu
    public Double getProsecnaOcena(Long ispitId) {
        Double avg = ispitPrijavaRepository.findProsecnaOcenaZaIspit(ispitId);
        return avg == null ? 0.0 : avg;
    }

    //Koliko je puta polagao
    public Long brojPolaganjaPredmeta(Long indeksId, Long predmetId) {
        return ispitPrijavaRepository.countPolaganja(indeksId, predmetId);
    }
}
