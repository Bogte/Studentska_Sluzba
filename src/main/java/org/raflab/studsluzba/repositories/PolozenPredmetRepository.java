package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PolozenPredmet;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolozenPredmetRepository extends CrudRepository<PolozenPredmet, Long> {

    boolean existsByStudentIndeksAndPredmet(StudentIndeks indeks, Predmet predmet);

    List<PolozenPredmet> findByStudentIndeks(StudentIndeks indeks);
}
