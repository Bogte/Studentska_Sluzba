package org.raflab.studsluzba.repositories;

import java.util.List;

import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentPodaci;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SlusaPredmetRepository extends JpaRepository<SlusaPredmet, Long> {
	
	@Query("select sp from SlusaPredmet sp where sp.studentIndeks.id = :indeksId1")
	List<SlusaPredmet> getSlusaPredmetForIndeksAktivnaGodina(Long indeksId);
	
	
	@Query("select sp.studentIndeks from SlusaPredmet sp where sp.drziPredmet.predmet.id = :idPredmeta "
			+ "and sp.drziPredmet.nastavnik.id = :idNastavnika  ")
	List<StudentIndeks> getStudentiSlusaPredmetAktivnaGodina(Long idPredmeta, Long idNastavnika);
	
	
	@Query("select sp.studentIndeks from SlusaPredmet sp where sp.drziPredmet.id = :idDrziPredmet")
	List<StudentIndeks> getStudentiSlusaPredmetZaDrziPredmet(Long idDrziPredmet);

	//Upit za studente koji ne su na odredjenom studijskom programu ali ne slusaju odredjeni predmet
	@Query("select si.student from StudentIndeks si "
	+ "where si.studijskiProgram.naziv = :nazivPrograma "
  	+ "and not exists (select sp from SlusaPredmet sp "
    + "join sp.drziPredmet dp "
    + "join dp.predmet p "
    + "where sp.studentIndeks = si "
    + "and p.naziv = :nazivPredmeta)")
	List<StudentPodaci> getStudentiNaProgramuKojiNeSlusajuPredmet(
			String nazivPrograma,
			String nazivPredmeta);


	/*
	 * TODO dodati slicne operacije koja vracaju sve studente za stud program/ godinu studija koje ne slusaju predmet
	 */
	@Query("select si from StudentIndeks si where not exists "
			+ "(select sp from SlusaPredmet sp where sp.studentIndeks=si and sp.drziPredmet.id = :idDrziPredmet) ")
	List<StudentIndeks> getStudentiNeSlusajuDrziPredmet(Long idDrziPredmet);

	//Student slusa predmet?
	boolean existsByStudentIndeksAndDrziPredmet_Predmet_Id(StudentIndeks indeks, Long predmetId);

	SlusaPredmet findByStudentIndeksAndDrziPredmet_Predmet(StudentIndeks studentIndeks, Predmet drziPredmetPredmet);
}
