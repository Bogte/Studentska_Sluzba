package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.IspitPrijava;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IspitPrijavaRepository extends JpaRepository<IspitPrijava, Long> {
    @Query("SELECT AVG(ip.ocena) FROM IspitPrijava ip " +
            "WHERE ip.ispit.predmet.id = :predmetId " +
            "AND ip.polozen = true " +
            "AND FUNCTION('YEAR', ip.ispit.datumOdrzavanja) BETWEEN :odGodine AND :doGodine")
    Double findProsecnaOcenaZaPredmetIUPeriodu(@Param("predmetId") Long predmetId,
                                               @Param("odGodine") int odGodine,
                                               @Param("doGodine") int doGodine);

    Page<IspitPrijava> findByStudentIndeksAndPolozenTrue(StudentIndeks indeks, Pageable pageable);

    Page<IspitPrijava> findByStudentIndeksAndPolozenFalse(StudentIndeks indeks, Pageable pageable);

    List<IspitPrijava> findByStudentIndeksAndPolozenTrue(StudentIndeks indeks);

    List<IspitPrijava> findByStudentIndeksAndPolozenFalse(StudentIndeks indeks);
}
