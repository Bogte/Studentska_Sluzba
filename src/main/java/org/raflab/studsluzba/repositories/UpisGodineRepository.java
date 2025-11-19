package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.UpisGodine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UpisGodineRepository extends CrudRepository<UpisGodine, Long> {

    @Query("SELECT DISTINCT ug " +
       "FROM UpisGodine ug " +
       "LEFT JOIN FETCH ug.prenetiPredmeti " +
       "WHERE ug.studentIndeks = :indeks " +
       "ORDER BY ug.datumUpisa DESC ")
    List<UpisGodine> findPrenetiPredmeti(@Param("indeks") StudentIndeks indeks);
}
