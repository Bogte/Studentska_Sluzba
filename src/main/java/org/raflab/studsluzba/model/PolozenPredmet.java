package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class PolozenPredmet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate datumPolaganja;
    private Integer ukupnoPoena;
    private Integer ocena;
    private String napomena;

    @ManyToOne
    private StudentIndeks studentIndeks;

    @ManyToOne
    private Predmet predmet;
}