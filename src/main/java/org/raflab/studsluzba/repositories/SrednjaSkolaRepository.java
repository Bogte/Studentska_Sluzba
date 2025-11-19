package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.SrednjaSkola;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SrednjaSkolaRepository extends CrudRepository<SrednjaSkola, Long> {

    Optional<SrednjaSkola> findByNaziv(String naziv);
}
