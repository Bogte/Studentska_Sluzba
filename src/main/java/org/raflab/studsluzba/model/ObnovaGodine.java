package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class ObnovaGodine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int godinaKojaSeObnavlja;
    private LocalDate datumObnove;
    private String napomena;

    @ManyToOne
    private StudentIndeks studentIndeks;

    @ManyToMany
    private List<Predmet> upisaniPredmeti;
}
