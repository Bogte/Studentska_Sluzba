package org.raflab.studsluzba.repositories;

import java.util.List;
import java.util.Optional;

import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PredmetRepository extends CrudRepository<Predmet, Long> {
	
	@Query("select p from Predmet p where p.studProgram.godinaAkreditacije = :godinaAkreditacije")
	List<Predmet> getPredmetForGodinaAkreditacije(Integer godinaAkreditacije);

	//Vraca nazive predmeta za odredjeni studentski program
	@Query("select p from Predmet p where p.studProgram.id = :studProgramId")
	List<Predmet> getPredmetForStudentskiProgram(@Param("studProgramId") Long studProgramId);
	//Vraca sve podatke o svim predmetima
	@Query("select p from Predmet p")
	List<Predmet> getAllPredmet();
	//Trazimo predmet po sifri
	Optional<Predmet> findBySifra(String sifra);


	List<Predmet> getPredmetsByStudProgramAndObavezan(StudijskiProgram studProgram, boolean obavezan);

	List<Predmet> findByIdIn(List<Long> ids);
	List<Predmet> findByNazivIn(List<String> nazivi);
}
