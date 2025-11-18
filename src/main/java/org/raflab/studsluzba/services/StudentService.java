package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentPodaci;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.model.dtos.StudentDTO;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentPodaciRepository studentRepo;

    @Autowired
    private StudentIndeksRepository indeksRepo;

    @Autowired
    private StudijskiProgramRepository programRepo;

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
}
