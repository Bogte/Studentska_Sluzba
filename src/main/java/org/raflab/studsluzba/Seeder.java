package org.raflab.studsluzba;

import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

@Component
public class Seeder implements CommandLineRunner {

    @Autowired
    private StudijskiProgramRepository studijskiProgramRepository;
    @Autowired
    private PredmetRepository predmetRepository;
    @Autowired
    private NastavnikRepository nastavnikRepository;
    @Autowired
    private NastavnikZvanjeRepository nastavnikZvanjeRepository;
    @Autowired
    private StudentPodaciRepository studentPodaciRepository;
    @Autowired
    private StudentIndeksRepository studentIndeksRepository;
    @Autowired
    private DrziPredmetRepository drziPredmetRepository;
    @Autowired
    private SlusaPredmetRepository slusaPredmetRepository;
    @Autowired
    private GrupaRepository grupaRepository;
    @Autowired
    private IspitniRokRepository ispitniRokRepository;
    @Autowired
    private IspitRepository ispitRepository;
    @Autowired
    private IspitPrijavaRepository ispitPrijavaRepository;
    @Autowired
    private UpisGodineRepository upisGodineRepository;
    @Autowired
    private ObnovaGodineRepository obnovaGodineRepository;


    @Override
    public void run(String... args) throws Exception {
        List<StudijskiProgram> spList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            StudijskiProgram sp = new StudijskiProgram();
            sp.setOznaka("SP" + i);
            sp.setNaziv("Program " + i);
            sp.setGodinaAkreditacije(2020 + i);
            sp.setZvanje("Zvanje " + i);
            sp.setTrajanjeGodina(4);
            sp.setTrajanjeSemestara(8);
            sp.setVrstaStudija("OAS");
            sp.setUkupnoEspb(240);
            spList.add(studijskiProgramRepository.save(sp));
        }

        List<Predmet> predmetList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Predmet p = new Predmet();
            p.setSifra("PR" + i);
            p.setNaziv("Predmet " + i);
            p.setOpis("Opis predmeta " + i);
            p.setEspb(6 + i);
            p.setStudProgram(spList.get((i - 1) % spList.size()));
            p.setObavezan(i % 2 == 0);
            predmetList.add(predmetRepository.save(p));
        }

        List<Nastavnik> nastavnikList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Nastavnik n = new Nastavnik();
            n.setIme("Nastavnik" + i);
            n.setPrezime("Prezime" + i);
            n.setSrednjeIme("Srednje" + i);
            n.setEmail("nastavnik" + i + "@example.com");
            n.setBrojTelefona("06012345" + i);
            n.setAdresa("Adresa " + i);
            n.setDatumRodjenja(LocalDate.of(1980 + i, i, i));
            n.setPol(i % 2 == 0 ? 'M' : 'F');
            n.setJmbg("80010123456" + i);
            nastavnikList.add(nastavnikRepository.save(n));
        }

        List<NastavnikZvanje> zvanjeList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            NastavnikZvanje nz = new NastavnikZvanje();
            nz.setDatumIzbora(LocalDate.of(2020 + i, i, i));
            nz.setNaucnaOblast("Oblast " + i);
            nz.setUzaNaucnaOblast("Uza oblast " + i);
            nz.setZvanje("Zvanje " + i);
            nz.setAktivno(i % 2 == 0);
            nz.setNastavnik(nastavnikList.get(i - 1));
            zvanjeList.add(nastavnikZvanjeRepository.save(nz));
        }

        List<StudentPodaci> studentPodaciList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            StudentPodaci s = new StudentPodaci();
            s.setIme("Student" + i);
            s.setPrezime("Prezime" + i);
            s.setSrednjeIme("Srednje" + i);
            s.setJmbg("00101012345" + i);
            s.setDatumRodjenja(LocalDate.of(2000 + i, i, i));
            s.setMestoRodjenja("Mesto" + i);
            s.setMestoPrebivalista("Prebivaliste" + i);
            s.setDrzavaRodjenja("Srbija");
            s.setDrzavljanstvo("Srbija");
            s.setNacionalnost("Srpska");
            s.setPol(i % 2 == 0 ? 'F' : 'M');
            s.setAdresa("Adresa " + i);
            s.setBrojTelefonaMobilni("06123456" + i);
            s.setEmail("student" + i + "@example.com");
            studentPodaciList.add(studentPodaciRepository.save(s));
        }

        List<StudentIndeks> indeksList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            StudentIndeks si = new StudentIndeks();
            si.setBroj(i);
            si.setGodina(2023);
            si.setStudProgramOznaka(spList.get(i - 1).getOznaka());
            si.setNacinFinansiranja(i % 2 == 0 ? "Budzet" : "Samofinansiranje");
            si.setAktivan(true);
            si.setVaziOd(LocalDate.of(2023, 10, i));
            si.setStudent(studentPodaciList.get(i - 1));
            si.setStudijskiProgram(spList.get(i - 1));
            si.setOstvarenoEspb(0);
            indeksList.add(studentIndeksRepository.save(si));
        }

        List<DrziPredmet> drziPredmetList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            DrziPredmet dp = new DrziPredmet();
            dp.setNastavnik(nastavnikList.get(i - 1));
            dp.setPredmet(predmetList.get(i - 1));
            drziPredmetList.add(drziPredmetRepository.save(dp));
        }

        List<SlusaPredmet> slusaPredmetList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SlusaPredmet sl = new SlusaPredmet();
            sl.setStudentIndeks(indeksList.get(i - 1));
            sl.setDrziPredmet(drziPredmetList.get(i - 1));
            slusaPredmetList.add(slusaPredmetRepository.save(sl));
        }

        for (int i = 1; i <= 5; i++) {
            Grupa g = new Grupa();
            g.setStudijskiProgram(spList.get(i - 1));
            g.setPredmeti(Collections.singletonList(predmetList.get(i - 1)));
            grupaRepository.save(g);
        }

        List<IspitniRok> rokovi = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            IspitniRok ir = new IspitniRok();
            ir.setNaziv("Rok " + i);
            ir.setDatumPocetka(LocalDate.of(2024, i, 1));
            ir.setDatumZavrsetka(LocalDate.of(2024, i, 15));
            rokovi.add(ispitniRokRepository.save(ir));
        }

        List<Ispit> ispiti = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 10; i++) {
            Ispit ispit = new Ispit();
            ispit.setPredmet(predmetList.get(random.nextInt(predmetList.size())));
            ispit.setNastavnik(nastavnikList.get(random.nextInt(nastavnikList.size())));
            ispit.setIspitniRok(rokovi.get(random.nextInt(rokovi.size())));
            ispit.setDatumOdrzavanja(LocalDate.of(2024, random.nextInt(3) + 1, random.nextInt(28) + 1));
            ispit.setZakljucen(true);
            ispiti.add(ispitRepository.save(ispit));
        }

        for (int i = 1; i <= 20; i++) {
            IspitPrijava prijava = new IspitPrijava();
            prijava.setStudentIndeks(indeksList.get(random.nextInt(indeksList.size())));
            prijava.setIspit(ispiti.get(random.nextInt(ispiti.size())));
            prijava.setDatumPrijave(LocalDate.now().minusDays(random.nextInt(20)));

            boolean priznat = random.nextBoolean();
            prijava.setPriznat(priznat);

            if (priznat) {
                prijava.setOcena(random.nextInt(5) + 6);
                prijava.setPolozen(true);
            } else {
                int pp = random.nextInt(40);
                int isp = random.nextInt(60);
                int ukupno = pp + isp;
                prijava.setPredispitniPoeni(pp);
                prijava.setPoeniSaIspita(isp);
                prijava.setUkupnoPoena(ukupno);

                int ocena = ukupno < 51 ? 5 : ukupno < 61 ? 6 : ukupno < 71 ? 7 : ukupno < 81 ? 8 : ukupno < 91 ? 9 : 10;

                prijava.setOcena(ocena);
                prijava.setPolozen(ocena > 5);
            }

            prijava.setNapomena("Automatski generisano");

            ispitPrijavaRepository.save(prijava);
        }

        for (int i = 1; i <= 5; i++) {
            UpisGodine upis = new UpisGodine();
            upis.setStudentIndeks(indeksList.get(i - 1));
            upis.setGodinaKojaSeUpisuje(1 + random.nextInt(4));
            upis.setDatumUpisa(LocalDate.of(2024, 10, random.nextInt(28) + 1));
            upis.setNapomena("Upisano automatski");
            upis.setPrenetiPredmeti(Arrays.asList(
                    predmetList.get(random.nextInt(predmetList.size()))
            ));
            upisGodineRepository.save(upis);
        }

        for (int i = 1; i <= 5; i++) {
            ObnovaGodine obnova = new ObnovaGodine();
            obnova.setStudentIndeks(indeksList.get(i - 1));
            obnova.setGodinaKojaSeObnavlja(1 + random.nextInt(4));
            obnova.setDatumObnove(LocalDate.of(2024, 9, random.nextInt(28) + 1));
            obnova.setNapomena("Obnova automatski");
            obnova.setUpisaniPredmeti(Arrays.asList(
                    predmetList.get(random.nextInt(predmetList.size()))
            ));
            obnovaGodineRepository.save(obnova);
        }
    }
}