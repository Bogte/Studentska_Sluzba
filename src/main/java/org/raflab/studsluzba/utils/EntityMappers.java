package org.raflab.studsluzba.utils;

import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.*;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMappers {


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
