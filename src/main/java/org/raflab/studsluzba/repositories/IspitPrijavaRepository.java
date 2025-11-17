package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.IspitPrijava;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
public interface IspitPrijavaRepository extends JpaRepository<IspitPrijava, Long> {
    @Query("SELECT AVG(ip.ocena) FROM IspitPrijava ip " +
            "WHERE ip.ispit.predmet.id = :predmetId " +
            "AND ip.polozen = true " +
            "AND FUNCTION('YEAR', ip.ispit.datumOdrzavanja) BETWEEN :odGodine AND :doGodine")
    Double findProsecnaOcenaZaPredmetIUPeriodu(@Param("predmetId") Long predmetId,
                                               @Param("odGodine") int odGodine,
                                               @Param("doGodine") int doGodine);
}
