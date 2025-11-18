package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Data
public class Uplata {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate datumUplate;
    private double iznosDin;
    private double srednjiKurs;
    private double iznosEur;

    @ManyToOne
    private StudentIndeks studentIndeks;
}
