package org.raflab.studsluzba.services;

import org.raflab.studsluzba.controllers.request.IspitIzlazakRequest;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.RezultatIspitaDTO;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IspitPrijavaService {

    @Autowired
    private IspitRepository ispitRepository;

    @Autowired
    private StudentIndeksRepository studentIndeksRepository;

    @Autowired
    private SlusaPredmetRepository slusaPredmetRepository;

    @Autowired
    private IspitPrijavaRepository ispitPrijavaRepository;

    @Autowired
    private PolozenPredmetRepository polozenPredmetRepository;

    //Prijava ispita za izabrani rok i da li slusa taj predmet
    public String prijaviIspit(Long ispitId, Long indeksId) {

        Ispit ispit = ispitRepository.findById(ispitId).orElse(null);
        //TODO Da se nme uzme samo taj neki postojeci nego da se vidi da li je aktivan indeks
        StudentIndeks indeks = studentIndeksRepository.findByStudentIdAndAktivanTrue(indeksId).orElse(null);

        if (ispit == null || indeks == null)
            return "Nepostojeći ispit ili indeks.";

        Long predmetId = ispit.getPredmet().getId();

        boolean slusa = slusaPredmetRepository.existsByStudentIndeksAndDrziPredmet_Predmet_Id(indeks, predmetId);

        if (!slusa) return "Student NE SLUŠA ovaj predmet — ne može da prijavi ispit.";

        boolean vecPrijavio = ispitPrijavaRepository.existsByStudentIndeksAndIspit(indeks, ispit);

        if (vecPrijavio) return "Student je već prijavio ovaj ispit.";

        IspitPrijava prijava = new IspitPrijava();

        prijava.setIspit(ispit);
        prijava.setStudentIndeks(indeks);
        prijava.setDatumPrijave(LocalDate.now());

        prijava.setPredispitniPoeni(0);
        prijava.setPoeniSaIspita(0);
        prijava.setUkupnoPoena(0);
        prijava.setOcena(null);
        prijava.setPolozen(false);
        prijava.setPonistio(false);
        prijava.setPriznat(false);
        prijava.setNapomena(null);

        ispitPrijavaRepository.save(prijava);

        return "Uspešno prijavljen ispit!";
    }

    //Izlazak na ispit i minimalno 51 poena
    @Transactional
    public String unesiIzlazakNaIspit(Long prijavaId) {

        IspitPrijava ip = ispitPrijavaRepository.findById(prijavaId).orElse(null);
        if (ip == null)
            return "Prijava ne postoji.";

        if (ip.isPonistio())
            return "Ispit je ponisten ne može se unositi rezultat.";

        if (ip.isPriznat()) {

            boolean dodat = upisiPolozenPredmet(ip);

            if (dodat)
                return "Priznati predmet je dodat u polozenim predmetima.";

            return "Priznati predmet je već upisan kao polozen.";
        }

        int predispitni = Optional.ofNullable(ip.getPredispitniPoeni()).orElse(0);
        int saIspita = Optional.ofNullable(ip.getPoeniSaIspita()).orElse(0);
        if (predispitni == 0 && saIspita == 0)
            return "Poeni nisu upisani u prijavu ispita.";

        int ukupno = predispitni + saIspita;
        ip.setUkupnoPoena(ukupno);

        if (ukupno >= 51) {
            ip.setPolozen(true);
            int ocena = izracunajOcenu(ukupno);
            ip.setOcena(ocena);
            boolean dodat = upisiPolozenPredmet(ip);
            if (!dodat) return "Poloznei predmet je vec upisan";

        } else {
            ip.setPolozen(false);
            ip.setOcena(5);
            return "Predmet nije polozen.";
        }

        ispitPrijavaRepository.save(ip);
        return "Rezultat ispita je uspesno unet.";
    }

    private int izracunajOcenu(int poeni) {
        if (poeni >= 91) return 10;
        if (poeni >= 81) return 9;
        if (poeni >= 71) return 8;
        if (poeni >= 61) return 7;
        return 6;
    }

    private Boolean upisiPolozenPredmet(IspitPrijava ip) {

        StudentIndeks indeks = ip.getStudentIndeks();
        Predmet predmet = ip.getIspit().getPredmet();


        boolean postoji = polozenPredmetRepository.existsByStudentIndeksAndPredmet(indeks, predmet);
        if (postoji) {
            //System.out.println("Polozen predmet već postoji neće biti ponovo dodat.");
            return false;
        }


        PolozenPredmet pp = new PolozenPredmet();
        pp.setStudentIndeks(indeks);
        pp.setPredmet(predmet);
        pp.setDatumPolaganja(LocalDate.now());
        if(ip.isPriznat()){
            pp.setUkupnoPoena(100);
            pp.setOcena(10);
        }
        else{
            pp.setUkupnoPoena(ip.getUkupnoPoena());
            pp.setOcena(ip.getOcena());
        }
        pp.setNapomena(ip.getNapomena());

        polozenPredmetRepository.save(pp);
        return true;
    }

    //Rezultati ispita
    public List<RezultatIspitaDTO> rezultatiIspita(Long ispitId) {

        List<IspitPrijava> prijave = ispitPrijavaRepository.findByIspit_Id(ispitId);

        return EntityMappers.toRezultatIspitaDTOList(prijave);
    }


}
