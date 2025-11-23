package org.raflab.studsluzba.utils;

import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class EntityMappers {

    //Ispit DTO
    public static IspitDTO fromIspitToDTO(Ispit ispit) {
        IspitDTO dto = new IspitDTO();
        dto.setId(ispit.getId());
        dto.setDatumOdrzavanja(ispit.getDatumOdrzavanja());
        dto.setVremePocetka(ispit.getVremePocetka());
        dto.setZakljucen(ispit.isZakljucen());

        if (ispit.getPredmet() != null) {
            dto.setPredmetId(ispit.getPredmet().getId());
            dto.setPredmetNaziv(ispit.getPredmet().getNaziv());
        }

        if (ispit.getNastavnik() != null) {
            dto.setNastavnikId(ispit.getNastavnik().getId());
            dto.setNastavnikImePrezime(
                    ispit.getNastavnik().getIme() + " " + ispit.getNastavnik().getPrezime()
            );
        }

        if (ispit.getIspitniRok() != null) {
            dto.setIspitniRokId(ispit.getIspitniRok().getId());
            dto.setIspitniRokNaziv(ispit.getIspitniRok().getNaziv());
        }

        return dto;
    }

    //DTO ispit
    public static Ispit fromDTOToIspit(IspitDTO dto, PredmetRepository predmetRepo, NastavnikRepository nastavnikRepo, IspitniRokRepository ispitniRokRepo) {

        Ispit ispit = new Ispit();

        ispit.setId(dto.getId());
        ispit.setDatumOdrzavanja(dto.getDatumOdrzavanja());
        ispit.setVremePocetka(dto.getVremePocetka());
        ispit.setZakljucen(dto.isZakljucen());

        if (dto.getPredmetId() != null)
            ispit.setPredmet(predmetRepo.findById(dto.getPredmetId()).orElse(null));

        if (dto.getNastavnikId() != null)
            ispit.setNastavnik(nastavnikRepo.findById(dto.getNastavnikId()).orElse(null));

        if (dto.getIspitniRokId() != null)
            ispit.setIspitniRok(ispitniRokRepo.findById(dto.getIspitniRokId()).orElse(null));

        return ispit;
    }

    //Ispit prijava DTO
    public static RezultatIspitaDTO fromIspitPrijavaToRezultatDTO(IspitPrijava ip) {

        RezultatIspitaDTO dto = new RezultatIspitaDTO();

        StudentIndeks si = ip.getStudentIndeks();
        StudentPodaci sp = si.getStudent();

        dto.setIme(sp.getIme());
        dto.setPrezime(sp.getPrezime());

        dto.setStudProgramOznaka(si.getStudProgramOznaka());
        dto.setGodinaUpisa(si.getGodina());
        dto.setBroj(si.getBroj());

        int predispitni = Optional.ofNullable(ip.getPredispitniPoeni()).orElse(0);
        int saIspita = Optional.ofNullable(ip.getPoeniSaIspita()).orElse(0);

        dto.setPredispitniPoeni(predispitni);
        dto.setPoeniSaIspita(saIspita);
        dto.setUkupnoPoena(predispitni + saIspita);

        dto.setPolozen(ip.isPolozen());

        return dto;
    }

    //Lista za ispit prijava DTO
    public static List<RezultatIspitaDTO> toRezultatIspitaDTOList(List<IspitPrijava> prijave) {

        return prijave.stream()
                .map(EntityMappers::fromIspitPrijavaToRezultatDTO)
                .sorted(Comparator
                        .comparing(RezultatIspitaDTO::getStudProgramOznaka)
                        .thenComparing(RezultatIspitaDTO::getGodinaUpisa)
                        .thenComparing(RezultatIspitaDTO::getBroj)
                )
                .collect(Collectors.toList());
    }

    //Predispitni poeni DTO
    public static PredispitniPoeniDTO toPredispitniPoeniDTO(IspitPrijava ip) {
        PredispitniPoeniDTO dto = new PredispitniPoeniDTO();

        dto.setPredmet(ip.getIspit().getPredmet().getNaziv());
        dto.setPredispitniPoeni(
                Optional.ofNullable(ip.getPredispitniPoeni()).orElse(0)
        );

        dto.setIndeksId(ip.getStudentIndeks().getId());
        dto.setBrojIndeksa(ip.getStudentIndeks().getBroj());
        dto.setGodinaUpisa(ip.getStudentIndeks().getGodina());
        dto.setStudijskiProgram(ip.getStudentIndeks().getStudProgramOznaka());
        dto.setDatum(ip.getIspit().getDatumOdrzavanja());

        return dto;
    }

    //Ispit prijava u DTO
    public static PrijavljeniStudentDTO fromIspitPrijavaToDTO(IspitPrijava ip) {

        PrijavljeniStudentDTO dto = new PrijavljeniStudentDTO();

        dto.setPrijavaId(ip.getId());

        StudentIndeks indeks = ip.getStudentIndeks();
        StudentPodaci sp = indeks.getStudent();

        dto.setStudentId(sp.getId());
        dto.setIme(sp.getIme());
        dto.setPrezime(sp.getPrezime());

        dto.setIndeksId(indeks.getId());
        dto.setBroj(indeks.getBroj());
        dto.setGodinaUpisa(indeks.getGodina());
        dto.setStudProgramOznaka(indeks.getStudProgramOznaka());

        if(ip.getUkupnoPoena() == null) dto.setOsvojeniPoeni(0);
        else dto.setOsvojeniPoeni(ip.getUkupnoPoena());
        dto.setOcena(ip.getOcena());

        return dto;
    }



    public static StudentDTO toStudnetDTO(StudentPodaci sp, StudentIndeks si) {
        StudentDTO dto = new StudentDTO();

        dto.setId(sp.getId());
        dto.setIme(sp.getIme());
        dto.setPrezime(sp.getPrezime());
        dto.setSrednjeIme(sp.getSrednjeIme());
        dto.setJmbg(sp.getJmbg());
        dto.setEmail(sp.getEmail());

        if (si != null) {
            dto.setIdIndeks(si.getId());
            dto.setGodinaUpisa(si.getGodina());
            dto.setStudProgramOznaka(si.getStudProgramOznaka());
            dto.setBroj(si.getBroj());
            dto.setAktivanIndeks(si.isAktivan());
            dto.setStudijskiProgramId(
                    si.getStudijskiProgram() != null ? si.getStudijskiProgram().getId() : null
            );
        }

        return dto;
    }

    public static StudentPodaci fromDTOToStudentPodaci(StudentDTO dto) {
        StudentPodaci sp = new StudentPodaci();
        sp.setId(dto.getId());
        sp.setIme(dto.getIme());
        sp.setPrezime(dto.getPrezime());
        sp.setSrednjeIme(dto.getSrednjeIme());
        sp.setJmbg(dto.getJmbg());
        sp.setEmail(dto.getEmail());
        return sp;
    }

    public static StudentIndeks fromDTOToStudentIndeks(StudentDTO dto, StudentPodaci sp, StudijskiProgram program) {
        StudentIndeks si = new StudentIndeks();
        si.setId(dto.getIdIndeks());
        si.setBroj(dto.getBroj());
        si.setGodina(dto.getGodinaUpisa());
        si.setStudProgramOznaka(dto.getStudProgramOznaka());
        si.setAktivan(dto.isAktivanIndeks());
        si.setStudent(sp);
        si.setStudijskiProgram(program);
        return si;
    }

	public static StudentDTO fromStudentPodaciToDTO(StudentPodaci sp) {
		StudentDTO dto = new StudentDTO();
		dto.setId(sp.getId());
        dto.setIme(sp.getIme());
        dto.setPrezime(sp.getPrezime());
        dto.setSrednjeIme(sp.getSrednjeIme());
        dto.setJmbg(sp.getJmbg());
        dto.setEmail(sp.getEmail());

		return dto;
	}

    //Dodao DTO converter za Predmet
    public static PredmetDTO fromPredmetToDTO(Predmet p) {
        PredmetDTO pd = new PredmetDTO();
        pd.setId(p.getId());
        pd.setEspb(p.getEspb());
        pd.setNaziv(p.getNaziv());
        pd.setSifra(p.getSifra());
        pd.setObavezan(p.isObavezan());
        if (p.getStudProgram() != null) {
            pd.setStudProgramNaziv(p.getStudProgram().getNaziv());
        }
        return pd;
    }
    //Dodajem DTO listu
    public static List<PredmetDTO> toPredmetDTOList(Iterable<Predmet> predmetIterable) {
        List<PredmetDTO> dtos = new ArrayList<>();
        predmetIterable.forEach(predmet -> dtos.add(fromPredmetToDTO(predmet)));
        return dtos;
    }
    //Dodao predmet converter za DTO
    public static Predmet fromDTOToPredmet(PredmetDTO predmetDTO) {
        Predmet p = new Predmet();
        p.setId(predmetDTO.getId());
        p.setEspb(predmetDTO.getEspb());
        p.setNaziv(predmetDTO.getNaziv());
        p.setSifra(predmetDTO.getSifra());
        p.setObavezan(predmetDTO.isObavezan());
        return p;
    }
	
	public static StudentDTO fromStudentIndeksToDTO(StudentIndeks si) {
		StudentDTO s = fromStudentPodaciToDTO(si.getStudent());	
		s.setIdIndeks(si.getId());
		s.setGodinaUpisa(si.getGodina());
		s.setBroj(si.getBroj());
		s.setStudProgramOznaka(si.getStudProgramOznaka());
		s.setAktivanIndeks(si.isAktivan());
		return s;
	}

    public StudentIndeksResponse fromStudentIndexToResponse(StudentIndeks si) {
        if (si == null) {
            return null;
        }
        StudentIndeksResponse response = new StudentIndeksResponse();
        response.setId(si.getId());
        response.setBroj(si.getBroj());
        response.setGodina(si.getGodina());
        response.setStudProgramOznaka(si.getStudProgramOznaka());
        response.setNacinFinansiranja(si.getNacinFinansiranja());
        response.setAktivan(si.isAktivan());
        response.setVaziOd(si.getVaziOd());
        response.setOstvarenoEspb(si.getOstvarenoEspb());
        response.setStudijskiProgram(si.getStudijskiProgram());
        response.setStudent(si.getStudent());
        return response;
    }

    public StudentPodaciResponse fromStudentPodaciToResponse(StudentPodaci sp) {
        if (sp == null) {
            return null;
        }

        StudentPodaciResponse response = new StudentPodaciResponse();
        response.setId(sp.getId());
        response.setIme(sp.getIme());
        response.setPrezime(sp.getPrezime());
        response.setSrednjeIme(sp.getSrednjeIme());
        response.setJmbg(sp.getJmbg());
        response.setDatumRodjenja(sp.getDatumRodjenja());
        response.setMestoRodjenja(sp.getMestoRodjenja());
        response.setMestoPrebivalista(sp.getMestoPrebivalista());
        response.setDrzavaRodjenja(sp.getDrzavaRodjenja());
        response.setDrzavljanstvo(sp.getDrzavljanstvo());
        response.setNacionalnost(sp.getNacionalnost());
        response.setPol(sp.getPol());
        response.setAdresa(sp.getAdresa());
        response.setBrojTelefonaMobilni(sp.getBrojTelefonaMobilni());
        response.setBrojTelefonaFiksni(sp.getBrojTelefonaFiksni());
        response.setEmail(sp.getEmail());
        response.setBrojLicneKarte(sp.getBrojLicneKarte());
        response.setLicnuKartuIzdao(sp.getLicnuKartuIzdao());
        response.setMestoStanovanja(sp.getMestoStanovanja());
        response.setAdresaStanovanja(sp.getAdresaStanovanja());

        return response;
    }

    public static List<UpisGodineDTO> toUpisGodineDTOList(List<UpisGodine> list) {
        return list.stream().map(EntityMappers::toUpisGodineDTO).collect(Collectors.toList());
    }

    //Upis to DTO
    public static UpisGodineDTO toUpisGodineDTO(UpisGodine ug) {

        UpisGodineDTO dto = new UpisGodineDTO();

        dto.setId(ug.getId());
        dto.setGodinaKojaSeUpisuje(ug.getGodinaKojaSeUpisuje());
        dto.setDatumUpisa(ug.getDatumUpisa());
        dto.setNapomena(ug.getNapomena());

        if (ug.getPrenetiPredmeti() != null) {
            dto.setPrenetiPredmeti(ug.getPrenetiPredmeti().stream().map(Predmet::getNaziv).collect(Collectors.toList()));
        } else {
            dto.setPrenetiPredmeti(Collections.emptyList());
        }

        return dto;
    }

    //Obnova godine u DTO
    public static ObnovaGodineDTO toObnovaGodineDTO(ObnovaGodine og) {

        ObnovaGodineDTO dto = new ObnovaGodineDTO();
        dto.setId(og.getId());
        dto.setGodinaKojaSeObnavlja(og.getGodinaKojaSeObnavlja());
        dto.setDatumObnove(og.getDatumObnove());
        dto.setNapomena(og.getNapomena());

        if (og.getUpisaniPredmeti() != null) {
            dto.setPredmeti(og.getUpisaniPredmeti().stream().map(Predmet::getNaziv).collect(Collectors.toList()));
        } else {
            dto.setPredmeti(Collections.emptyList());
        }

        return dto;
    }

    public static List<ObnovaGodineDTO> toObnovaGodineDTOList(List<ObnovaGodine> list) {
        return list.stream().map(EntityMappers::toObnovaGodineDTO).collect(Collectors.toList());
    }

    public static UplataDTO toUplataDTO(Uplata u) {
        return new UplataDTO(u.getId(), u.getDatumUplate(), u.getIznosDin(), u.getSrednjiKurs());
    }

}
