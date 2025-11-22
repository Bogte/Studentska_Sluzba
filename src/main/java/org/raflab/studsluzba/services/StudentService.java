package org.raflab.studsluzba.services;

import org.raflab.studsluzba.controllers.request.ObnovaGodineRequest;
import org.raflab.studsluzba.controllers.request.UpisGodineRequest;
import org.raflab.studsluzba.controllers.request.UplataRequest;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.*;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    //Takodje ista greska vazi i za ovo da sam 2 puta pravio
    @Autowired
    private StudentPodaciRepository studentRepo;

    //Greskom sam koristio autowierd pa me mrzi da sada prepravljam
    @Autowired
    private StudentIndeksRepository indeksRepo;

    @Autowired
    private StudijskiProgramRepository programRepo;

    @Autowired
    private UpisGodineRepository upisGodineRepository;

    @Autowired
    private IspitPrijavaRepository ispitPrijavaRepository;

    @Autowired
    private PredmetRepository predmetRepository;

    @Autowired
    private ObnovaGodineRepository obnovaGodineRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UplataRepository uplataRepository;

    @Autowired
    private StudentPodaciRepository studentPodaciRepository;
    @Autowired
    private EntityMappers entityMappers;
    @Autowired
    private StudentIndeksRepository studentIndeksRepository;

    @Autowired
    private SrednjaSkolaRepository srednjaSkolaRepository;


    //Create studnet
    public StudentDTO create(StudentDTO dto) {

        StudentPodaci sp = EntityMappers.fromDTOToStudentPodaci(dto);
        sp = studentRepo.save(sp);

        StudijskiProgram program = programRepo.findById(dto.getStudijskiProgramId())
                .orElseThrow(() -> new RuntimeException("Studijski program ne postoji"));

        StudentIndeks si = EntityMappers.fromDTOToStudentIndeks(dto, sp, program);
        si = indeksRepo.save(si);

        return EntityMappers.toStudnetDTO(sp, si);
    }

    //Svi studenti
    public List<StudentDTO> getAllStudents() {

        List<StudentDTO> result = new ArrayList<>();

        for (StudentPodaci sp : studentRepo.findAll()) {

            StudentIndeks indeks = indeksRepo.findFirstByStudentAndAktivanTrue(sp).orElse(null);

            result.add(EntityMappers.toStudnetDTO(sp, indeks));
        }
        return result;
    }

    //Nadji po ID
    public StudentDTO getById(Long id) {

        StudentPodaci sp = studentRepo.findById(id).orElse(null);
        if (sp == null) return null;

        StudentIndeks aktivniIndeks =
                indeksRepo.findFirstByStudentAndAktivanTrue(sp).orElse(null);

        return EntityMappers.toStudnetDTO(sp, aktivniIndeks);
    }

    //Brisi studneta po ID
    public void delete(Long id) {
        studentRepo.deleteById(id);
    }

    //Studnet podaci na osnivu indeks ID
    public StudentDTO getByIndex(int broj, int godina) {

        Optional<StudentIndeks> opt = indeksRepo.findByBrojAndGodina(broj, godina);

        if (opt.isEmpty()) {
            return null;
        }

        StudentIndeks si = opt.get();
        StudentPodaci sp = si.getStudent();

        return EntityMappers.toStudnetDTO(sp, si);
    }

    //Polozeni ispiti
    public Page<IspitPolozenDTO> getPolozeniIspiti(int broj, int godina, int page, int size) {

        StudentIndeks indeks = indeksRepo.findByBrojAndGodina(broj, godina)
                .orElse(null);

        if (indeks == null)
            return Page.empty();

        Pageable pageable = PageRequest.of(page, size, Sort.by("ispit.datumOdrzavanja").descending());

        Page<IspitPrijava> prijave = ispitPrijavaRepository.findByStudentIndeksAndPolozenTrue(indeks, pageable);

        return prijave.map(ip -> {
            IspitPolozenDTO dto = new IspitPolozenDTO();
            dto.setPredmet(ip.getIspit().getPredmet().getNaziv());
            dto.setOcena(ip.getOcena());
            dto.setDatum(ip.getIspit().getDatumOdrzavanja());
            return dto;
        });
    }

    //Nepolozeni ispiti
    public Page<IspitNepolozeniDTO> getNepolozeniIspiti(int broj, int godina, int page, int size) {
        StudentIndeks indeks = indeksRepo.findByBrojAndGodina(broj, godina).orElse(null);

        if (indeks == null) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("ispit.datumOdrzavanja").descending()
        );

        Page<IspitPrijava> prijave = ispitPrijavaRepository.findByStudentIndeksAndPolozenFalse(indeks, pageable);

        return prijave.map(ip -> {
            IspitNepolozeniDTO dto = new IspitNepolozeniDTO();
            dto.setPredmet(ip.getIspit().getPredmet().getNaziv());
            dto.setDatum(ip.getIspit().getDatumOdrzavanja());
            dto.setUkupanBrojPoena(ip.getUkupnoPoena()); // może null ako nije izasao
            return dto;
        });
    }

    //Upisane godine
    public List<UpisGodineDTO> getUpisaneGodine(int broj, int godina) {

        StudentIndeks indeks = indeksRepo.findByBrojAndGodina(broj,godina).orElse(null);

        if (indeks == null)
            return Collections.emptyList();

        List<UpisGodine> list = upisGodineRepository.findPrenetiPredmeti(indeks);

        return EntityMappers.toUpisGodineDTOList(list);
    }

    //Upisivanje studenata
    @Transactional
    public UpisGodineDTO upisiGodinu(UpisGodineRequest req) {

        StudentIndeks indeks = indeksRepo.findByBrojAndGodina(req.getBrojIndeksa(), req.getGodinaUpisa())
                .orElseThrow(() -> new RuntimeException("Ne postoji student sa ovim indeksom"));

        StudijskiProgram sp = indeks.getStudijskiProgram();
        List<Predmet> sviPredmetiNaProgramu = predmetRepository.findByStudProgram(sp);

        List<IspitPrijava> polozeni = ispitPrijavaRepository.findByStudentIndeksAndPolozenTrue(indeks);

        Set<Long> polozeniId = polozeni.stream().map(ip -> ip.getIspit().getPredmet().getId()).collect(Collectors.toSet());

        List<Predmet> preneti = sviPredmetiNaProgramu.stream().filter(p -> !polozeniId.contains(p.getId())).collect(Collectors.toList());

        UpisGodine ug = new UpisGodine();
        ug.setGodinaKojaSeUpisuje(req.getGodinaKojaSeUpisuje());
        ug.setNapomena(req.getNapomena());
        ug.setDatumUpisa(LocalDate.now());
        ug.setStudentIndeks(indeks);
        ug.setPrenetiPredmeti(preneti);

        ug = upisGodineRepository.save(ug);

        return EntityMappers.toUpisGodineDTO(ug);
    }

    //Pregled obnovljenih godina
    @Transactional
    public List<ObnovaGodineDTO> getObnovljeneGodine(int broj, int godina) {

        StudentIndeks indeks = indeksRepo.findByBrojAndGodina(broj, godina).orElse(null);

        if (indeks == null)
            return Collections.emptyList();

        List<ObnovaGodine> list = obnovaGodineRepository.findByStudentIndeksOrderByDatumObnoveDesc(indeks);

        return EntityMappers.toObnovaGodineDTOList(list);
    }

    //Obnova godine sa predmetima iz sledece godine
    @Transactional
    public ObnovaGodineDTO obnovaGodine(ObnovaGodineRequest req) {

        StudentIndeks indeks = indeksRepo.findByBrojAndGodina(req.getBrojIndeksa(), req.getGodinaUpisa()).orElseThrow(() -> new RuntimeException("Indeks ne postoji"));

        List<IspitPrijava> nepolozeni = ispitPrijavaRepository.findByStudentIndeksAndPolozenFalse(indeks);

        Set<Long> nepolozeniId = nepolozeni.stream().map(ip -> ip.getIspit().getPredmet().getId())
                .collect(Collectors.toSet());

        List<Predmet> predmetiNepolozeni = predmetRepository.findAllByIdIn(new ArrayList<>(nepolozeniId));

        List<Predmet> predmetiIducaGodina = predmetRepository.findAllByIdIn(req.getIdPredmetaIzNaredneGodine());

        // Nisam najbolje skontao da li ukljucuje i stare godine ili samo za sledecu godinu za ESPB
        int espb1 = predmetiNepolozeni.stream().mapToInt(Predmet::getEspb).sum();
        int espb2 = predmetiIducaGodina.stream().mapToInt(Predmet::getEspb).sum();

        int ukupno = espb1 + espb2;

        if (ukupno > 60) {
            throw new RuntimeException("Ukupno ESPB (" + ukupno + ") prelazi dozvoljenih 60.");
        }

        ObnovaGodine og = new ObnovaGodine();
        og.setGodinaKojaSeObnavlja(req.getGodinaKojaSeObnavlja());
        og.setDatumObnove(LocalDate.now());
        og.setNapomena(req.getNapomena());
        og.setStudentIndeks(indeks);

        List<Predmet> finalPredmeti = new ArrayList<>();
        finalPredmeti.addAll(predmetiNepolozeni);
        finalPredmeti.addAll(predmetiIducaGodina);

        og.setUpisaniPredmeti(finalPredmeti);

        og = obnovaGodineRepository.save(og);

        return EntityMappers.toObnovaGodineDTO(og);
    }

    //Dodavanje nove uplate
    @Transactional
    public UplataDTO dodajUplatu(UplataRequest req) {

        StudentIndeks indeks = indeksRepo.findByBrojAndGodina(req.getBrojIndeksa(), req.getGodinaUpisa())
                .orElseThrow(() -> new RuntimeException("Indeks ne postoji"));

        //Api za kurs
        String url = "https://kurs.resenje.org/api/v1/currencies/eur/rates/today";

        Map response = restTemplate.getForObject(url, Map.class);

        if (response == null || !response.containsKey("exchange_middle")) {
            throw new RuntimeException("Ne mogu da dohvatim srednji kurs.");
        }

        double srednjiKurs = Double.parseDouble(response.get("exchange_middle").toString());

        Uplata u = new Uplata();
        u.setDatumUplate(LocalDate.now());
        u.setIznosDin(req.getIznosRsd());
        u.setSrednjiKurs(srednjiKurs);
        u.setStudentIndeks(indeks);

        u = uplataRepository.save(u);

        return EntityMappers.toUplataDTO(u);
    }

    //Preostalo da se uplati iznios
    public PreostalaUplataDTO preostaloZaUplatu(int broj, int godina) {

        StudentIndeks indeks = indeksRepo.findByBrojAndGodina(broj, godina)
                .orElseThrow(() -> new RuntimeException("Indeks nije pronađen"));

        List<Uplata> uplate = uplataRepository.findByStudentIndeks(indeks);

        double ukupnoEUR = 0;

        for (Uplata u : uplate) {
            ukupnoEUR += u.getIznosDin() / u.getSrednjiKurs();
        }

        //Stavio da ako je presao student sa uplatama da ga drzi na nuli da ne ide u -
        double preostaloEur = 3000 - ukupnoEUR;
        if (preostaloEur < 0) preostaloEur = 0;

        String url = "https://kurs.resenje.org/api/v1/currencies/eur/rates/today";
        Map response = restTemplate.getForObject(url, Map.class);

        if (response == null || !response.containsKey("exchange_middle")) {
            throw new RuntimeException("Ne mogu da dohvatim srednji kurs.");
        }

        double danasnjiKurs = Double.parseDouble(response.get("exchange_middle").toString());

        double preostaloRsd = preostaloEur * danasnjiKurs;

        return new PreostalaUplataDTO(round(ukupnoEUR), round(preostaloEur), round(preostaloRsd), danasnjiKurs);
    }
    //Zaokruzivanje na 2 decimale
    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    //Pretraga po imenu ili prezimenu ili oba
    public Page<StudentDTO> pretraziStudente(String ime, String prezime, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<StudentPodaci> result;

        //Ime
        if (ime != null && !ime.isBlank() && (prezime == null || prezime.isBlank())) {
            result = studentPodaciRepository.findByImeContainingIgnoreCase(ime, pageable);
        }
        //Prezime
        else if (prezime != null && !prezime.isBlank() && (ime == null || ime.isBlank())) {
            result = studentPodaciRepository.findByPrezimeContainingIgnoreCase(prezime, pageable);
        }
        //Oba
        else if (ime != null && !ime.isBlank() && prezime != null && !prezime.isBlank()) {
            result = studentPodaciRepository
                    .findByImeContainingIgnoreCaseAndPrezimeContainingIgnoreCase(ime, prezime, pageable);
        }
        else {
            return Page.empty();
        }

        return result.map(sp -> {
            StudentDTO dto = EntityMappers.fromStudentPodaciToDTO(sp);

            studentIndeksRepository.findByStudentAndAktivanTrue(sp).ifPresent(ind -> {
                dto.setIdIndeks(ind.getId());
                dto.setBroj(ind.getBroj());
                dto.setGodinaUpisa(ind.getGodina());
                dto.setStudProgramOznaka(ind.getStudProgramOznaka());
                dto.setAktivanIndeks(ind.isAktivan());
                dto.setStudijskiProgramId(ind.getStudijskiProgram().getId());
            });

            return dto;
        });
    }

    //Pretraga po zavrsenoj srednjoj skoli
    public Page<StudentDTO> findByNazivSrednjeSkole(String naziv, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<StudentPodaci> result =
                studentPodaciRepository.findBySrednjaSkola_NazivContainingIgnoreCase(naziv, pageable);

        return result.map(sp -> {
            StudentDTO dto = EntityMappers.fromStudentPodaciToDTO(sp);

            studentIndeksRepository.findByStudentAndAktivanTrue(sp).ifPresent(ind -> {
                dto.setIdIndeks(ind.getId());
                dto.setBroj(ind.getBroj());
                dto.setGodinaUpisa(ind.getGodina());
                dto.setStudProgramOznaka(ind.getStudProgramOznaka());
                dto.setAktivanIndeks(ind.isAktivan());
                dto.setStudijskiProgramId(ind.getStudijskiProgram().getId());
            });

            return dto;
        });
    }

    //Selekciaja predispotnih poena
    public List<PredispitniPoeniDTO> selektujPredispitnePoene(
            Long indeksId, Long predmetId, Long skolskaGodinaId) {

        List<IspitPrijava> lista =
                ispitPrijavaRepository.findPredispitniPoeni(indeksId, predmetId, skolskaGodinaId);

        return lista.stream().map(EntityMappers::toPredispitniPoeniDTO).collect(Collectors.toList());
    }

}
